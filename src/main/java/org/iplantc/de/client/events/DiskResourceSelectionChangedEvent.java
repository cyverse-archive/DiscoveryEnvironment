package org.iplantc.de.client.events;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.event.shared.GwtEvent;

public class DiskResourceSelectionChangedEvent extends
        GwtEvent<DiskResourceSelectionChangedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.DiskResourceSelectionChangedEventHandler
     */
    public static final GwtEvent.Type<DiskResourceSelectionChangedEventHandler> TYPE = new GwtEvent.Type<DiskResourceSelectionChangedEventHandler>();

    private final String tag;
    private final List<DiskResource> selected;

    /**
     * Instantiated from a tag and a list of selected disk resources.
     * 
     * @param tag tag associated with the firing window.
     * @param selected list of selected disk resources.
     */
    public DiskResourceSelectionChangedEvent(final String tag, final List<DiskResource> selected) {
        this.tag = tag;
        this.selected = selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(DiskResourceSelectionChangedEventHandler handler) {
        handler.onChange(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<DiskResourceSelectionChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the tag associated with the window containing the selected disk resources.
     * 
     * @return associated tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Retrieve the selected disk resources.
     * 
     * @return selected disk resources.
     */
    public List<DiskResource> getSelected() {
        return selected;
    }
}
