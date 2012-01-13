package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for system payload events.
 * 
 * @author amuir
 * 
 */
public interface SystemPayloadEventHandler extends EventHandler {
    /**
     * Called when an system payload event has fired.
     * 
     * @param event fired event.
     */
    void onFire(SystemPayloadEvent event);
}
