package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;

public class DataPayloadEvent extends MessagePayloadEvent<DataPayloadEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.AnalysisPayloadEventHandler
     */
    public static final GwtEvent.Type<DataPayloadEventHandler> TYPE = new GwtEvent.Type<DataPayloadEventHandler>();

    public DataPayloadEvent(JSONObject message, JSONObject payload) {
        super(message, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(DataPayloadEventHandler handler) {
        handler.onFire(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DataPayloadEventHandler> getAssociatedType() {
        return TYPE;
    }
}
