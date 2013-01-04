/**
 * 
 */
package org.iplantc.de.client.notifications.events;

import java.util.List;

import org.iplantc.de.client.notifications.models.NotificationMessage;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 * 
 */
public class DeleteNotificationsUpdateEvent extends GwtEvent<DeleteNotificationsUpdateEventHandler> {
    private List<NotificationMessage> messages;

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.NotificationCountUpdateEvent
     */
    public static final GwtEvent.Type<DeleteNotificationsUpdateEventHandler> TYPE = new GwtEvent.Type<DeleteNotificationsUpdateEventHandler>();

    public DeleteNotificationsUpdateEvent(List<NotificationMessage> ids) {
        this.setMessages(ids);
    }

    @Override
    public GwtEvent.Type<DeleteNotificationsUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DeleteNotificationsUpdateEventHandler handler) {
        handler.onDelete(this);
    }

    /**
     * @return the ids
     */
    public List<NotificationMessage> getMessages() {
        return messages;
    }

    /**
     * @param ids the ids to set
     */
    public void setMessages(List<NotificationMessage> ids) {
        this.messages = ids;
    }

}
