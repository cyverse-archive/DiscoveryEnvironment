package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;

/**
 * System payload event.
 * 
 * @author amuir
 * 
 */
public class SystemPayloadEvent extends MessagePayloadEvent<SystemPayloadEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.SystemPayloadEventHandler
     */
    public static final GwtEvent.Type<SystemPayloadEventHandler> TYPE = new GwtEvent.Type<SystemPayloadEventHandler>();

    /**
     * Instantiate from a message and payload.
     * 
     * @param message message to be displayed as a notification.
     * @param payload additional data needed to react to this event.
     */
    public SystemPayloadEvent(JSONObject message, JSONObject payload) {
        super(message, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(SystemPayloadEventHandler handler) {
        handler.onFire(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SystemPayloadEventHandler> getAssociatedType() {
        // TODO Auto-generated method stub
        return TYPE;
    }
}
