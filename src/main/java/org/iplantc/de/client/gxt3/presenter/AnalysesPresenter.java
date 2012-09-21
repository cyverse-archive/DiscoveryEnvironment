package org.iplantc.de.client.gxt3.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.gxt3.model.AnalysesAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.AnalysesList;
import org.iplantc.de.client.gxt3.model.Analysis;
import org.iplantc.de.client.gxt3.views.AnalysesToolbarView;
import org.iplantc.de.client.gxt3.views.AnalysesToolbarViewImpl;
import org.iplantc.de.client.gxt3.views.AnalysesView;
import org.iplantc.de.client.gxt3.views.AnalysesView.Presenter;
import org.iplantc.de.client.utils.NotificationHelper;
import org.iplantc.de.client.utils.NotifyInfo;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

/**
 * 
 * A presenter for analyses view
 * 
 * @author sriram
 * 
 */
public class AnalysesPresenter implements Presenter,
        org.iplantc.de.client.gxt3.views.AnalysesToolbarView.Presenter {

    private final AnalysesView view;
    private final AnalysesToolbarView toolbar;
    private AnalysesAutoBeanFactory factory = GWT.create(AnalysesAutoBeanFactory.class);

    /**
     * Indicates the status of an analysis.
     */
    public static enum EXECUTION_STATUS {
        /** analysis status unknown */
        UNKNOWN(I18N.CONSTANT.unknown()),
        /** analysis is ready */
        SUBMITTED(I18N.CONSTANT.submitted()),
        /** analysis is running */
        RUNNING(I18N.CONSTANT.running()),
        /** analysis is complete */
        COMPLETED(I18N.CONSTANT.completed()),
        /** analysis timed out */
        HELD(I18N.CONSTANT.held()),
        /** analysis failed */
        FAILED(I18N.CONSTANT.failed()),
        /** analysis was stopped */
        SUBMISSION_ERR(I18N.CONSTANT.subErr()),
        /** analysis is idle */
        IDLE(I18N.CONSTANT.idle()),
        /** analysis is removed */
        REMOVED(I18N.CONSTANT.removed());

        private String displayText;

        private EXECUTION_STATUS(String displaytext) {
            this.displayText = displaytext;
        }

        /**
         * Returns a string that identifies the EXECUTION_STATUS.
         * 
         * @return
         */
        public String getTypeString() {
            return toString().toLowerCase();
        }

        /**
         * Null-safe and case insensitive variant of valueOf(String)
         * 
         * @param typeString name of an EXECUTION_STATUS constant
         * @return
         */
        public static EXECUTION_STATUS fromTypeString(String typeString) {
            if (typeString == null || typeString.isEmpty()) {
                return null;
            }

            return valueOf(typeString.toUpperCase());
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    public AnalysesPresenter(AnalysesView view) {
        this.view = view;
        this.view.setPresenter(this);
        toolbar = new AnalysesToolbarViewImpl();
        toolbar.setPresenter(this);
        view.setNorthWidget(toolbar);
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        retrieveData();
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelClicked() {
        if (view.getSelectedAnalyses().size() > 0) {
            final List<Analysis> execs = view.getSelectedAnalyses();
            for (Analysis ae : execs) {
                if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.SUBMITTED.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.IDLE.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.RUNNING.toString()))) {
                    Services.ANALYSIS_SERVICE.stopAnalysis(ae.getId(),
                            new CancelAnalysisServiceCallback(ae));
                }
            }
        }

    }

    @Override
    public void onAnalysesSelection(List<Analysis> selectedItems) {
        if (selectedItems.size() > 0) {
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setViewParamButtonEnabled(true);
            toolbar.setCancelButtonEnabled(true);
        } else {
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setViewParamButtonEnabled(false);
            toolbar.setCancelButtonEnabled(false);
        }

    }

    private void retrieveData() {
        Services.ANALYSIS_SERVICE.getAnalyses(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AutoBean<AnalysesList> bean = AutoBeanCodex.decode(factory, AnalysesList.class,
                                result);
                        view.loadAnalyses(bean.as().getAnalysisList());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.DISPLAY.analysesRetrievalFailure(), caught);
                    }
                });
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
                if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.COMPLETED.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.FAILED.toString()))) {
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
