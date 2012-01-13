package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * An event to notify that a files description was edited
 * 
 * @author sriram
 * 
 */
public class FileDescriptionEditedEvent extends GwtEvent<FileDescriptionEditedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.FileDescriptionEditedEventHandler
     */
    public static final GwtEvent.Type<FileDescriptionEditedEventHandler> TYPE = new GwtEvent.Type<FileDescriptionEditedEventHandler>();

    private String id;
    private String description;

    /**
     * dispatch the event
     * 
     * @param handler the event handler to dispatch the event
     */
    @Override
    protected void dispatch(FileDescriptionEditedEventHandler handler) {
        handler.onEditComplete(this);
    }

    /**
     * Retrieves the associated event handler type.
     * 
     * @return return the type of the event
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<FileDescriptionEditedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieves an identifier for this instance of the event.
     * 
     * @return return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves a description for this instance of the event.
     * 
     * @return a string containing a description associated with the event.
     */
    public String getDescription() {
        return description;
    }
}