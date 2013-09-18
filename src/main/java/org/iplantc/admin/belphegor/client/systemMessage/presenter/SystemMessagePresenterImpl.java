package org.iplantc.admin.belphegor.client.systemMessage.presenter;

import java.util.Collections;
import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.SystemMessageView;
import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class SystemMessagePresenterImpl implements SystemMessageView.Presenter {

    private final SystemMessageServiceFacade sysMsgService;
    private final SystemMessageView view;
    private final IplantDisplayStrings strings;
    private List<String> systemMessageTypes = Collections.emptyList();

    @Inject
    public SystemMessagePresenterImpl(SystemMessageView view, SystemMessageServiceFacade sysMsgService, IplantDisplayStrings strings) {
        this.view = view;
        this.sysMsgService = sysMsgService;
        this.strings = strings;
        view.setPresenter(this);

        // Fetch all system messages
        sysMsgService.getSystemMessageTypes(new AsyncCallback<List<String>>() {

            @Override
            public void onSuccess(List<String> result) {
                if ((result != null) && !result.isEmpty()) {
                    systemMessageTypes.addAll(result);
                } else {
                    systemMessageTypes = Collections.emptyList();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }


    @Override
    public void go(HasOneWidget container) {
        view.mask(strings.loadingMask());
        container.setWidget(view);
        sysMsgService.getSystemMessages(new AsyncCallback<List<Message>>() {
    
            @Override
            public void onSuccess(List<Message> result) {
                view.unmask();
                view.setSystemMessages(result);
            }
    
            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });
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
    public List<String> getAnnouncementTypes() {
        return systemMessageTypes;
    }
}
