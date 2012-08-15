package org.iplantc.de.client.views;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.util.DateParser;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AnalysisUpdateEvent;
import org.iplantc.de.client.events.AnalysisUpdateEventHandler;
import org.iplantc.de.client.models.JsAnalysisExecution;
import org.iplantc.de.client.models.gxt3.AnalysisExecution;
import org.iplantc.de.client.models.gxt3.AnalysisExecutionProperties;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.views.panels.MyAnalysesPanel;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

/**
 * A grid that is used to display users Analyses
 * 
 * @author sriram
 * 
 */
public class MyAnalysesGrid extends Grid<AnalysisExecution> {

    private String idCurrentSelection;
    private ArrayList<HandlerRegistration> handlers;
    private static RowExpander<AnalysisExecution> expander;

    /**
     * Create a new MyAnalysesGrid
     * 
     * @param store store to be used by the grid
     * @param cm column model describing the columns in the grid
     */
    public MyAnalysesGrid(ListStore<AnalysisExecution> store, ColumnModel<AnalysisExecution> cm) {
        super(store, cm);
        registerHandlers();
    }

    /**
     * Set the id of our current selection.
     * 
     * @param idCurrentSelection id of currently selected analysis.
     */
    public void setCurrentSelection(final String idCurrentSelection) {
        this.idCurrentSelection = idCurrentSelection;
    }

    private void registerHandlers() {
        handlers = new ArrayList<HandlerRegistration>();
        EventBus eventbus = EventBus.getInstance();
        handlers.add(eventbus.addHandler(AnalysisUpdateEvent.TYPE, new AnalysisUpdateEventHandler() {

            @Override
            public void onUpdate(AnalysisUpdateEvent event) {
                if (event.getPayload() != null) {
                    handleMessage(event.getPayload());
                }
            }

        }));
    }

    private void handleMessage(JSONObject payload) {
        if ("job_status_change".equals(JsonUtil.getString(payload, "action"))) { //$NON-NLS-1$ //$NON-NLS-2$
            updateStore(payload);
        }
    }

