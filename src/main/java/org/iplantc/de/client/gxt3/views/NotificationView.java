package org.iplantc.de.client.gxt3.views;

import java.util.List;

import org.iplantc.de.client.gxt3.model.NotificationMessage;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
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
         * get default paging config
         * 
         * @return default FilterPagingLoadConfig
         */
        public FilterPagingLoadConfig buildDefaultLoadConfig();

        /**
         * 
         * 
         */
        public void onNotificationSelection(List<NotificationMessage> items);
    }

    /**
     * get current loader config
     * 
     * @return the current load config
     */
    public FilterPagingLoadConfig getCurrentLoadConfig();

    /**
     * Get list of selected notification
     * 
     * @return a list containing selected notification objects
     */
    public List<NotificationMessage> getSelectedItems();

    public void setPresenter(final Presenter presenter);

    public ListStore<NotificationMessage> getListStore();

    /**
     * loads notifications using given laod conig
     * 
     * @param config FilterPagingLoadConfig
     */
    public void loadNotifications(FilterPagingLoadConfig config);

    public void setLoader(
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> loader);

    void setNorthWidget(IsWidget widget);

}
