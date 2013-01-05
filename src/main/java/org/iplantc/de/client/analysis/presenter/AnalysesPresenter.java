package org.iplantc.de.client.analysis.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.analysis.models.AnalysesAutoBeanFactory;
import org.iplantc.de.client.analysis.models.AnalysesList;
import org.iplantc.de.client.analysis.models.Analysis;
import org.iplantc.de.client.analysis.models.AnalysisExecutionStatus;
import org.iplantc.de.client.analysis.models.AnalysisParameter;
import org.iplantc.de.client.analysis.models.AnalysisParameterProperties;
import org.iplantc.de.client.analysis.models.AnalysisParametersList;
import org.iplantc.de.client.analysis.views.AnalysesToolbarView;
import org.iplantc.de.client.analysis.views.AnalysesToolbarViewImpl;
import org.iplantc.de.client.analysis.views.AnalysesView;
import org.iplantc.de.client.analysis.views.AnalysesView.Presenter;
import org.iplantc.de.client.analysis.views.AnalysisParamView;
import org.iplantc.de.client.analysis.views.cells.AnalysisParamNameCell;
import org.iplantc.de.client.analysis.views.cells.AnalysisParamValueCell;
import org.iplantc.de.client.notifications.util.NotificationHelper;
import org.iplantc.de.client.utils.NotifyInfo;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * 
 * A presenter for analyses view
 * 
 * @author sriram
 * 
 */
