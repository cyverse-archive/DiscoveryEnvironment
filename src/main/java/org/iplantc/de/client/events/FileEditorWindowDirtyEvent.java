package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents a FileEditorWindow with changes made by the user that have not been committed to
 * storage.
 * 
 * @author amuir
 * 
 */
public class FileEditorWindowDirtyEvent extends GwtEvent<FileEditorWindowDirtyEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.FileEditorWindowDirtyEventHandler
     */
    public static final GwtEvent.Type<FileEditorWindowDirtyEventHandler> TYPE = new GwtEvent.Type<FileEditorWindowDirtyEventHandler>();

    private String idFile;
    private boolean dirty;

    /**
     * Instantiate from a file id and dirty flag.
     * 
     * @param idFile id of the file that may be dirty.
     * @param dirty true if this window is in a saveable state (e.g. the user has made changes they may
     *            want to save).
     */
    public FileEditorWindowDirtyEvent(String idFile, boolean dirty) {
        this.idFile = idFile;
        this.dirty = dirty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(FileEditorWindowDirtyEventHandler handler) {
        if (dirty) {
            handler.onDirty(this);
        } else {
            handler.onClean(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<FileEditorWindowDirtyEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the id of the file associated with this event.
     * 
     * @return id of file whose dirty state has changed.
     */
    public String getFileId() {
        return idFile;
    }
}
