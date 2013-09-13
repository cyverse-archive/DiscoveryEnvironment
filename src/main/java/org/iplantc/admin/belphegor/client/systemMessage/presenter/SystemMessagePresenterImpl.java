package org.iplantc.admin.belphegor.client.systemMessage.presenter;

import org.iplantc.admin.belphegor.client.systemMessage.SystemMessageView;
import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.ListStore;

public class SystemMessagePresenterImpl implements SystemMessageView.Presenter {

    private ListStore<Message> store;
    private final SystemMessageServiceFacade sysMsgService;

    @Inject
    public SystemMessagePresenterImpl(SystemMessageView view, SystemMessageServiceFacade sysMsgService) {
        view.setPresenter(this);
        this.sysMsgService = sysMsgService;
        /*
         * TODO Fetch all system messages
         */

    }


    @Override
    public void addSystemMessage(Message msg) {
        /*
         * TODO Call service to create message.
         * Upon success, add message to view's store
         */

    }

    @Override
    public void editSystemMessage(Message msg) {
        /*
         * TODO Call service to edit message
         * Upon success, update message in view, and notify user of success.
         */

    }

    @Override
    public void deleteSystemMessage(Message msg) {
        /*
         * TODO Call service to delete message
         * Upon success, remove message from view.
         */

    }

    @Override
    public void go(HasOneWidget container) {
        // TODO Auto-generated method stub

    }
}
