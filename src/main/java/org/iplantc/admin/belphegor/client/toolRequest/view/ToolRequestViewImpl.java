package org.iplantc.admin.belphegor.client.toolRequest.view;

import java.util.Date;
import java.util.List;

import org.iplantc.admin.belphegor.client.toolRequest.ToolRequestView;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestProperties;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestStatus;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class ToolRequestViewImpl extends Composite implements ToolRequestView, SelectionChangedHandler<ToolRequest> {

    private static ToolRequestViewImplUiBinder uiBinder = GWT.create(ToolRequestViewImplUiBinder.class);

    interface ToolRequestViewImplUiBinder extends UiBinder<Widget, ToolRequestViewImpl> {
    }

    @UiField
    TextButton updateBtn;

    @UiField
    Grid<ToolRequest> grid;

    @UiField
    ListStore<ToolRequest> store;

    @UiField
    ToolRequestDetailsPanel detailsPanel;

    private final ToolRequestProperties trProps = GWT.create(ToolRequestProperties.class);
    private final ToolRequestAutoBeanFactory factory = GWT.create(ToolRequestAutoBeanFactory.class);
    private ToolRequestView.Presenter presenter;

    @Inject
    public ToolRequestViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().addSelectionChangedHandler(this);
        List<ToolRequest> tools = Lists.newArrayList();
        ToolRequest tr = factory.toolRequest().as();
        tr.setName("Tool One");
        tr.setId("1");
        tr.setStatus(ToolRequestStatus.Submitted);
        tools.add(tr);

        tr = factory.toolRequest().as();
        tr.setName("Tool Two");
        tr.setId("2");
        tr.setStatus(ToolRequestStatus.Evaluation);
        tools.add(tr);

        store.addAll(tools);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiFactory
    ListStore<ToolRequest> createListStore() {
        ListStore<ToolRequest> listStore = new ListStore<ToolRequest>(trProps.id());
        return listStore;
    }

    @UiFactory
    ColumnModel<ToolRequest> createColumnModel() {
        List<ColumnConfig<ToolRequest, ?>> list = Lists.newArrayList();
        ColumnConfig<ToolRequest, String> nameCol = new ColumnConfig<ToolRequest, String>(trProps.name(), 90, "Name");
        ColumnConfig<ToolRequest, ToolRequestStatus> statusCol = new ColumnConfig<ToolRequest, ToolRequestStatus>(trProps.status(), 90, "Status");
        ColumnConfig<ToolRequest, Date> dateSubmittedCol = new ColumnConfig<ToolRequest, Date>(trProps.dateSubmitted(), 90, "Date Submitted");
        ColumnConfig<ToolRequest, Date> dateUpdatedCol = new ColumnConfig<ToolRequest, Date>(trProps.dateUpdated(), 90, "Date Updated");
        ColumnConfig<ToolRequest, String> updatedByCol = new ColumnConfig<ToolRequest, String>(trProps.updatedBy(), 90, "Updated By");
        ColumnConfig<ToolRequest, String> versionCol = new ColumnConfig<ToolRequest, String>(trProps.version(), 90, "Version");

        list.add(nameCol);
        list.add(statusCol);
        list.add(dateSubmittedCol);
        list.add(dateUpdatedCol);
        list.add(updatedByCol);
        list.add(versionCol);
        return new ColumnModel<ToolRequest>(list);
    }

    @UiHandler("updateBtn")
    void onUpdateBtnClicked(SelectEvent event) {
        final UpdateToolRequestDialog updateToolRequestDialog = new UpdateToolRequestDialog(grid.getSelectionModel().getSelectedItem());
        updateToolRequestDialog.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ToolRequestUpdate tru = updateToolRequestDialog.getToolRequestUpdate();
                presenter.updateToolRequest(tru);
            }
        });
        updateToolRequestDialog.show();
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<ToolRequest> event) {
        boolean isSingleItemSelected = event.getSelection().size() == 1;
        updateBtn.setEnabled(isSingleItemSelected);
        if (isSingleItemSelected) {
            presenter.fetchToolRequestDetails();
        }

    }

    @Override
    public void setToolRequests(List<ToolRequest> toolRequests) {
        store.addAll(toolRequests);
    }

}