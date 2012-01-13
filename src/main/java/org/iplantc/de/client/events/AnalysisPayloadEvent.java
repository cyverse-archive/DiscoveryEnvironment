package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;

/**
 * Analysis payload event.
 * 
 * @author amuir
 * 
 */
public class AnalysisPayloadEvent extends MessagePayloadEvent<AnalysisPayloadEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.AnalysisPayloadEventHandler
     */
    public static final GwtEvent.Type<AnalysisPayloadEventHandler> TYPE = new GwtEvent.Type<AnalysisPayloadEventHandler>();

    /**
     * Instant
     * 
     * @param message
     * @param payload
     */
    public AnalysisPayloadEvent(JSONObject message, JSONObject payload) {
        super(message, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(AnalysisPayloadEventHandler handler) {
        handler.onFire(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AnalysisPayloadEventHandler> getAssociatedType() {
        return TYPE;
    }
}
