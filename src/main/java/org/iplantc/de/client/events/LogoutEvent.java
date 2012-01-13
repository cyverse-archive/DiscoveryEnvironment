package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents a user logging out of the system.
 * 
 * @author amuir
 * 
 */
public class LogoutEvent extends GwtEvent<LogoutEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.LogoutEventHandler
     */
    public static final GwtEvent.Type<LogoutEventHandler> TYPE = new GwtEvent.Type<LogoutEventHandler>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(LogoutEventHandler handler) {
        handler.onLogout(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<LogoutEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the token used when the user navigates back within their browser.
     * 
     * @return history token for logout.
     */
    public String getHistoryToken() {
        return "logout"; //$NON-NLS-1$
    }
}
