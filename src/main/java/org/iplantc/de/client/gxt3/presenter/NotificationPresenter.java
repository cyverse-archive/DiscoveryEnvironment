package org.iplantc.de.client.gxt3.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.gxt3.model.NotificationAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.NotificationList;
import org.iplantc.de.client.gxt3.model.NotificationMessage;
import org.iplantc.de.client.gxt3.views.NotificationView;
import org.iplantc.de.client.gxt3.views.NotificationView.Presenter;
import org.iplantc.de.client.utils.NotificationHelper;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * 
 * A presenter for notification window
 * 
 * @author sriram
 * 
 */
public class NotificationPresenter implements Presenter {

    private final NotificationView view;
    private final NotificationAutoBeanFactory factory = GWT.create(NotificationAutoBeanFactory.class);

    private PagingLoadResult<NotificationMessage> callbackResult;
    private final NotificationHelper helper;

    public NotificationPresenter(NotificationView view) {
        this.view = view;
        this.view.setPresenter(this);
        helper = NotificationHelper.getInstance();
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        view.setLoader(initProxyLoader());
    }

    private PagingLoader<PagingLoadConfig, PagingLoadResult<NotificationMessage>> initProxyLoader() {

        RpcProxy<PagingLoadConfig, PagingLoadResult<NotificationMessage>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<NotificationMessage>>() {
            @Override
            public void load(final PagingLoadConfig loadConfig,
                    final AsyncCallback<PagingLoadResult<NotificationMessage>> callback) {
                // TODO: implement sorting and filter
                Services.MESSAGE_SERVICE.getNotifications(loadConfig.getLimit(), loadConfig.getOffset(),
                        "",
                        com.sencha.gxt.data.shared.SortDir.DESC.toString(),
                        new NotificationServiceCallback(loadConfig, callback));

            }

        };

        final PagingLoader<PagingLoadConfig, PagingLoadResult<NotificationMessage>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<NotificationMessage>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, NotificationMessage, PagingLoadResult<NotificationMessage>>(
                view.getListStore()));

        return loader;
    }

    @Override
    public void filterBy(Category category) {
        // TODO Auto-generated method stub

    }

    @Override
    public Category getCurrentFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortDir getCurrentSortDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCurrentOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<NotificationMessage> getSelectedItems() {
        // TODO Auto-generated method stub
        return null;
    }

    private List<NotificationMessage> getNotificationMessages(AutoBean<NotificationList> bean) {
        List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
        for (Notification n : bean.as().getNotifications()) {
            NotificationMessage nm = n.getMessage();
            nm.setCategory(Category.fromTypeString(n.getCategory()));
            messages.add(nm);
        }
        return messages;
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
            Number jsonTotal = JsonUtil.getNumber(JsonUtil.getObject(result), "total");
            if (jsonTotal != null) {
                total = jsonTotal.intValue();
            }

            List<NotificationMessage> messages = getNotificationMessages(bean);

            callbackResult = new PagingLoadResultBean<NotificationMessage>(messages, total,
                    loadConfig.getOffset());
            callback.onSuccess(callbackResult);
        }
    }

}
