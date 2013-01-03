package org.iplantc.de.client.notifications.views;

import org.iplantc.de.client.notifications.util.NotificationHelper.Category;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.button.TextButton;

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

        void onDeleteAllClicked();

    }

    void setDeleteButtonEnabled(boolean enabled);

    void setPresenter(Presenter p);

    void setRefreshButton(TextButton refreshBtn);

}
