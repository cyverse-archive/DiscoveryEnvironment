package org.iplantc.de.client.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.util.CommonStoreSorter;
import org.iplantc.core.uicommons.client.util.DateParser;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.AnalysisPayloadEventHandler;
import org.iplantc.de.client.events.UserEvent;
import org.iplantc.de.client.models.AnalysisExecution;
import org.iplantc.de.client.models.JsAnalysisExecution;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.views.panels.MyAnalysesPanel;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.tips.QuickTip;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A grid that is used to display users Analyses
 * 
 * @author sriram
 * 
 */
public class MyAnalysesGrid extends Grid<AnalysisExecution> {

    private String idCurrentSelection;
    private ArrayList<HandlerRegistration> handlers;
    private static RowExpander expander;
    private static XTemplate tpl;

    /**
     * Create a new MyAnalysesGrid
     * 
     * @param store store to be used by the grid
     * @param cm column model describing the columns in the grid
     */
    public MyAnalysesGrid(ListStore<AnalysisExecution> store, ColumnModel cm) {
        super(store, cm);
        registerHandlers();
    }

    /**
     * Set the id of our current selection.
     * 
     * @param idCurrentSelection id of currently selected job.
     */
    public void setCurrentSelection(final String idCurrentSelection) {
        this.idCurrentSelection = idCurrentSelection;
    }

    private void registerHandlers() {
        handlers = new ArrayList<HandlerRegistration>();
        EventBus eventbus = EventBus.getInstance();
        handlers.add(eventbus.addHandler(AnalysisPayloadEvent.TYPE, new AnalysisPayloadEventHandler() {
            @Override
            public void onFire(AnalysisPayloadEvent event) {
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

        if (getStore().findModel("id", JsonUtil.getString(payload, "id")) != null) { //$NON-NLS-1$ //$NON-NLS-2$
            switch (enumStatus) {
                case COMPLETED:
                    updateEndExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            JsonUtil.getString(payload, "resultfolderid"), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getString(payload, "enddate"))); //$NON-NLS-1$
                    break;

                case FAILED:
                    updateEndExecStatus(JsonUtil.getString(payload, "id"), enumStatus.toString(), //$NON-NLS-1$
                            JsonUtil.getString(payload, "resultfolderid"), //$NON-NLS-1$
                            DateParser.parseDate(JsonUtil.getString(payload, "enddate"))); //$NON-NLS-1$
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
            getSelectionModel().select(exec, false);
        }

        sort();
    }

    private void sort() {
        getStore().sort("startdate", SortDir.DESC); //$NON-NLS-1$
    }

    private void updateExecStatus(String id, String status) {
        AnalysisExecution ae = getStore().findModel("id", id); //$NON-NLS-1$

        if (ae != null) {
            ae.setStatus(status);
            getStore().update(ae);
        }
    }

    private static XTemplate initExpander() {

        String tmpl = "<p><b>Description:</b>{description}</p>"; //$NON-NLS-1$

        return XTemplate.create(tmpl);
    }

    private void updateEndExecStatus(String id, String status, String resultfolderid, Date enddate) {
        AnalysisExecution ae = getStore().findModel("id", id); //$NON-NLS-1$

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
        AnalysisExecution ae = getStore().findModel("id", id); //$NON-NLS-1$

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

    @SuppressWarnings("unchecked")
    private static MyAnalysesGrid createInstanceImpl(CheckBoxSelectionModel<AnalysisExecution> sm) {
        ListStore<AnalysisExecution> gstore = new ListStore<AnalysisExecution>();
        gstore.setStoreSorter(new CommonStoreSorter());
        final ColumnModel colModel = buildColumnModel(sm);
        MyAnalysesGrid grid = new MyAnalysesGrid(gstore, colModel);
        grid.setSelectionModel(sm);
        grid.addPlugin(sm);
        grid.addPlugin(expander);
        grid.setAutoExpandMax(2048);
        grid.getView().setForceFit(true);
        grid.setToolTip(I18N.DISPLAY.resultsGridToolTip());
        new QuickTip(grid);

        return grid;
    }

    private static ColumnModel buildColumnModel(CheckBoxSelectionModel<AnalysisExecution> sm) {

        tpl = initExpander();
        expander = new RowExpander(tpl);
        sm.getColumn().setMenuDisabled(true);
        expander.setMenuDisabled(true);

        ColumnConfig name = new ColumnConfig("name", I18N.DISPLAY.name(), 175); //$NON-NLS-1$
        ColumnConfig analysisname = new ColumnConfig("analysis_name", I18N.DISPLAY.appName(), 250); //$NON-NLS-1$
        ColumnConfig start = new ColumnConfig("startdate", I18N.DISPLAY.startDate(), 150); //$NON-NLS-1$
        ColumnConfig end = new ColumnConfig("enddate", I18N.DISPLAY.endDate(), 150); //$NON-NLS-1$
        ColumnConfig status = new ColumnConfig("status", I18N.DISPLAY.status(), 100); //$NON-NLS-1$

        DateTimeFormat format = DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        start.setDateTimeFormat(format);
        end.setDateTimeFormat(format);
        analysisname.setRenderer(new AppNameCellRenderer());

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.addAll(Arrays.asList(sm.getColumn(), expander, name, analysisname, start, end, status));

        return new ColumnModel(columns);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        retrieveData();
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
                    gstore.add(temp);
                    sort();
                }

                selectModel(idCurrentSelection);
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.resultsRetrievalFailure(), caught);
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
            AnalysisExecution exec = getStore().findModel("id", idSelection); //$NON-NLS-1$

            if (exec != null) {
                getView().ensureVisible(getStore().indexOf(exec), 0, false);
                getSelectionModel().select(exec, false);
            }
        }
    }
}

class AppNameCellRenderer implements GridCellRenderer<AnalysisExecution> {
    @Override
    public Object render(final AnalysisExecution model, String property, ColumnData config,
            int rowIndex, int colIndex, ListStore<AnalysisExecution> store, Grid<AnalysisExecution> grid) {
        Hyperlink link = new Hyperlink(model.getAnalysisName(), "analysis_name"); //$NON-NLS-1$
        link.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                EventBus bus = EventBus.getInstance();
                UserEvent e = new UserEvent(Constants.CLIENT.windowTag(), model.getAnalysisId());
                bus.fireEvent(e);
            }
        });

        return link;
    }
}