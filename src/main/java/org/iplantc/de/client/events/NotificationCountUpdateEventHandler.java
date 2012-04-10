/**
 * 
 */
package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author sriram
 * 
 */
public interface NotificationCountUpdateEventHandler extends EventHandler {

    /**
     * Handler when notification count changes
     */
    public void onCountUpdate(NotificationCountUpdateEvent ncue);

}
