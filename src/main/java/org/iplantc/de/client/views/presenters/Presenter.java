package org.iplantc.de.client.views.presenters;

import java.util.HashMap;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Defines base functionality for classes serving as a "presenter."
 */
public abstract class Presenter {
    /**
     * Maps history tokens to their corresponding views.
     */
    private HashMap<String, View> viewFor;

    /**
     * The currently enabled view.
     */
    private View currentView;

    /**
     * Creates a presenter with no views.
     */
    protected Presenter() {
        viewFor = new HashMap<String, View>();
        currentView = null;
    }

    /**
     * Adds a view to the presenter.
     * 
     * @param historyToken the history token associated with the view.
     * @param view the view.
     */
    protected void addView(String historyToken, View view) {
        viewFor.put(historyToken, view);
    }

    /**
     * Sets the currently enabled view. If a view is already enabled then it will be unloaded. If the
     * history token isn't recognized then the view will be left unchanged.
     * 
     * @param historyToken the history token associated with the view.
     */
    protected void setCurrentView(String historyToken) {
        View view = viewFor.get(historyToken);
        if (view != null && currentView != view) {
            removeCurrentView();
            currentView = view;
            displayCurrentView();
        }
    }

    /**
     * Removes the currently enabled view if a view is active.
     */
    private void removeCurrentView() {
        if (currentView != null) {
            RootPanel.get().remove(currentView.getDisplayWidget());
            currentView = null;
        }
    }

    /**
     * Displays the currently enabled view.
     */
    private void displayCurrentView() {
        if (currentView != null) {
            addDisplayWidget();
            GWT.runAsync(new RunAsyncCallback() {
                @Override
                public void onSuccess() {
                    currentView.display();
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.unableToBuildWorkspace(), caught);
                }
            });
        }
    }

    /**
     * Adds the current view's display widget to the root panel.
     */
    private void addDisplayWidget() {
        Widget displayWidget = currentView.getDisplayWidget();
        if (displayWidget != null) {
            RootPanel.get().add(displayWidget);
        }
    }

    /**
     * Updates the view.
     * 
     * @param cmd the command being processed.
     * @param params the command parameters.
     */
    protected abstract void updateView(String cmd, String params);

    /**
     * Displays the current view.
     * 
     * @param cmd the command being processed.
     * @param params the command parameters.
     */
    public void display(String cmd, String params) {
        updateView(cmd, params);
    }


}
