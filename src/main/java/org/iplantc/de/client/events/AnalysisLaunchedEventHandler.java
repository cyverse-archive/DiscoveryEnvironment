package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for <code>AnalysisLaunchedEvent</code>s
 * 
 * @see org.iplantc.de.client.events.AnalysisLaunchedEvent
 */
public interface AnalysisLaunchedEventHandler extends EventHandler {
    /**
     * Handle when an analysis has successfully launched.
     * 
     * @param event event to be handled.
     */
    void onLaunch(AnalysisLaunchedEvent event);
}
