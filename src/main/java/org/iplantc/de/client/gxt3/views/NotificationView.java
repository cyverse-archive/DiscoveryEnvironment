package org.iplantc.de.client.gxt3.views;

import java.util.List;

import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;

public interface NotificationView extends IsWidget {
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        /**
         * Filters the list of notifications by a given Category.
         * 
         * @param category
         */
        public void filterBy(Category category);

        /**
         * get current filter for notification window
         * 
         * @return {@link Category} category
         */
        public Category getCurrentFilter();

        /**
         * get current sort dir
         * 
         * @return
         */
        public SortDir getCurrentSortDir();

        /**
         * get current offset
         * 
         * @return
         */
        public int getCurrentOffset();

        /**
         * Get list of selected notification
         * 
         * @return a list containing selected notification objects
         */
        public List<Notification> getSelectedItems();
    }

    /**
     * Filters the list of notifications by a given Category.
     * 
     * @param category
     */
    public void filterBy(Category category);

    /**
     * get current filter for notification window
     * 
     * @return {@link Category} category
     */
    public Category getCurrentFilter();

    /**
     * get current sort dir
     * 
     * @return
     */
    public SortDir getCurrentSortDir();

    /**
     * get current offset
     * 
     * @return
     */
    public int getCurrentOffset();

    /**
     * Get list of selected notification
     * 
     * @return a list containing selected notification objects
     */
    public List<Notification> getSelectedItems();

    public void setPresenter(final Presenter presenter);

    public ListStore<Notification> getListStore();

    public void setNotifications(List<Notification> notifications);

}
