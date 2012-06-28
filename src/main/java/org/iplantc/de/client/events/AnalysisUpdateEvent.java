/**
 * 
 */
package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;

/**
 * An envent that is fired to update info in analyses window
 * 
 * @author sriram
 * 
 */
public class AnalysisUpdateEvent extends GwtEvent<AnalysisUpdateEventHandler> {

    public static final GwtEvent.Type<AnalysisUpdateEventHandler> TYPE = new GwtEvent.Type<AnalysisUpdateEventHandler>();

    private JSONObject payload;

    public AnalysisUpdateEvent(JSONObject payload) {
        this.payload = payload;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AnalysisUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisUpdateEventHandler handler) {
        handler.onUpdate(this);

    }

    /**
     * @return the payload
     */
    public JSONObject getPayload() {
        return payload;
    }

}
