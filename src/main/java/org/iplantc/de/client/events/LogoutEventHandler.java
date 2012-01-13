package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for LogoutEvents.
 * 
 * @see org.iplantc.de.client.events.LogoutEvent
 */
public interface LogoutEventHandler extends EventHandler {
    /**
     * Handle when logout is requested.
     * 
     * @param event event to be handled.
     */
    void onLogout(LogoutEvent event);
}
