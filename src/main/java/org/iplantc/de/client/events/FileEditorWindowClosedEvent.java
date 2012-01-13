package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event representation a FileEditorWindow being closed by the user.
 * 
 * @author amuir
 * 
 */
public class FileEditorWindowClosedEvent extends GwtEvent<FileEditorWindowClosedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.FileEditorWindowClosedEventHandler
     */
    public static final GwtEvent.Type<FileEditorWindowClosedEventHandler> TYPE = new GwtEvent.Type<FileEditorWindowClosedEventHandler>();

    private String id;

    /**
     * Instantiate from id.
     * 
     * @param id id of edited file.
     */
    public FileEditorWindowClosedEvent(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(FileEditorWindowClosedEventHandler handler) {
        handler.onClosed(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<FileEditorWindowClosedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the id of the closed file.
     * 
     * @return file id of closed window.
     */
    public String getId() {
        return id;
    }
}
