/**
 * 
 */
package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author sriram
 * 
 */
public interface SettingsUpdatedEventHandler extends EventHandler {

    /**
     * called when user settings are updated
     * 
     * @param event
     */
    void onUpdate(SettingsUpdatedEvent event);

}
