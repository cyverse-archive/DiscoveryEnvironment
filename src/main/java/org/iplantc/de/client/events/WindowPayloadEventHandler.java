package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Called when an window payload event has fired.
 * 
 * @author amuir
 * 
 */
public interface WindowPayloadEventHandler extends EventHandler {
    /**
     * Called when a window event is fired.
     * 
     * @param event fired event.
     */
    void onFire(WindowPayloadEvent event);
}
