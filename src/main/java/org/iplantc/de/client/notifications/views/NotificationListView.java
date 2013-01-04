/**
 * 
 */
package org.iplantc.de.client.notifications.views;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.notifications.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.client.notifications.events.DeleteNotificationsUpdateEventHandler;
import org.iplantc.de.client.notifications.models.Notification;
import org.iplantc.de.client.notifications.models.NotificationAutoBeanFactory;
import org.iplantc.de.client.notifications.models.NotificationList;
import org.iplantc.de.client.notifications.models.NotificationMessage;
import org.iplantc.de.client.notifications.services.MessageServiceFacade;
import org.iplantc.de.client.notifications.util.NotificationHelper;
import org.iplantc.de.client.notifications.util.NotificationHelper.Category;
import org.iplantc.de.client.utils.NotifyInfo;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * New notifications as list
 * 
 * @author sriram
 * 
 */
public class NotificationListView implements IsWidget {

    private ListView<NotificationMessage, NotificationMessage> view;
    private ListStore<NotificationMessage> store;
    private int total_unseen;
    private HorizontalPanel hyperlinkPanel;

    private final NotificationAutoBeanFactory factory = GWT.create(NotificationAutoBeanFactory.class);

    public static final int NEW_NOTIFICATIONS_LIMIT = 10;

    final Resources resources = GWT.create(Resources.class);
    final Style style = resources.css();
    final Renderer r = GWT.create(Renderer.class);

    interface Renderer extends XTemplates {
        @XTemplate("<div class=\"{style.thumb}\"> {msg.message}</div>")
        public SafeHtml renderItem(NotificationMessage msg, Style style);
    }

    interface Style extends CssResource {
        String over();

        String select();

        String thumb();

        String thumbWrap();
    }

    interface Resources extends ClientBundle {
        @Source("NotificationListView.css")
        Style css();
    }

    ModelKeyProvider<NotificationMessage> kp = new ModelKeyProvider<NotificationMessage>() {
        @Override
        public String getKey(NotificationMessage item) {
            return item.getTimestamp() + "";
        }
    };

