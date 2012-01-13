package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;

/**
 * Window payload event.
 * 
 * @author amuir
 * 
 */
public class WindowPayloadEvent extends MessagePayloadEvent<WindowPayloadEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.AnalysisPayloadEventHandler
     */
    public static final GwtEvent.Type<WindowPayloadEventHandler> TYPE = new GwtEvent.Type<WindowPayloadEventHandler>();

    /**
     * {@inheritDoc}
     */
    public WindowPayloadEvent(JSONObject message, JSONObject payload) {
        super(message, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(WindowPayloadEventHandler handler) {
        handler.onFire(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<WindowPayloadEventHandler> getAssociatedType() {
        return TYPE;
    }
}
