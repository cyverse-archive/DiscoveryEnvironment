package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler for UserPreferenceCloseEvent
 * 
 * @author sriram
 * 
 */
public interface UserPreferenceCloseEventHandler extends EventHandler {
    /**
     * Called back when cancel button is clicked
     * 
     * @param upcc UserPreferenceCloseEvent event
     */
    public void onClose(UserPreferenceCloseEvent upcc);
}
