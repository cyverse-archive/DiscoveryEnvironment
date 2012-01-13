package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler for FileDescriptionEditedEvent event
 * 
 * @author sriram
 * 
 */
public interface FileDescriptionEditedEventHandler extends EventHandler {
    /**
     * Does post processing after the editing has been completed.
     * 
     * @param event a FileDescriptionEditedEvent event
     */
    public void onEditComplete(FileDescriptionEditedEvent event);
}
