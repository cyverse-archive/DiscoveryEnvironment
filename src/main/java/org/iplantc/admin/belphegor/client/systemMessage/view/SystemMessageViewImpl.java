package org.iplantc.admin.belphegor.client.systemMessage.view;

import java.util.Date;
import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.SystemMessageView;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.models.sysmsgs.MessageFactory;
import org.iplantc.core.uicommons.client.models.sysmsgs.MessageProperties;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
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

public class SystemMessageViewImpl extends Composite implements SystemMessageView, SelectionChangedHandler<Message> {

    private static SystemMessageViewImplUiBinder uiBinder = GWT.create(SystemMessageViewImplUiBinder.class);

    interface SystemMessageViewImplUiBinder extends UiBinder<Widget, SystemMessageViewImpl> {}

    @UiField
    TextButton addBtn, deleteBtn, editBtn;

    @UiField
    Grid<Message> grid;

    @UiField
    ListStore<Message> store;

    private final MessageProperties msgProps = GWT.create(MessageProperties.class);
    private final MessageFactory factory = GWT.create(MessageFactory.class);
    private SystemMessageView.Presenter presenter;

    @Inject
    public SystemMessageViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        grid.getSelectionModel().addSelectionChangedHandler(this);
        String jsonOne = "{ "
                + "\"activation_date\": \"Tue Apr 09 2013 15:17:54 GMT-0700 (MST)\", "
                + "\"date_created\": \"Tue Apr 09 2013 22:17:54 GMT-0700 (MST)\", "
                + "\"deactivation_date\": \"Mon Dec 02 2013 12:00:00 GMT-0700 (MST)\", "
                + "\"dismissible\": false, "
                + "\"logins_disabled\": false, "
                + "\"message\": \"This is a warning\", "
                + "\"type\": \"warning\", "
                + "\"uuid\": \"140ee541-9967-47cd-ba2b-3b17d8c19dae\"}";
        String jsonTwo = "{ "
                + "\"activation_date\": \"Tue Apr 09 2013 15:17:54 GMT-0700 (MST)\", "
                + "\"date_created\": \"Tue Apr 09 2013 22:17:54 GMT-0700 (MST)\", "
                + "\"deactivation_date\": \"Mon Dec 02 2013 12:00:00 GMT-0700 (MST)\", "
                + "\"dismissible\": false, "
                + "\"logins_disabled\": false, "
                + "\"message\": \"Another message\", "
                + "\"type\": \"notification\", "
                + "\"uuid\": \"11111111-1111-47cd-ba2b-3b17d8c19dae\"}";
        Message one = AutoBeanCodex.decode(factory, Message.class, jsonOne).as();

        Message two = AutoBeanCodex.decode(factory, Message.class, jsonTwo).as();
        
        store.add(one);
        store.add(two);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Message> event) {
        boolean isSingleItemSelected = event.getSelection().size() == 1;
        deleteBtn.setEnabled(isSingleItemSelected);
        editBtn.setEnabled(isSingleItemSelected);
    }

    @UiFactory
    ListStore<Message> createListStore() {
        ListStore<Message> listStore = new ListStore<Message>(msgProps.id());
        return listStore;
    }

    @UiFactory
    ColumnModel<Message> createColumnModel() {
        ColumnConfig<Message, Date> activationDateCol = new ColumnConfig<Message, Date>(msgProps.activationTime(), 90, "Activation Date");
        ColumnConfig<Message, Date> deactivationDateCol = new ColumnConfig<Message, Date>(msgProps.deactivationTime(), 90, "Deactivation Date");
        ColumnConfig<Message, String> msgCol = new ColumnConfig<Message, String>(msgProps.body(), 90, "Message");
        ColumnConfig<Message, String> typeCol = new ColumnConfig<Message, String>(msgProps.type(), 90, "Type");
        
        @SuppressWarnings("unchecked")
        List<ColumnConfig<Message, ?>> colList = Lists.<ColumnConfig<Message, ?>> newArrayList(activationDateCol, deactivationDateCol, msgCol, typeCol);
        
        return new ColumnModel<Message>(colList);
    }

    @UiHandler("addBtn")
    void addButtonClicked(SelectEvent event) {
        final EditCreateSystemMessageDialog createSystemMessage = EditCreateSystemMessageDialog.createSystemMessage();
        createSystemMessage.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                Message msg = createSystemMessage.getMessage();
                presenter.addSystemMessage(msg);
//                store.add(msg);
            }
        });
        
        createSystemMessage.show();
    }

    @UiHandler("deleteBtn")
    void deleteBtnClicked(SelectEvent event) {
        presenter.deleteSystemMessage(grid.getSelectionModel().getSelectedItem());
//        store.remove(grid.getSelectionModel().getSelectedItem());
    }

    @UiHandler("editBtn")
    void editBtnClicked(SelectEvent event) {
        // Call out to service to update, update item in store on success callback.
        final EditCreateSystemMessageDialog editSystemMessage = EditCreateSystemMessageDialog.editSystemMessage(grid.getSelectionModel().getSelectedItem());
        editSystemMessage.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                Message msg = editSystemMessage.getMessage();
                presenter.editSystemMessage(msg);
            }
        });
        editSystemMessage.show();
    }

}
