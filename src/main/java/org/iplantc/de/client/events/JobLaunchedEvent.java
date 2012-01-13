package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents a job being launched within the system.
 */
public class JobLaunchedEvent extends GwtEvent<JobLaunchedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.JobLaunchedEventHandler
     */
    public static final GwtEvent.Type<JobLaunchedEventHandler> TYPE = new GwtEvent.Type<JobLaunchedEventHandler>();

    private String tag;
    private String name;

    /**
     * Constructs an instance of the event.
     * 
     * @param tag the identifier of window associated with the event
     * @param name the name of the job being launched
     */
    public JobLaunchedEvent(final String tag, final String name) {
        this.tag = tag;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(JobLaunchedEventHandler handler) {
        handler.onLaunch(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<JobLaunchedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Gets the identifier of the window associated with the event.
     * 
     * A tag is similar to a window handle. It is a unique identifier within the desktop view.
     * 
     * @return the string representation of the associated window's identifier
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the user-supplied name of the job being launched.
     * 
     * @return a name identifying the job
     */
    public String getName() {
        return name;
    }
}
