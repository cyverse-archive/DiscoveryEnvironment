package org.iplantc.admin.belphegor.client.systemMessage.service;

import java.util.List;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemMessageServiceFacade {

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/NotificationAgent#admin---listing-system-notifications"
     * >Notification Agent Doc</a>
     * 
     * @param callback
     */
    void getSystemMessages(AsyncCallback<List<Message>> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/NotificationAgent#admin---adding-a-system-notification"
     * >Notification Agent Doc</a>
     * 
     * @param msgToAdd
     * @param callback
     */
    void addSystemMessage(Message msgToAdd, AsyncCallback<Message> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/NotificationAgent#admin---updating-a-system-notification"
     * >Notification Agent Doc</a>
     * 
     * @param updatedMsg
     * @param callback
     */
    void updateSystemMessage(Message updatedMsg, AsyncCallback<Message> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/NotificationAgent#admin---deleting-a-system-notification-by-uuid"
     * >Notification Agent Doc</a>
     * 
     * @param msgToDelete
     * @param callback
     */
    void deleteSystemMessage(Message msgToDelete, AsyncCallback<Void> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/NotificationAgent#admin---getting-all-system-notification-types"
     * >Notification Agent Doc</a>
     * 
     * @param callback
     */
    void getSystemMessageTypes(AsyncCallback<List<String>> callback);

}
