package org.iplantc.admin.belphegor.client.apps.presenter;

import java.util.List;

import org.iplantc.admin.belphegor.client.apps.views.AppCategorizeView;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.gwt.user.client.ui.HasOneWidget;

public class AppCategorizePresenter implements AppCategorizeView.Presenter {

    private final AppCategorizeView view;
    private final App app;

    AppCategorizePresenter(AppCategorizeView view, final App app) {
        this.view = view;
        this.app = app;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setAppGroups(List<AppGroup> children) {
        view.setAppGroups(children);

        // Remove trash and beta from the store.
        ToolIntegrationAdminProperties props = ToolIntegrationAdminProperties.getInstance();
        view.removeGroupWithId(props.getDefaultBetaAnalysisGroupId());
        view.removeGroupWithId(props.getDefaultTrashAnalysisGroupId());

        if (app.getGroups() != null) {
            view.setSelectedGroups(app.getGroups());
        }
    }

    public List<AppGroup> getSelectedGroups() {
        return view.getSelectedGroups();
    }
}
