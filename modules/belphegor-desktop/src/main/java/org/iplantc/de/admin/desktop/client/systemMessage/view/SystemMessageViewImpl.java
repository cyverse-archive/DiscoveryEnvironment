package org.iplantc.de.admin.desktop.client.systemMessage.view;

import org.iplantc.de.admin.desktop.client.systemMessage.SystemMessageView;
import org.iplantc.de.admin.desktop.client.systemMessage.view.cells.SystemMessageNameCell;
import org.iplantc.de.client.models.systemMessages.SystemMessage;
import org.iplantc.de.commons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SystemMessageViewImpl extends Composite implements SystemMessageView, SelectionChangedHandler<SystemMessage> {

    private final class MsgColComparator implements Comparator<SystemMessage> {
        @Override
        public int compare(SystemMessage o1, SystemMessage o2) {
            return o1.getBody().compareToIgnoreCase(o2.getBody());
        }
    }

    interface SystemMessageViewImplUiBinder extends UiBinder<Widget, SystemMessageViewImpl> { }

    @UiField
    TextButton addBtn, deleteBtn;

    @UiField
    Grid<SystemMessage> grid;

    @UiField(provided = true)
    IplantResources res;

    @UiField
    ListStore<SystemMessage> store;

    @UiField(provided = true)
    IplantDisplayStrings strings;

    private static SystemMessageViewImplUiBinder uiBinder = GWT.create(SystemMessageViewImplUiBinder.class);
    private final MessageProperties msgProps;
    private SystemMessageView.Presenter presenter;

    @Inject
    public SystemMessageViewImpl(IplantResources res, IplantDisplayStrings strings,
                                 MessageProperties msgProps) {
        this.res = res;
        this.strings = strings;
        this.msgProps = msgProps;
        initWidget(uiBinder.createAndBindUi(this));

        grid.getSelectionModel().addSelectionChangedHandler(this);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    public void addSystemMessage(SystemMessage systemMessage) {
        store.add(systemMessage);
    }

    @Override
    public void deleteSystemMessage(SystemMessage msgToDelete) {
        store.remove(msgToDelete);
    }

    @Override
    public void editSystemMessage(SystemMessage sysMsgToEdit) {
        final IPlantDialog editSystemMessageDlg = new IPlantDialog();
        editSystemMessageDlg.setHeadingText("Edit System Message");
        editSystemMessageDlg.setHideOnButtonClick(false);
        editSystemMessageDlg.getOkButton().setText("Submit");
        editSystemMessageDlg.setWidth("500");
        editSystemMessageDlg.addCancelButtonSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                editSystemMessageDlg.hide();
            }
        });
        // Call out to service to update, update item in store on success callback.
        final EditCreateSystemMessageDialog editSystemMessagePanel = EditCreateSystemMessageDialog.editSystemMessage(sysMsgToEdit, presenter.getAnnouncementTypes());
        editSystemMessageDlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                final SystemMessage value = editSystemMessagePanel.getValue();
                if (!editSystemMessagePanel.hasErrors()) {
                    presenter.editSystemMessage(value);
                    editSystemMessageDlg.hide();
                }
            }
        });
        editSystemMessageDlg.add(editSystemMessagePanel);
        editSystemMessageDlg.show();
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<SystemMessage> event) {
        boolean isSingleItemSelected = event.getSelection().size() == 1;
        deleteBtn.setEnabled(isSingleItemSelected);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setSystemMessages(List<SystemMessage> systemMessages) {
        store.addAll(systemMessages);
    }

    @Override
    public void updateSystemMessage(SystemMessage updatedSystemMessage) {
        store.update(updatedSystemMessage);
    }

    @UiHandler("addBtn")
    void addButtonClicked(SelectEvent event) {
        final IPlantDialog createSystemMessageDlg = new IPlantDialog();
        createSystemMessageDlg.setHeadingText("Create System Message");
        createSystemMessageDlg.setHideOnButtonClick(false);
        createSystemMessageDlg.getOkButton().setText("Submit");
        createSystemMessageDlg.setWidth("500");
        createSystemMessageDlg.addCancelButtonSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                createSystemMessageDlg.hide();
            }
        });

        final EditCreateSystemMessageDialog createSystemMessagePanel = EditCreateSystemMessageDialog.createSystemMessage(presenter.getAnnouncementTypes());
        createSystemMessageDlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {

                final SystemMessage value = createSystemMessagePanel.getValue();
                if (!createSystemMessagePanel.hasErrors()) {
                    presenter.addSystemMessage(value);
                    createSystemMessageDlg.hide();
                }
            }
        });

        createSystemMessageDlg.add(createSystemMessagePanel);

        createSystemMessageDlg.show();
    }

    @UiFactory
    ColumnModel<SystemMessage> createColumnModel() {
        ColumnConfig<SystemMessage, Date> activationDateCol = new ColumnConfig<>(msgProps.activationTime(), 200, "Activation Date");
        ColumnConfig<SystemMessage, Date> deactivationDateCol = new ColumnConfig<>(msgProps.deactivationTime(), 200, "Deactivation Date");
        ColumnConfig<SystemMessage, SystemMessage> msgCol = new ColumnConfig<>(new IdentityValueProvider<SystemMessage>("body"), 400, "Message");
        ColumnConfig<SystemMessage, String> typeCol = new ColumnConfig<>(msgProps.type(), 90, "Type");
        ColumnConfig<SystemMessage, Boolean> dismissibleColumn = new ColumnConfig<>(msgProps.dismissible(), 90, "Is Dismissible?");
        activationDateCol.setFixed(true);
        deactivationDateCol.setFixed(true);
        msgCol.setCell(new SystemMessageNameCell(this));
        msgCol.setComparator(new MsgColComparator());
        typeCol.setFixed(true);
        typeCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        @SuppressWarnings("unchecked")
        List<ColumnConfig<SystemMessage, ?>> colList = Lists.newArrayList(msgCol, activationDateCol, deactivationDateCol, dismissibleColumn, typeCol);

        return new ColumnModel<>(colList);
    }

    @UiFactory
    ListStore<SystemMessage> createListStore() {
        ListStore<SystemMessage> listStore = new ListStore<>(msgProps.id());
        return listStore;
    }

    @UiHandler("deleteBtn")
    void deleteBtnClicked(SelectEvent event) {
        presenter.deleteSystemMessage(grid.getSelectionModel().getSelectedItem());
    }

}
