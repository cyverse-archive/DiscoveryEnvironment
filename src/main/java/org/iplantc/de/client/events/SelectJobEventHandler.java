package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for SelectJobEvents.
 * 
 * @see org.iplantc.de.client.events.SelectJobEvent
 */
public interface SelectJobEventHandler extends EventHandler {
    /**
     * Handle when the user selects a job.
     * 
     * @param event event to be handled.
     */
    void onSelect(SelectJobEvent event);
}
