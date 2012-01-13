package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for UserEvents.
 * 
 * @see org.iplantc.de.client.events.UserEvent
 */
public interface UserEventHandler extends EventHandler {
    /**
     * Handle when a user has generated input from the application desktop.
     * 
     * @param event event to be handled.
     */
    void onEvent(UserEvent event);
}
