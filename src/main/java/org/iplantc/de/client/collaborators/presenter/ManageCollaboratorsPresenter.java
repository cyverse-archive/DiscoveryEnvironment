/**
 * 
 */
package org.iplantc.de.client.collaborators.presenter;

import java.util.List;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.collaborators.models.Collaborator;
import org.iplantc.de.client.collaborators.util.CollaboratorsUtil;
import org.iplantc.de.client.collaborators.views.ManageCollaboratorsView;
import org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * @author sriram
 * 
 */
public class ManageCollaboratorsPresenter implements Presenter {

    private final ManageCollaboratorsView view;
    private MODE currentMode;

    public static enum MODE {
        MANAGE, SEARCH
    };

    public ManageCollaboratorsPresenter(ManageCollaboratorsView view) {
        this.view = view;
        view.setPresenter(this);
        loadCurrentCollaborators();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.core.uicommons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget
     * )
     */
    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#addAsCollaborators
     * (java.util.List)
     */
    @Override
    public void addAsCollaborators(final List<Collaborator> models) {
        CollaboratorsUtil.addCollaborators(models, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                // remove added models from search results
                view.removeCollaborators(models);
            }

            @Override
            public void onFailure(Throwable caught) {
                // do nothing
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#removeFromCollaborators
     * (java.util.List)
     */
    @Override
    public void removeFromCollaborators(final List<Collaborator> models) {
        CollaboratorsUtil.removeCollaborators(models, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                view.removeCollaborators(models);

            }

            @Override
            public void onFailure(Throwable caught) {
                // do nothing
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#loadCurrentCollaborators
     * ()
     */
    @Override
    public void loadCurrentCollaborators() {
        view.mask(null);
        CollaboratorsUtil.getCollaborators(new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
            }

            @Override
            public void onSuccess(Void result) {
                updateCurrentMode(MODE.MANAGE);
                view.unmask();
                view.loadData(CollaboratorsUtil.getCurrentCollaborators());
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.collaborators.views.ManageCollaboratorsView.Presenter#searchUsers(java.lang
     * .String)
     */
    @Override
    public void searchUsers(String searchTerm) {
        view.mask(I18N.DISPLAY.searching());
        CollaboratorsUtil.search(searchTerm, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
            }

            @Override
            public void onSuccess(Void result) {
                updateCurrentMode(MODE.SEARCH);
                view.unmask();
                view.loadData(CollaboratorsUtil.getSearchResutls());
            }
        });

    }

    private void updateCurrentMode(MODE m) {
        currentMode = m;
        view.setMode(m);
    }

}
