package org.iplantc.de.client.gxt3.views;

import java.util.List;

import org.iplantc.de.client.gxt3.model.NotificationMessage;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

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
        public List<NotificationMessage> getSelectedItems();
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
    public List<NotificationMessage> getSelectedItems();

    public void setPresenter(final Presenter presenter);

    public ListStore<NotificationMessage> getListStore();

    public void setNotifications(List<NotificationMessage> notifications);

    public void setLoader(PagingLoader<PagingLoadConfig, PagingLoadResult<NotificationMessage>> loader);

}
