package org.iplantc.de.client.notifications.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.notifications.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.client.notifications.models.Notification;
import org.iplantc.de.client.notifications.models.NotificationAutoBeanFactory;
import org.iplantc.de.client.notifications.models.NotificationList;
import org.iplantc.de.client.notifications.models.NotificationMessage;
import org.iplantc.de.client.notifications.services.MessageServiceFacade;
import org.iplantc.de.client.notifications.util.NotificationHelper;
import org.iplantc.de.client.notifications.util.NotificationHelper.Category;
import org.iplantc.de.client.notifications.views.NotificationToolbarView;
import org.iplantc.de.client.notifications.views.NotificationToolbarViewImpl;
import org.iplantc.de.client.notifications.views.NotificationView;
import org.iplantc.de.client.notifications.views.NotificationView.Presenter;
import org.iplantc.de.client.utils.builders.context.AnalysisContextBuilder;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * 
 * A presenter for notification window
 * 
 * @author sriram
 * 
 */
public class NotificationPresenter implements Presenter, NotificationToolbarView.Presenter {

    private final NotificationView view;
    private final NotificationToolbarView toolbar;
    private final NotificationAutoBeanFactory factory = GWT.create(NotificationAutoBeanFactory.class);

    private PagingLoadResult<NotificationMessage> callbackResult;
    private Category currentCategory;

    public NotificationPresenter(NotificationView view) {
        this.view = view;
        toolbar = new NotificationToolbarViewImpl();
        toolbar.setPresenter(this);
        view.setNorthWidget(toolbar);
        this.view.setPresenter(this);
        setRefreshButton(view.getRefreshButton());
        // set default cat
        currentCategory = Category.ALL;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        view.setLoader(initProxyLoader());
    }

    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> initProxyLoader() {

        RpcProxy<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> proxy = new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>>() {
            @Override
            public void load(final FilterPagingLoadConfig loadConfig,
                    final AsyncCallback<PagingLoadResult<NotificationMessage>> callback) {
                Services.MESSAGE_SERVICE.getNotifications(loadConfig.getLimit(), loadConfig.getOffset(),
                        (loadConfig.getFilters().get(0).getField()) == null ? "" : loadConfig
                                .getFilters().get(0).getField().toLowerCase(), loadConfig.getSortInfo()
                                .get(0).getSortDir().toString(), new NotificationServiceCallback(
                                loadConfig, callback));

            }

        };

        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, NotificationMessage, PagingLoadResult<NotificationMessage>>(
                view.getListStore()));

        loader.useLoadConfig(buildDefaultLoadConfig());
        return loader;
    }

    @Override
    public void filterBy(Category category) {
        currentCategory = category;
        FilterPagingLoadConfig config = view.getCurrentLoadConfig();
        FilterConfig filterBean = new FilterConfigBean();
        if (!currentCategory.toString().equalsIgnoreCase("ALL")) {
            filterBean.setField(currentCategory.toString());
        }

        List<FilterConfig> filters = new ArrayList<FilterConfig>();
        filters.add(filterBean);
        config.setFilters(filters);

        view.loadNotifications(config);

    }

    @Override
    public void onNotificationSelection(List<NotificationMessage> items) {
        if (items == null || items.size() == 0) {
            toolbar.setDeleteButtonEnabled(false);
        } else {
            toolbar.setDeleteButtonEnabled(true);
        }
    }

    private List<NotificationMessage> getNotificationMessages(AutoBean<NotificationList> bean) {
        List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
        for (Notification n : bean.as().getNotifications()) {
            NotificationMessage nm = n.getMessage();
            nm.setCategory(Category.fromTypeString(n.getCategory()));
            if (n.getCategory().equalsIgnoreCase(Category.ANALYSIS.toString())) {
                AnalysisContextBuilder builder = new AnalysisContextBuilder();
                nm.setContext(builder.build(n.getNotificationPayload()));
            }
            messages.add(nm);
        }
        return messages;
    }

    public FilterPagingLoadConfig buildDefaultLoadConfig() {
        FilterPagingLoadConfig config = new FilterPagingLoadConfigBean();
        config.setLimit(10);

        SortInfo info = new SortInfoBean("timestamp", SortDir.DESC);
        List<SortInfo> sortInfo = new ArrayList<SortInfo>();
        sortInfo.add(info);
        config.setSortInfo(sortInfo);

        FilterConfig filterBean = new FilterConfigBean();
        if (!currentCategory.toString().equalsIgnoreCase("ALL")) {
            filterBean.setField(currentCategory.toString());
        }

        List<FilterConfig> filters = new ArrayList<FilterConfig>();
        filters.add(filterBean);
        config.setFilters(filters);

        return config;
    }

    private final class NotificationServiceCallback implements AsyncCallback<String> {
        private final PagingLoadConfig loadConfig;
        private final AsyncCallback<PagingLoadResult<NotificationMessage>> callback;

        private NotificationServiceCallback(PagingLoadConfig loadConfig,
                AsyncCallback<PagingLoadResult<NotificationMessage>> callback) {
            this.loadConfig = loadConfig;
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            org.iplantc.core.uicommons.client.ErrorHandler.post(caught);

        }

        @Override
        public void onSuccess(String result) {
            AutoBean<NotificationList> bean = AutoBeanCodex.decode(factory, NotificationList.class,
                    result);
            int total = 0;
            String jsonTotal = JsonUtil.getString(JsonUtil.getObject(result), "total");
            if (jsonTotal != null) {
                total = Integer.parseInt(jsonTotal);
            }

            List<NotificationMessage> messages = getNotificationMessages(bean);

            callbackResult = new PagingLoadResultBean<NotificationMessage>(messages, total,
                    loadConfig.getOffset());
            callback.onSuccess(callbackResult);
            NotificationHelper.getInstance().markAsSeen(messages);

        }
    }

    @Override
    public void onFilterSelection(Category cat) {
        filterBy(cat);
    }

    @Override
    public void onDeleteClicked() {
        NotificationHelper.getInstance().delete(view.getSelectedItems(), new Command() {
            @Override
            public void execute() {
                view.loadNotifications(view.getCurrentLoadConfig());
            }
        });

    }

    @Override
    public void setRefreshButton(TextButton refreshBtn) {
        if (refreshBtn != null) {
            refreshBtn.setText(I18N.DISPLAY.refresh());
            toolbar.setRefreshButton(refreshBtn);
        }
    }

    @Override
    public void onDeleteAllClicked() {
        view.mask();
        MessageServiceFacade facade = new MessageServiceFacade();
        facade.deleteAll(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(String result) {
                view.unmask();
                view.loadNotifications(view.getCurrentLoadConfig());
                DeleteNotificationsUpdateEvent event = new DeleteNotificationsUpdateEvent(null);
                EventBus.getInstance().fireEvent(event);
            }
        });

    }
}
