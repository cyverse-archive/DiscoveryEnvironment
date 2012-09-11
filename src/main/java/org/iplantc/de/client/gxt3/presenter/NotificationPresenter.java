package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.gxt3.model.NotificationAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.NotificationList;
import org.iplantc.de.client.gxt3.views.NotificationView;
import org.iplantc.de.client.gxt3.views.NotificationView.Presenter;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.sencha.gxt.data.shared.SortDir;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
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
    private final MessageServiceFacade facade;
    private final NotificationAutoBeanFactory factory = GWT.create(NotificationAutoBeanFactory.class);

    private PagingLoadResult<Notification> callbackResult;

    public NotificationPresenter(NotificationView view, MessageServiceFacade facade) {
        this.view = view;
        this.facade = facade;
        this.view.setPresenter(this);
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        view.setLoader(initProxyLoader());
    }

    private PagingLoader<PagingLoadConfig, PagingLoadResult<Notification>> initProxyLoader() {

        RpcProxy<PagingLoadConfig, PagingLoadResult<Notification>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Notification>>() {
            @Override
            public void load(final PagingLoadConfig loadConfig,
                    final AsyncCallback<PagingLoadResult<Notification>> callback) {
                // TODO: implement sorting and filter
                facade.getNotifications(loadConfig.getLimit(), loadConfig.getOffset(), "",
                        com.sencha.gxt.data.shared.SortDir.DESC.toString(), new AsyncCallback<String>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                org.iplantc.core.uicommons.client.ErrorHandler.post(caught);

                            }

                            @Override
                            public void onSuccess(String result) {
                                AutoBean<NotificationList> bean = AutoBeanCodex.decode(factory,
                                        NotificationList.class, result);
                                int total = 0;
                                Number jsonTotal = JsonUtil.getNumber(JsonUtil.getObject(result),
                                        "total");
                                if (jsonTotal != null) {
                                    total = jsonTotal.intValue();
                                }
                                System.out.println("--->" + bean.as().getNotifications());
                                callbackResult = new PagingLoadResultBean<Notification>(bean.as()
                                        .getNotifications(), total, loadConfig.getOffset());
                                callback.onSuccess(callbackResult);
                            }
                        });

            }

        };

        final PagingLoader<PagingLoadConfig, PagingLoadResult<Notification>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Notification>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Notification, PagingLoadResult<Notification>>(
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
    public List<Notification> getSelectedItems() {
        // TODO Auto-generated method stub
        return null;
    }

}
