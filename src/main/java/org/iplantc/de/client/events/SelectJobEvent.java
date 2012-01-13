package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents a user selecting a job.
 */
public class SelectJobEvent extends GwtEvent<SelectJobEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.SelectJobEventHandler
     */
    public static final GwtEvent.Type<SelectJobEventHandler> TYPE = new GwtEvent.Type<SelectJobEventHandler>();

    private String idJob;



    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(SelectJobEventHandler handler) {
        handler.onSelect(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GwtEvent.Type<SelectJobEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the id of the selected job.
     * 
     * @return id of selected job.
     */
    public String getJobId() {
        return idJob;
    }
}
