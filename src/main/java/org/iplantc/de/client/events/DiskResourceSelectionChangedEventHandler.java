package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Event handler for when a user changes disk resource selection.
 * 
 * @author amuir
 * 
 */
public interface DiskResourceSelectionChangedEventHandler extends EventHandler {
    /**
     * Fired when a user changes disk resource selection in the data grid.
     * 
     * @param event event to be handled.
     */
    void onChange(DiskResourceSelectionChangedEvent event);
}
