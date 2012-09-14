package org.iplantc.admin.belphegor.client.gxt3.views.widgets;

import com.google.gwt.user.client.ui.IsWidget;

public interface BelphegorAppsToolbar extends IsWidget {
    public interface Presenter {

        void onAddCategoryClicked();

        void onRenameCategoryClicked();

        void onDeleteClicked();

        void onRestoreAppClicked();
    }

    void setAddCategoryButtonEnabled(boolean enabled);

    void setRenameCategoryButtonEnabled(boolean enabled);

    void setDeleteButtonEnabled(boolean enabled);

    void setRestoreButtonEnabled(boolean enabled);
}