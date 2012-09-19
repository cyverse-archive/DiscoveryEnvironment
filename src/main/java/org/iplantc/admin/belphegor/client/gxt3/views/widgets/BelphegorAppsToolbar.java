package org.iplantc.admin.belphegor.client.gxt3.views.widgets;

import com.google.gwt.user.client.ui.IsWidget;

public interface BelphegorAppsToolbar extends IsWidget {
    public interface Presenter {

        void onAddAppGroupClicked();

        void onRenameAppGroupClicked();

        void onDeleteClicked();

        void onRestoreAppClicked();
    }

    void setAddAppGroupButtonEnabled(boolean enabled);

    void setRenameAppGroupButtonEnabled(boolean enabled);

    void setDeleteButtonEnabled(boolean enabled);

    void setRestoreButtonEnabled(boolean enabled);

    void setPresenter(Presenter presenter);
}