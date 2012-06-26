package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that notifies unseen notification count has changed
 * 
 * @author sriram
 * 
 */
public class NotificationCountUpdateEvent extends GwtEvent<NotificationCountUpdateEventHandler> {

    private int total;

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.NotificationCountUpdateEvent
     */
    public static final GwtEvent.Type<NotificationCountUpdateEventHandler> TYPE = new GwtEvent.Type<NotificationCountUpdateEventHandler>();

    public NotificationCountUpdateEvent(int total) {
        this.total = total;
    }

    /**
     * @return the dataCount
     */
    public int getTotal() {
        return total;
    }

    @Override
    public Type<NotificationCountUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NotificationCountUpdateEventHandler arg0) {
        arg0.onCountUpdate(this);
    }

}
