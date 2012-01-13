package org.iplantc.de.client;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.events.LogoutEvent;
import org.iplantc.de.client.events.LogoutEventHandler;
import org.iplantc.de.client.utils.LogoutUtil;
import org.iplantc.de.client.utils.MessagePoller;
import org.iplantc.de.client.views.presenters.Presenter;
import org.iplantc.de.client.views.presenters.WorkspacePresenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

/**
 * Provides management of history tokens and coordinates the necessary operations with its' presenter.
 * 
 */
public class HistoryManager implements ValueChangeHandler<String> {
    private String cmd = new String();
    private String params = new String();
    private Presenter presenter = null;

    /**
     * Default constructor.
     */
    public HistoryManager() {
        // Add history listener
        History.addValueChangeHandler(this);

        presenter = new WorkspacePresenter();
        initEventHandlers();
    }

    private void initEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // register logout handler
        eventbus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
            @Override
            public void onLogout(LogoutEvent event) {
                // stop polling for messages
                MessagePoller poller = MessagePoller.getInstance();
                poller.stop();

                resetEventHandlers();
                handleToken(event.getHistoryToken());
                redirectToLogoutPage();
            }
        });
    }

    private void resetEventHandlers() {
        EventBus eventbus = EventBus.getInstance();
        eventbus.clearHandlers();

        initEventHandlers();
    }

    private void displayPresenter() {
        if (presenter != null) {
            presenter.display(cmd, params);
        }
    }

    private boolean isValidToken(String token) {
        return (token != null && token.length() > 0);
    }

    /**
     * Process a command from a history token.
     * 
     * @param token the history token to be processed.
     */
    public void processCommand(String token) {
        int idx = token.indexOf("|"); //$NON-NLS-1$

        // if we do not have a vertical bar, we may still have a valid token
        // (albeit one
        // without parameters)
        if (idx == -1) {
            idx = token.length();
        }

        cmd = token.substring(0, idx);
        params = (idx == token.length()) ? "" : token.substring(idx + 1, token.length()); //$NON-NLS-1$

        displayPresenter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        processCommand((String)event.getValue());
    }

    /**
     * Validate and dispatch browser event.
     * 
     * @param token the history token to be handled.
     * @return true if a valid token has been passed in.
     */
    public boolean handleToken(String token) {
        boolean ret = false; // assume failure

        if (isValidToken(token)) {
            History.newItem(token);

            ret = true;
        }

        return ret;
    }

    /**
     * Perform logout redirection.
     */
    private void redirectToLogoutPage() {
        Window.Location.assign(LogoutUtil.buildLogoutUrl());
    }
}
