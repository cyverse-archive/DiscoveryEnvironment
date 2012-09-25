/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.analysis.models.Analysis;
import org.iplantc.de.client.analysis.models.AnalysisExecutionStatus;
import org.iplantc.de.client.analysis.models.AnalysisProperties;
import org.iplantc.de.client.events.AnalysisUpdateEvent;
import org.iplantc.de.client.events.AnalysisUpdateEventHandler;
import org.iplantc.core.uicommons.client.util.DateParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author sriram
 * 
 */
public class AnalysesViewImpl implements AnalysesView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AnalysesView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AnalysesViewImpl> {
    }

    @UiField(provided = true)
    final ListStore<Analysis> listStore;

    @UiField(provided = true)
    final ColumnModel<Analysis> cm;

    @UiField
    GridView gridView;

    @UiField
    Grid<Analysis> grid;

    @UiField
    FramedPanel mainPanel;

    @UiField
    BorderLayoutContainer con;

    @UiField
    BorderLayoutData northData;

    private final Widget widget;

    private Presenter presenter;

    private ArrayList<HandlerRegistration> handlers;

    public AnalysesViewImpl(ListStore<Analysis> listStore, ColumnModel<Analysis> cm,
            GridSelectionModel<Analysis> checkBoxModel, RowExpander<Analysis> expander) {
        this.listStore = listStore;
        this.cm = cm;
        widget = uiBinder.createAndBindUi(this);
        grid.setSelectionModel(checkBoxModel);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Analysis>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
                presenter.onAnalysesSelection(event.getSelection());
            }
        });
        gridView.setEmptyText(I18N.DISPLAY.noAnalyses());
        expander.initPlugin(grid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return widget;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.AnalysesView#setPresenter(org.iplantc.de.client.gxt3.views.
     * AnalysesView.Presenter)
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.gxt3.views.AnalysesView#setNorthWidget(com.google.gwt.user.client.ui.IsWidget
     * )
     */
    @Override
    public void setNorthWidget(IsWidget widget) {
        con.setNorthWidget(widget, northData);
    }

    @Override
    public void loadAnalyses(List<Analysis> items) {
        listStore.clear();
        listStore.addAll(items);
    }

    @Override
    public List<Analysis> getSelectedAnalyses() {
        return grid.getSelectionModel().getSelectedItems();
    }

    @Override
    public void removeFromStore(List<Analysis> items) {
        if (items != null & items.size() > 0) {
            for (Analysis a : items) {
                grid.getStore().remove(a);
            }
        }

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
        AnalysisExecutionStatus enumStatus = AnalysisExecutionStatus.fromTypeString(JsonUtil.getString(
                payload, "status")); //$NON-NLS-1$

        if (listStore.findModelWithKey((JsonUtil.getString(payload, "id"))) != null) { //$NON-NLS-1$ //$NON-NLS-2$
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
            // AnalysisExecution exec = buildAnalysisExecution(payload);
            // getStore().add(exec);
        }

        // sort();
    }

    private void updateExecStatus(String id, String status) {
        Analysis ae = listStore.findModelWithKey(id);

        if (ae != null) {
            ae.setStatus(status);
            listStore.update(ae);
        }
    }

    private void updateEndExecStatus(String id, String status, String resultfolderid, Date enddate) {
        Analysis ae = listStore.findModelWithKey(id); //$NON-NLS-1$

        if (ae != null) {
            ae.setStatus(status);
            if (enddate != null) {
                ae.setEndDate(enddate.getTime());
            }
            ae.setResultFolderId(resultfolderid);
            listStore.update(ae);
        }
    }

    private void updateRunExecStatus(String id, String status, Date startdate) {
        Analysis ae = listStore.findModelWithKey(id); //$NON-NLS-1$

        if (ae != null) {
            ae.setStatus(status);
            if (startdate != null) {
                ae.setStartDate(startdate.getTime());
            }
            listStore.update(ae);
        }
    }
}
