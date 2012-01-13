package org.iplantc.de.client.dispatchers;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.events.LogoutEvent;

/**
 * Defines the default implementation of an ActionDispatcher.
 * 
 * Currently, the only action handled is "logout"
 */
public class DefaultActionDispatcher implements ActionDispatcher {
    @Override
    public void dispatchAction(String tag) {
        if (tag.equals(Constants.CLIENT.logoutTag())) {
            EventBus eventbus = EventBus.getInstance();
            LogoutEvent event = new LogoutEvent();
            eventbus.fireEvent(event);
        }
    }
}
