package org.iplantc.de.admin.desktop.client.toolAdmin.view;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import java.util.List;

/**
 * Created by aramsey on 10/27/15.
 */



public class ToolAdminViewImpl extends Composite implements ToolAdminView, SelectionChangedEvent.SelectionChangedHandler<Tool> {

    private static ToolAdminViewImplUiBinder uiBinder = GWT.create(ToolAdminViewImplUiBinder.class);

    interface ToolAdminViewImplUiBinder extends UiBinder<Widget, ToolAdminViewImpl>{

    }

    @UiField
    TextButton addButton;
    @UiField
    Grid<Tool> grid;
    @UiField
    ListStore<Tool> listStore;
    @UiField(provided = true) ToolAdminViewAppearance appearance;

    private final ToolProperties toolProps;
    private ToolAdminView.Presenter presenter;

    @Inject
    public ToolAdminViewImpl (final ToolAdminViewAppearance appearance, ToolProperties toolProps){
        this.appearance = appearance;
        this.toolProps = toolProps;
        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().addSelectionChangedHandler(this);
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
    }

    //Sencha required method that has to exist (see below snippet)
    /**
     * Getter for listStore called 1 times. Type: IMPORTED. Build precedence: 1.

    private com.sencha.gxt.data.shared.ListStore get_listStore() {
        return build_listStore();
    }
    private com.sencha.gxt.data.shared.ListStore build_listStore() {
        // Creation section.
        final com.sencha.gxt.data.shared.ListStore listStore = owner.createListStore();
        // Setup section.

        this.owner.listStore = listStore;

        return listStore;
    }*/

    @UiFactory
    ListStore<Tool> createListStore(){
        final ListStore<Tool> listStore = new ListStore<>(toolProps.id());
        listStore.setEnableFilters(true);
        return listStore;
    }

    //Sencha required method that has to exist (see above snippet as example)
    @UiFactory
    ColumnModel<Tool> createColumnModel() {
        List<ColumnConfig<Tool, ?>> list = Lists.newArrayList();
        ColumnConfig<Tool, String> nameCol = new ColumnConfig<>(toolProps.name(),
                                                                appearance.nameColumnWidth(),
                                                                appearance.nameColumnLabel());
        ColumnConfig<Tool, String> descriptionCol = new ColumnConfig<>(toolProps.description(), appearance.descriptionColumnWidth(), appearance.descriptionColumnLabel());
        ColumnConfig<Tool, String> locationCol = new ColumnConfig<>(toolProps.location(), appearance.locationColumnInfoWidth(), appearance.locationColumnInfoLabel());
        ColumnConfig<Tool, String> typeCol = new ColumnConfig<>(toolProps.type(), appearance.typeColumnInfoWidth(), appearance.typeColumnInfoLabel());
        ColumnConfig<Tool, String> attributionCol = new ColumnConfig<>(toolProps.attribution(), appearance.attributionColumnWidth(), appearance.attributionColumnLabel());
        ColumnConfig<Tool, String> versionCol = new ColumnConfig<>(toolProps.version(), appearance.versionColumnInfoWidth(), appearance.versionColumnInfoLabel());

        list.add(nameCol);
        list.add(descriptionCol);
        list.add(locationCol);
        list.add(typeCol);
        list.add(attributionCol);
        list.add(versionCol);
        return new ColumnModel<>(list);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setToolList(List<Tool> tools) {
        listStore.addAll(tools);
    }

    @Override
    public void setToolDetails(ToolContainer toolContainer) {
        final IPlantDialog dialogWindow = new IPlantDialog();
        dialogWindow.setHideOnButtonClick(false);
        dialogWindow.setHeadingText(appearance.dialogWindowName());
        dialogWindow.getOkButton().setText(appearance.dialogWindowUpdateBtnText());
        dialogWindow.getOkButton().disable();
        dialogWindow.addCancelButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                dialogWindow.hide();
            }
        });
        FlowLayoutContainer container = new FlowLayoutContainer();
        container.getScrollSupport().setScrollMode(ScrollSupport.ScrollMode.AUTO);
        ToolAdminDetailsWindow detailsPanel = ToolAdminDetailsWindow.addToolDetails();
        detailsPanel.edit(toolContainer);
        container.add(detailsPanel);
        dialogWindow.add(container);
        dialogWindow.setPixelSize(500, 500);
        dialogWindow.show();
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Tool> event) {
        presenter.getToolDetails(event.getSelection().get(0));
    }

    @UiHandler("addButton")
    void addButtonClicked (SelectEvent event){
        final IPlantDialog dialogWindow = new IPlantDialog();
        dialogWindow.setHideOnButtonClick(false);
        dialogWindow.setHeadingText(appearance.dialogWindowName());
        dialogWindow.getOkButton().setText(appearance.dialogWindowUpdateBtnText());
        dialogWindow.getOkButton().disable();
        dialogWindow.addCancelButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                dialogWindow.hide();
            }
        });
        FlowLayoutContainer container = new FlowLayoutContainer();
        container.getScrollSupport().setScrollMode(ScrollSupport.ScrollMode.AUTO);
        ToolAdminDetailsWindow detailsPanel = ToolAdminDetailsWindow.addToolDetails();
        container.add(detailsPanel);
        dialogWindow.add(container);
        dialogWindow.setPixelSize(500, 500);
        dialogWindow.show();
    }

}
