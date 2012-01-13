package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for FileEditorWindowClosedEvents.
 * 
 * @see org.iplantc.de.client.events.FileEditorWindowClosedEvent
 */
public interface FileEditorWindowClosedEventHandler extends EventHandler {
    /**
     * Handle when a file editor window has been closed.
     * 
     * @param event event to be handled.
     */
    public void onClosed(FileEditorWindowClosedEvent event);
}
