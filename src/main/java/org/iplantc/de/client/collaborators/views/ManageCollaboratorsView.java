/**
 * 
 */
package org.iplantc.de.client.collaborators.views;

import java.util.List;

import org.iplantc.core.uicommons.client.models.collaborators.Collaborator;
import org.iplantc.de.client.collaborators.presenter.ManageCollaboratorsPresenter;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author sriram
 * 
 */
public interface ManageCollaboratorsView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void addAsCollaborators(List<Collaborator> models);

        void removeFromCollaborators(List<Collaborator> models);

        void loadCurrentCollaborators();

        void searchUsers(String searchTerm);

    }

    void setPresenter(Presenter p);

    void loadData(List<Collaborator> models);

    void removeCollaborators(List<Collaborator> models);

    void mask(String maskText);

    void unmask();

    void setMode(ManageCollaboratorsPresenter.MODE mode);
}
