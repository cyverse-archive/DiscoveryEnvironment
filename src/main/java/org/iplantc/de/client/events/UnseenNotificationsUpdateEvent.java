/**
 * 
 */
package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 * 
 */
public class UnseenNotificationsUpdateEvent extends GwtEvent<UnseenNotificationsUpdateEventHandler> {

    // # messages to fetch
    private int count;

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.NotificationCountUpdateEvent
     */
    public static final GwtEvent.Type<UnseenNotificationsUpdateEventHandler> TYPE = new GwtEvent.Type<UnseenNotificationsUpdateEventHandler>();

    public UnseenNotificationsUpdateEvent(int count) {
        this.setCount(count);
    }

    @Override
    public GwtEvent.Type<UnseenNotificationsUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UnseenNotificationsUpdateEventHandler handler) {
        handler.update(this);
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

}
