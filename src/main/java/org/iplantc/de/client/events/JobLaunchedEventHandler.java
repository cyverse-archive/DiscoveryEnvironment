package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for JobLaunchedEvents
 * 
 * @see org.iplantc.de.client.events.JobLaunchedEvent
 */
public interface JobLaunchedEventHandler extends EventHandler {
    /**
     * Handle when a job has successfully launched.
     * 
     * @param event event to be handled.
     */
    void onLaunch(JobLaunchedEvent event);
}
