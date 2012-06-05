package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * Fired when user updates his settings
 * 
 * @author sriram
 * 
 */
public class SettingsUpdatedEvent extends GwtEvent<SettingsUpdatedEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.SettingsUpdatedEventHandler
     */
    public static final GwtEvent.Type<SettingsUpdatedEventHandler> TYPE = new GwtEvent.Type<SettingsUpdatedEventHandler>();

    @Override
    public Type<SettingsUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SettingsUpdatedEventHandler handler) {
        handler.onUpdate(this);

    }

}
