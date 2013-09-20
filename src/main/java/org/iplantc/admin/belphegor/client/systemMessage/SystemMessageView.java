package org.iplantc.admin.belphegor.client.systemMessage;

import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.views.IsMaskable;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface SystemMessageView extends IsWidget, IsMaskable {

    public interface Presenter {

        /**
         * Adds a system message by calling the {@link SystemMessageServiceFacade#addSystemMessage}
         * endpoint.
         * 
         * Upon success, the view will be updated with the resulting System message.
         * 
         * @param msg
         */
        void addSystemMessage(Message msg);

        /**
         * Submits the given systems message to be updated by calling the
         * {@link SystemMessageServiceFacade#updateSystemMessage} endpoint.
         * 
         * Upon success, the view will be updated appropriately.
         * 
         * @param msg
         */
        void editSystemMessage(Message msg);

        /**
         * Submits the given system message to be deleted by calling the
         * {@link SystemMessageServiceFacade#deleteSystemMessage} endpoint.
         * 
         * Upon success, the message will be removed from the view.
         * 
         * @param msg
         */
        void deleteSystemMessage(Message msg);

        void go(HasOneWidget container);

        List<String> getAnnouncementTypes();

    }

    void setPresenter(Presenter presenter);

    void setSystemMessages(List<Message> systemMessages);

    void addSystemMessage(Message systemMessage);

    void updateSystemMessage(Message updatedSystemMessage);

    void deleteSystemMessage(Message msgToDelete);

}
