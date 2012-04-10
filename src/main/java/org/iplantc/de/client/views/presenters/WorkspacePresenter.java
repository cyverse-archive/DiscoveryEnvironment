package org.iplantc.de.client.views.presenters;

import org.iplantc.de.client.views.DefaultWorkspaceView;
import org.iplantc.de.client.views.LoginView;

/**
 * Defines presentation behavior for the workspace.
 */
public class WorkspacePresenter extends Presenter {
    /**
     * Creates a new workspace presenter with the default views.
     */
    public WorkspacePresenter() {
        addViews();
    }

    /**
     * Adds the known views to the workspace presenter.
     */
    private void addViews() {
        addView("workspace", new DefaultWorkspaceView()); //$NON-NLS-1$
        addView("login", new LoginView()); //$NON-NLS-1$
    }

    /**
     * Updates the view associated with the command.
     * 
     * @param cmd the presenter command that requires update.
     * @param params the parameters for the command.
     */
    @Override
    protected void updateView(final String cmd, final String params) {
        setCurrentView(cmd);
    }
}
