package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event generated when the user preference dialog is closed.
 * 
 * @author sriram
 * 
 */
public class UserPreferenceCloseEvent extends GwtEvent<UserPreferenceCloseEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.UserPreferenceCloseEventHandler
     */
    public static final GwtEvent.Type<UserPreferenceCloseEventHandler> TYPE = new GwtEvent.Type<UserPreferenceCloseEventHandler>();

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UserPreferenceCloseEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(UserPreferenceCloseEventHandler handler) {
        handler.onClose(this);
    }
}