    private void updateStore(JSONObject payload) {
        MyAnalysesPanel.EXECUTION_STATUS enumStatus = MyAnalysesPanel.EXECUTION_STATUS
                .fromTypeString(JsonUtil.getString(payload, "status")); //$NON-NLS-1$

        if (getStore().findModelWithKey(JsonUtil.getString(payload, "id")) != null) { //$NON-NLS-1$
            switch (enumStatus) {
                case COMPLETED:
                    updateEndExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            JsonUtil.getString(payload, "resultfolderid"), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getNumber(payload, "enddate").longValue())); //$NON-NLS-1$
                    break;

                case FAILED:
                    updateEndExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            JsonUtil.getString(payload, "resultfolderid"), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getNumber(payload, "enddate").longValue())); //$NON-NLS-1$
                    break;

                case RUNNING:
                    updateRunExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getString(payload, "startdate"))); //$NON-NLS-1$
                    break;

                case SUBMITTED:
                    updateRunExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getString(payload, "startdate"))); //$NON-NLS-1$
                    break;

                default:
                    updateExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString()); //$NON-NLS-1$
                    break;
            }
        } else {
            AnalysisExecution exec = buildAnalysisExecution(payload);
            getStore().add(exec);
        }

        sort();
    }

    private void sort() {
        //        getStore().sort("startdate", SortDir.DESC); //$NON-NLS-1$

        // TODO JDS - Implement sorting for Analysis grid
        AnalysisExecutionProperties props = GWT.create(AnalysisExecutionProperties.class);
        // getStore().addSortInfo(new StoreSortInfo<AnalysisExecution>(props.getStartDate(),
        // SortDir.DESC));
    }

    private void updateExecStatus(String id, String status) {
        AnalysisExecution ae = getStore().findModelWithKey(id);

        if (ae != null) {
            ae.setStatus(status);
            getStore().update(ae);
        }
    }

    private void updateEndExecStatus(String id, String status, String resultfolderid, Date enddate) {
        AnalysisExecution ae = getStore().findModelWithKey(id);

        if (ae != null) {
            ae.setStatus(status);
            if (enddate != null) {
                ae.setEndDate(enddate);
            }
            ae.setResultFolderId(resultfolderid);
            getStore().update(ae);
        }
    }

    private void updateRunExecStatus(String id, String status, Date startdate) {
        AnalysisExecution ae = getStore().findModelWithKey(id);

        if (ae != null) {
            ae.setStatus(status);
            if (startdate != null) {
                ae.setStartDate(startdate);
            }
            getStore().update(ae);
        }
    }

    private AnalysisExecution buildAnalysisExecution(JSONObject payload) {
        AnalysisExecution exec = new AnalysisExecution(payload);
        return exec;
    }

    /**
     * Allocate default instance.
     * 
     * @return newly allocated my analysis grid.
     */
    public static MyAnalysesGrid createInstance(CheckBoxSelectionModel<AnalysisExecution> sm) {
        return createInstanceImpl(sm);
    }

    private static MyAnalysesGrid createInstanceImpl(CheckBoxSelectionModel<AnalysisExecution> sm) {
        ListStore<AnalysisExecution> gstore = new ListStore<AnalysisExecution>(
                new ModelKeyProvider<AnalysisExecution>() {

                    @Override
                    public String getKey(AnalysisExecution item) {
                        return item.getId();
                    }
                });

        final ColumnModel<AnalysisExecution> colModel = buildColumnModel(sm);
        final MyAnalysesGrid grid = new MyAnalysesGrid(gstore, colModel);
        grid.setLoader(new ListLoader<PagingLoadConfig, PagingLoadResult<AnalysisExecution>>(
                new RpcProxy<PagingLoadConfig, PagingLoadResult<AnalysisExecution>>() {

                    @Override
                    public void load(PagingLoadConfig loadConfig,
                            AsyncCallback<PagingLoadResult<AnalysisExecution>> callback) {
                        grid.retrieveData();

                    }
                }));
        grid.setSelectionModel(sm);
        expander.initPlugin(grid);

        final LiveGridView<AnalysisExecution> liveGridView = new LiveGridView<AnalysisExecution>();
        liveGridView.setForceFit(true);

        // liveGridView.setAutoExpandMax(2048);
        grid.setView(liveGridView);

        return grid;
    }

    private static ColumnModel<AnalysisExecution> buildColumnModel(
            CheckBoxSelectionModel<AnalysisExecution> sm) {

        expander = new RowExpander<AnalysisExecution>(new IdentityValueProvider<AnalysisExecution>(),
                new ExpanderCell());
        sm.getColumn().setMenuDisabled(true);
        expander.setMenuDisabled(true);

        AnalysisExecutionProperties props = GWT.create(AnalysisExecutionProperties.class);
        ColumnConfig<AnalysisExecution, String> name = new ColumnConfig<AnalysisExecution, String>(
                props.getName(), 175, I18N.DISPLAY.name());
        ColumnConfig<AnalysisExecution, String> analysisname = new ColumnConfig<AnalysisExecution, String>(
                props.getAnalysisName(), 250, I18N.DISPLAY.appName());
        ColumnConfig<AnalysisExecution, Date> start = new ColumnConfig<AnalysisExecution, Date>(
                props.getStartDate(), 150, I18N.DISPLAY.startDate());
        ColumnConfig<AnalysisExecution, Date> end = new ColumnConfig<AnalysisExecution, Date>(
                props.getEndDate(), 150, I18N.DISPLAY.endDate());
        ColumnConfig<AnalysisExecution, String> status = new ColumnConfig<AnalysisExecution, String>(
                props.getStatus(), 100, I18N.DISPLAY.status());

        analysisname.setCell(new AnalysisNameCell());

        name.setCell(new AppNameCell());
        
        List<ColumnConfig<AnalysisExecution, ?>> columns = new ArrayList<ColumnConfig<AnalysisExecution, ?>>();
        columns.add(sm.getColumn());
        columns.add(expander);
        columns.add(name);
        columns.add(analysisname);
        columns.add(start);
        columns.add(end);
        columns.add(status);

        return new ColumnModel<AnalysisExecution>(columns);
    }

    @Override
    protected void onAfterRenderView() {
        super.onAfterRenderView();
        retrieveData();

    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        if (getStore().size() <= 0) {
            retrieveData();
        }
    }

    private void retrieveData() {
        mask(I18N.DISPLAY.loadingMask());

        AnalysisServiceFacade facade = new AnalysisServiceFacade();

        facade.getAnalyses(UserInfo.getInstance().getWorkspaceId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject JSON = JsonUtil.getObject(result);

                JSONValue val = JSON.get("analyses"); //$NON-NLS-1$
                if (val != null) {
                    JSONArray items = val.isArray();

                    JsArray<JsAnalysisExecution> jsAnalyses = JsonUtil.asArrayOf(items.toString());
                    List<AnalysisExecution> temp = new ArrayList<AnalysisExecution>();

                    for (int i = 0; i < jsAnalyses.length(); i++) {
                        temp.add(new AnalysisExecution(jsAnalyses.get(i)));
                    }

                    ListStore<AnalysisExecution> gstore = getStore();
                    gstore.addAll(temp);
                    sort();
                    getView().refresh(true);
                }

                selectModel(idCurrentSelection);
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.analysesRetrievalFailure(), caught);
                unmask();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void cleanup() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    /**
     * Select a row in the grid
     * 
     */
    public void selectModel(final String idSelection) {
        if (idSelection != null) {
            AnalysisExecution exec = getStore().findModelWithKey(idSelection);

            if (exec != null) {
                getView().ensureVisible(getStore().indexOf(exec), 0, false);
                getSelectionModel().select(exec, false);
            }
        }
    }

    private static class ExpanderCell extends AbstractCell<AnalysisExecution> {

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, AnalysisExecution value,
                SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }


            SafeHtml safeValue = SafeHtmlUtils.fromString(value.getDescription());
            sb.appendHtmlConstant("<p><b>Description:</b>"); //$NON-NLS-1$
            sb.append(safeValue);
            sb.appendHtmlConstant("</p>"); //$NON-NLS-1$
        }

    }

    private static class AppNameCell extends AbstractCell<String> {

        public AppNameCell() {
            super(CLICK);
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, String value,
                SafeHtmlBuilder sb) {

            if (value == null) {
                return;
            }

            SafeHtml safeHl = SafeHtmlUtils
                    .fromTrustedString(new Hyperlink(value, "app_name").getHTML());
            sb.append(safeHl);
        }
    }

    private static class AnalysisNameCell extends AbstractCell<String> {

        public AnalysisNameCell() {
            super(CLICK);
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, String value,
                SafeHtmlBuilder sb) {
            if (value == null) {
                return;
            }

            SafeHtml safeHl = SafeHtmlUtils.fromTrustedString(new Hyperlink(value,
                    "analysis_name").getHTML());
            sb.append(safeHl);
        }
    }
}
