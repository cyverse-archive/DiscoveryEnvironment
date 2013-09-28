package org.iplantc.admin.belphegor.client.apps.views;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppCategorizeView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void setAppGroups(List<AppGroup> children);
    }

    void setAppGroups(List<AppGroup> children);

    List<AppGroup> getSelectedGroups();

    void setSelectedGroups(List<AppGroup> groups);

    void removeGroupWithId(String groupId);
}