    ListViewCustomAppearance<NotificationMessage> appearance = new ListViewCustomAppearance<NotificationMessage>(
            "." + style.thumbWrap(), style.over(), style.select()) {

        @Override
        public void renderEnd(SafeHtmlBuilder builder) {
            String markup = new StringBuilder("<div class=\"").append(CommonStyles.get().clear())
                    .append("\"></div>").toString();
            builder.appendHtmlConstant(markup);
        }

        @Override
        public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
            builder.appendHtmlConstant("<div class='" + style.thumbWrap()
                    + "' style='border: 1px solid white'>");
            builder.append(content);
            builder.appendHtmlConstant("</div>");
        }

    };

    public NotificationListView() {
        resources.css().ensureInjected();
        initListeners();
        hyperlinkPanel = new HorizontalPanel();
        hyperlinkPanel.setSpacing(2);
        updateNotificationLink();
    }

    private void initListeners() {
        EventBus.getInstance().addHandler(DeleteNotificationsUpdateEvent.TYPE,
                new DeleteNotificationsUpdateEventHandler() {

                    @Override
                    public void onDelete(DeleteNotificationsUpdateEvent event) {
                        if (event.getMessages() != null) {
                            for (NotificationMessage deleted : event.getMessages()) {
                                NotificationMessage nm = store.findModel(deleted);
                                if (nm != null) {
                                    store.remove(nm);
                                }
                            }
                        } else {
                            store.clear();
                        }

                    }
                });
    }

    public void highlightNewNotifications() {
        List<NotificationMessage> new_notifications = store.getAll();
        // TODO: implement higlight
    }

    //
    public void markAsSeen() {
        java.util.List<NotificationMessage> new_notifications = store.getAll();
        JSONArray arr = new JSONArray();
        int i = 0;
        if (new_notifications.size() > 0) {
            for (NotificationMessage n : new_notifications) {
                arr.set(i++, new JSONString(n.getId().toString()));
                // set seen here
            }

            JSONObject obj = new JSONObject();
            obj.put("uuids", arr);

            org.iplantc.de.client.notifications.services.MessageServiceFacade facade = new org.iplantc.de.client.notifications.services.MessageServiceFacade();
            facade.markAsSeen(obj, new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    // Do nothing intentionally
                }

                @Override
                public void onFailure(Throwable caught) {
                    org.iplantc.core.uicommons.client.ErrorHandler.post(caught);
                }
            });
        }

    }

    public void fetchUnseenNotifications() {
        MessageServiceFacade facadeMessageService = new MessageServiceFacade();
        facadeMessageService.getRecentMessages(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                processMessages(result);
            }
        });
    }

    /**
     * Process method takes in a JSON String, breaks out the individual messages, transforms them into
     * events, finally the event is fired.
     * 
     * @param json string to be processed.
     */
    public void processMessages(final String json) {
        // cache before removing
        List<NotificationMessage> temp = store.getAll();
        store.clear();
        boolean displayInfo = false;

        if (json != null) {
            AutoBean<NotificationList> bean = AutoBeanCodex
                    .decode(factory, NotificationList.class, json);
            List<Notification> notifications = bean.as().getNotifications();
            for (Notification n : notifications) {
                NotificationMessage nm = n.getMessage();
                nm.setSeen(n.isSeen());
                nm.setCategory(Category.fromTypeString(n.getCategory()));
                if (nm != null && !isExist(temp, nm)) {
                    store.add(nm);
                    displayNotificationPopup(nm);
                    displayInfo = true;
                }
            }
        }
        if (total_unseen > NEW_NOTIFICATIONS_LIMIT && displayInfo) {
            NotifyInfo.display(I18N.DISPLAY.newNotifications(), I18N.DISPLAY.newNotificationsAlert());
        }
        // store.sort(Notification.PROP_TIMESTAMP, SortDir.DESC);
        highlightNewNotifications();
    }

    private void displayNotificationPopup(NotificationMessage n) {
        if (!n.isSeen()) {
            if (n.getCategory().equals(Category.DATA)) {
                NotifyInfo.display(Category.DATA.toString(), n.getMessage());
            } else if (n.getCategory().equals(Category.ANALYSIS)) {
                NotifyInfo.display(Category.ANALYSIS.toString(), n.getMessage());
            }
        }
    }

    private boolean isExist(List<NotificationMessage> list, NotificationMessage n) {
        for (NotificationMessage noti : list) {
            if (noti.getId().equals(n.getId())) {
                return true;
            }
        }

        return false;

    }

    public void setUnseenCount(int count) {
        this.total_unseen = count;
        updateNotificationLink();
    }

    private void updateNotificationLink() {
        hyperlinkPanel.clear();
        hyperlinkPanel.add(buildNotificationHyerlink());
        if (total_unseen > 0) {
            hyperlinkPanel.add(buildAckAllHyperlink());
        }
    }

    private Anchor buildAckAllHyperlink() {
        Anchor link = new Anchor(I18N.DISPLAY.markAllasSeen());
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                MessageServiceFacade facade = new MessageServiceFacade();
                facade.acknowledgeAll(new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);

                    }

                    @Override
                    public void onSuccess(String result) {
                        Info.display(I18N.DISPLAY.notifications(), I18N.DISPLAY.markAllasSeenSuccess());
                    }
                });

            }
        });

        link.setStyleName("de_hyperlink");
        return link;

    }

    private Anchor buildNotificationHyerlink() {
        String displayText;
        if (total_unseen > 0) {
            displayText = I18N.DISPLAY.newNotifications() + " (" + total_unseen + ")";
        } else {
            displayText = I18N.DISPLAY.allNotifications();
        }

        Anchor link = new Anchor(displayText);
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (total_unseen > 0) {
                    showNotificationWindow(NotificationHelper.Category.NEW);
                } else {
                    showNotificationWindow(NotificationHelper.Category.ALL);
                }

            }
        });

        link.setStyleName("de_hyperlink");
        return link;
    }

    /** Makes the notification window visible and filters by a category */
    private void showNotificationWindow(final Category category) {
        NotificationWindowConfig config = new NotificationWindowConfig();
        config.setCategory(category);

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory
                .buildWindowConfig(Constants.CLIENT.myNotifyTag(), config);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        dispatcher.dispatchAction(Constants.CLIENT.myNotifyTag());
    }

    @Override
    public Widget asWidget() {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.setBorders(true);
        store = new ListStore<NotificationMessage>(kp);
        view = new ListView<NotificationMessage, NotificationMessage>(store,
                new IdentityValueProvider<NotificationMessage>(), appearance);

        view.getSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<NotificationMessage>() {

                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<NotificationMessage> event) {
                        final NotificationMessage msg = event.getSelection().get(0);
                        if (msg != null) {
                            NotificationHelper.getInstance().view(msg);
                        }
                    }

                });

        view.setCell(new SimpleSafeHtmlCell<NotificationMessage>(
                new AbstractSafeHtmlRenderer<NotificationMessage>() {

                    @Override
                    public SafeHtml render(NotificationMessage object) {
                        return r.renderItem(object, style);
                    }
                }));

        view.setSize("250px", "250px");
        view.setBorders(false);
        container.add(view);
        hyperlinkPanel.setHeight("30px");
        container.add(hyperlinkPanel);
        return container;
    }

}
