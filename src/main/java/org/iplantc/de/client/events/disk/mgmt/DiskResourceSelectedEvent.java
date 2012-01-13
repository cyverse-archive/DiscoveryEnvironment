package org.iplantc.de.client.events.disk.mgmt;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents the selection of a file from a "My Data" window.
 * 
 * The event needs to let handlers know the identifier of the caller, which is their 'tag.' Windows have
 * an associated string that is an identifier (for example, the "My Data" window has a unique identifier,
 * its' tag, of "my_data"). Knowing about the caller will allow the appropriate reaction to the event.
 * 
 * @author lenards
 * 
 */
public class DiskResourceSelectedEvent extends GwtEvent<DiskResourceSelectedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEventHandler
     */
    public static final GwtEvent.Type<DiskResourceSelectedEventHandler> TYPE = new GwtEvent.Type<DiskResourceSelectedEventHandler>();

    private DiskResource resource;
    private String tag;

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Constructs an instance of the event given the necessary argument data.
     * 
     * @param resource the DiskResource object
     * @param tag identifies the caller
     * 
     */
    public DiskResourceSelectedEvent(DiskResource resource, String tag) {
        this.resource = resource;
        this.tag = tag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(DiskResourceSelectedEventHandler handler) {
        handler.onSelected(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<DiskResourceSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public DiskResource getResource() {
        return resource;
    }

}