public class AnalysesPresenter implements Presenter,
        org.iplantc.de.client.analysis.views.AnalysesToolbarView.Presenter {

    private final AnalysesView view;
    private final AnalysesToolbarView toolbar;
    private AnalysesAutoBeanFactory factory = GWT.create(AnalysesAutoBeanFactory.class);
    private PagingLoadResult<Analysis> callbackResult;

    public AnalysesPresenter(AnalysesView view) {
        this.view = view;
        this.view.setPresenter(this);
        toolbar = new AnalysesToolbarViewImpl();
        toolbar.setPresenter(this);
        view.setNorthWidget(toolbar);
        setRefreshButton(view.getRefreshButton());
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        view.setLoader(initRemoteLoader());
    }

    @Override
    public void onDeleteClicked() {
        if (view.getSelectedAnalyses().size() > 0) {
            final List<Analysis> execs = view.getSelectedAnalyses();

            ConfirmMessageBox cmb = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                    I18N.DISPLAY.analysesExecDeleteWarning());
            cmb.addHideHandler(new DeleteMessageBoxHandler(execs));
            cmb.show();
        }

    }

    @Override
    public void onViewParamClicked() {
        for (Analysis ana : view.getSelectedAnalyses()) {
            ListStore<AnalysisParameter> listStore = new ListStore<AnalysisParameter>(
                    new AnalysisParameterKeyProvider());
            final AnalysisParamView apv = new AnalysisParamView(listStore, buildColumnModel());
            retrieveParameterData(ana.getId(), new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    AutoBean<AnalysisParametersList> bean = AutoBeanCodex.decode(factory,
                            AnalysisParametersList.class, result);
                    apv.loadParameters(bean.as().getParameterList());
                }

                @Override
                public void onFailure(Throwable caught) {

                }
            });
            apv.setHeading(I18N.DISPLAY.viewParameters(ana.getName()));
            apv.show();
        }
    }

    private class AnalysisParameterKeyProvider implements ModelKeyProvider<AnalysisParameter> {

        @Override
        public String getKey(AnalysisParameter item) {
            return item.getId();
        }

    }

    private void setButtonState() {
        int selectionSize = 0;

        selectionSize = view.getSelectedAnalyses().size();

        switch (selectionSize) {
            case 0:
                toolbar.setCancelButtonEnabled(false);
                toolbar.setDeleteButtonEnabled(false);
                toolbar.setViewParamButtonEnabled(false);
                break;

            case 1:
                enableCancelAnalysisButtonByStatus();
                toolbar.setDeleteButtonEnabled(true);
                toolbar.setViewParamButtonEnabled(true);
                break;

            default:
                toolbar.setDeleteButtonEnabled(true);
                toolbar.setViewParamButtonEnabled(false);
                enableCancelAnalysisButtonByStatus();
        }
    }

    private void enableCancelAnalysisButtonByStatus() {
        List<Analysis> aes = view.getSelectedAnalyses();
        boolean enable = false;
        for (Analysis ae : aes) {
            if (ae != null) {
                if (ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.SUBMITTED.toString()))
                        || ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.IDLE.toString()))
                        || ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.RUNNING.toString()))) {
                    enable = true;
                    break;
                }
            }
        }
        toolbar.setCancelButtonEnabled(enable);
    }

    private void retrieveParameterData(final String analysisId, final AsyncCallback<String> callback) {
        Services.ANALYSIS_SERVICE.getAnalysisParams(analysisId, callback);
    }

    @Override
    public void onCancelClicked() {
        if (view.getSelectedAnalyses().size() > 0) {
            final List<Analysis> execs = view.getSelectedAnalyses();
            for (Analysis ae : execs) {
                if (ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.SUBMITTED.toString()))
                        || ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.IDLE.toString()))
                        || ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.RUNNING.toString()))) {
                    Services.ANALYSIS_SERVICE.stopAnalysis(ae.getId(),
                            new CancelAnalysisServiceCallback(ae));
                }
            }
        }

    }

    @Override
    public void onAnalysesSelection(List<Analysis> selectedItems) {
        setButtonState();
    }

    @Override
    public void setRefreshButton(TextButton refreshBtn) {
        if (refreshBtn != null) {
            refreshBtn.setText(I18N.DISPLAY.refresh());
            toolbar.setRefreshButton(refreshBtn);
        }
    }

    @SuppressWarnings("unchecked")
    private ColumnModel<AnalysisParameter> buildColumnModel() {
        AnalysisParameterProperties props = GWT.create(AnalysisParameterProperties.class);
        ColumnConfig<AnalysisParameter, AnalysisParameter> param_name = new ColumnConfig<AnalysisParameter, AnalysisParameter>(
                new IdentityValueProvider<AnalysisParameter>(), 175); //$NON-NLS-1$
        param_name.setHeader(I18N.DISPLAY.paramName());
        param_name.setCell(new AnalysisParamNameCell());

        ColumnConfig<AnalysisParameter, String> param_type = new ColumnConfig<AnalysisParameter, String>(
                props.type(), 75); //$NON-NLS-1$
        param_type.setHeader(I18N.DISPLAY.paramType());

        ColumnConfig<AnalysisParameter, AnalysisParameter> param_value = new ColumnConfig<AnalysisParameter, AnalysisParameter>(
                new IdentityValueProvider<AnalysisParameter>(), 325); //$NON-NLS-1$
        param_value.setHeader(I18N.DISPLAY.paramValue());
        param_value.setCell(new AnalysisParamValueCell());

        List<ColumnConfig<AnalysisParameter, ?>> columns = new ArrayList<ColumnConfig<AnalysisParameter, ?>>();
        columns.addAll(Arrays.asList(param_name, param_type, param_value));

        return new ColumnModel<AnalysisParameter>(columns);
    }

    /**
     * Initializes the RpcProxy and BasePagingLoader to support paging for the AnalysesGrid.
     */
    private PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> initRemoteLoader() {
        RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>>() {

            @Override
            public void load(PagingLoadConfig loadConfig,
                    AsyncCallback<PagingLoadResult<Analysis>> callback) {
                Services.ANALYSIS_SERVICE.getAnalyses(UserInfo.getInstance().getWorkspaceId(),
                        loadConfig, new GetAnalysesServiceCallback(loadConfig, callback));

            }
        };

        final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Analysis, PagingLoadResult<Analysis>>(
                view.getListStore()));

        return loader;

    }

    /**
     * An AsyncCallback for the AnalysisServiceFacade that will load paged results into the AnalysesGrid.
     * 
     * @author psarando
     * 
     */
    private final class GetAnalysesServiceCallback implements AsyncCallback<String> {
        private final AsyncCallback<PagingLoadResult<Analysis>> callback;
        private final PagingLoadConfig loadConfig;

        public GetAnalysesServiceCallback(PagingLoadConfig loadConfig,
                AsyncCallback<PagingLoadResult<Analysis>> pagingCallback) {
            this.callback = pagingCallback;
            this.loadConfig = loadConfig;
        }

        @Override
        public void onSuccess(String result) {
            JSONObject jsonResult = JsonUtil.getObject(result);
            JSONArray items = JsonUtil.getArray(jsonResult, "analyses"); //$NON-NLS-1$
            Number total = 0;
            if (items != null) {
                total = JsonUtil.getNumber(jsonResult, "total"); //$NON-NLS-1$
            }
            AutoBean<AnalysesList> bean = AutoBeanCodex.decode(factory, AnalysesList.class, result);
            List<Analysis> analyses = bean.as().getAnalysisList();
            callbackResult = new PagingLoadResultBean<Analysis>(analyses, total.intValue(),
                    loadConfig.getOffset());
            callback.onSuccess(callbackResult);

        }

        @Override
        public void onFailure(Throwable caught) {
            org.iplantc.core.uicommons.client.ErrorHandler.post(caught);
        }

    }

    private final class DeleteSeviceCallback implements AsyncCallback<String> {
        private final List<Analysis> execs;
        private final List<Analysis> items_to_delete;

        private DeleteSeviceCallback(List<Analysis> items_to_delete, List<Analysis> execs) {
            this.execs = execs;
            this.items_to_delete = items_to_delete;
        }

        @Override
        public void onSuccess(String arg0) {
            updateGrid();

        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.deleteAnalysisError(), caught);
        }

        private void updateGrid() {
            view.removeFromStore(items_to_delete);

            if (items_to_delete == null || execs.size() != items_to_delete.size()) {
                AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.warning(),
                        I18N.DISPLAY.analysesNotDeleted());
                amb.show();
            }
        }
    }

    private final class CancelAnalysisServiceCallback implements AsyncCallback<String> {

        private final Analysis ae;

        public CancelAnalysisServiceCallback(final Analysis ae) {
            this.ae = ae;
        }

        @Override
        public void onSuccess(String result) {
            NotifyInfo.notify(NotificationHelper.Category.ANALYSIS, I18N.DISPLAY.success(),
                    I18N.DISPLAY.analysisStopSuccess(ae.getName()), null);
        }

        @Override
        public void onFailure(Throwable caught) {
            /*
             * JDS Send generic error message. In the future, the "error_code" string should be parsed
             * from the JSON to provide more detailed user feedback.
             */
            ErrorHandler.post(I18N.ERROR.stopAnalysisError(ae.getName()), caught);
        }

    }

    private final class DeleteMessageBoxHandler implements HideHandler {
        private final List<Analysis> execs;
        private final List<Analysis> items_to_delete;

        private DeleteMessageBoxHandler(List<Analysis> execs) {
            this.execs = execs;
            items_to_delete = new ArrayList<Analysis>();
        }

        private String buildDeleteRequestBody(List<Analysis> execs) {
            JSONObject obj = new JSONObject();
            JSONArray items = new JSONArray();
            int count = 0;
            for (Analysis ae : execs) {
                if (ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.COMPLETED.toString()))
                        || ae.getStatus().equalsIgnoreCase((AnalysisExecutionStatus.FAILED.toString()))) {
                    items.set(count++, new JSONString(ae.getId()));
                    items_to_delete.add(ae);
                }

            }
            obj.put("executions", items); //$NON-NLS-1$
            return obj.toString();
        }

        @Override
        public void onHide(HideEvent event) {
            ConfirmMessageBox cmb = (ConfirmMessageBox)event.getSource();
            if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.YES.name())) {
                String body = buildDeleteRequestBody(execs);
                Services.ANALYSIS_SERVICE.deleteAnalysis(UserInfo.getInstance().getWorkspaceId(), body,
                        new DeleteSeviceCallback(items_to_delete, execs));
            }

        }
    }
}
