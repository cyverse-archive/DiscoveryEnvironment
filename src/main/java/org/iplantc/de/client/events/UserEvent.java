package org.iplantc.de.client.events;

import org.iplantc.de.client.models.WindowConfig;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Generic event represents user interactions with the windowed application environment.
 * 
 * The intention is that this event will be fired when a user clicks in a menu. The action and tag values
 * will be provided to handlers to that they may determine operation and appropriately react.
 * 
 * @author amuir
 * 
 */
public class UserEvent extends GwtEvent<UserEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.UserEventHandler
     */
    public static final GwtEvent.Type<UserEventHandler> TYPE = new GwtEvent.Type<UserEventHandler>();

    protected String action = new String();
    protected String type = new String();
    protected WindowConfig config;

    /**
     * Instantiate from action and tag.
     * 
     * @param action string representation of user action.
     * @param type string representation of associated type.
     * @param config associated parameters.
     */
    public UserEvent(String action, String type, WindowConfig config) {
        setAction(action);
        setType(type);
        this.config = config;
    }

    /**
     * Instantiate from an action and type.
     * 
     * @param action string representation of user action.
     * @param type string representation of associated type.
     */
    public UserEvent(String action, String type) {
        this(action, type, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(UserEventHandler handler) {
        handler.onEvent(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<UserEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the action associated with the user event.
     * 
     * @return action user action.
     */
    public String getAction() {
        return action;
    }

    private void setAction(String action) {
        if (action != null) {
            this.action = action;
        }
    }

    /**
     * Retrieve the type associated with the user event.
     * 
     * @return tag type for the user action.
     */
    public String getType() {
        return type;
    }

    private void setType(String tag) {
        if (tag != null) {
            this.type = tag;
        }
    }

    /**
     * Retrieve parameters for the user action.
     * 
     * @return parameters for the user action.
     */
    public WindowConfig getParams() {
        return config;
    }
}
