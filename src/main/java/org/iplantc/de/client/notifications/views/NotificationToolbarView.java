package org.iplantc.de.client.notifications.views;

import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * @author sriram
 * 
 */
public interface NotificationToolbarView extends IsWidget {

    public interface Presenter {

        void onFilterSelection(Category cat);

        void onDeleteClicked();

    }

    void setDeleteButtonEnabled(boolean enabled);

    void setPresenter(Presenter p);

}
