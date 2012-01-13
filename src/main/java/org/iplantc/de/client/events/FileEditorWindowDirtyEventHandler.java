package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Defines a handler for FileEditorWindowDirtyEvents
 * 
 * @see org.iplantc.de.client.events.FileEditorWindowDirtyEvent
 */
public interface FileEditorWindowDirtyEventHandler extends EventHandler {
    /**
     * Handle when a file editor window is no longer in a saveable state.
     * 
     * @param event event to be handled.
     */
    void onClean(FileEditorWindowDirtyEvent event);

    /**
     * Handle when a file editor window is in a saveable state.
     * 
     * @param event event to be handled.
     */
    void onDirty(FileEditorWindowDirtyEvent event);
}
