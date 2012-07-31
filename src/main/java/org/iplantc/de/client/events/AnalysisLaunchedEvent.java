package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents an analysis being launched within the system.
 */
public class AnalysisLaunchedEvent extends GwtEvent<AnalysisLaunchedEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.AnalysisLaunchedEventHandler
     */
    public static final GwtEvent.Type<AnalysisLaunchedEventHandler> TYPE = new GwtEvent.Type<AnalysisLaunchedEventHandler>();

    private final String tag;
    private final String name;

    /**
     * Constructs an instance of the event.
     * 
     * @param tag the identifier of window associated with the event
     * @param name the name of the analysis being launched
     */
    public AnalysisLaunchedEvent(final String tag, final String name) {
        this.tag = tag;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(AnalysisLaunchedEventHandler handler) {
        handler.onLaunch(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<AnalysisLaunchedEventHandler> getAssociatedType() {
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
     * Gets the user-supplied name of the analysis being launched.
     * 
     * @return a name identifying the analysis
     */
    public String getName() {
        return name;
    }
}
