package org.iplantc.de.admin.desktop.client.apps.views;

import org.iplantc.de.client.models.apps.AppCategory;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface AppCategorizeView extends IsWidget {

    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        void setAppCategories(List<AppCategory> children);
    }

    void setAppCategories(List<AppCategory> categories);

    List<AppCategory> getSelectedCategories();

    void setSelectedCategories(List<AppCategory> categories);

    void removeCategoryWithId(String categoryId);
}
