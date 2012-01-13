package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for analysis payload events.
 * 
 * @author amuir
 * 
 */
public interface AnalysisPayloadEventHandler extends EventHandler {
    /**
     * Called when an analysis payload event has fired.
     * 
     * @param event fired event.
     */
    void onFire(AnalysisPayloadEvent event);
}
