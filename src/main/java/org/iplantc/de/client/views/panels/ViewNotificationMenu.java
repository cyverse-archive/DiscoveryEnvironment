/**
 * 
 */
package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.utils.NotificationHelper;
import org.iplantc.de.client.utils.NotificationHelper.Category;
import org.iplantc.de.client.utils.NotifyInfo;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.ListViewSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author sriram
 * 
 */
public class ViewNotificationMenu extends Menu {

    private ListStore<Notification> store;

    private CustomListView<Notification> view;

    public static final int NEW_NOTIFICATIONS_LIMIT = 10;

    private int total_unseen;

    private NotificationHelper helper = NotificationHelper.getInstance();

    public ViewNotificationMenu() {
        setLayout(new FitLayout());
        view = initList();
        LayoutContainer lc = buildPanel();
        lc.add(view);
        add(lc);
    }

    private LayoutContainer buildPanel() {
        LayoutContainer lc = new LayoutContainer();
        lc.setLayout(new FitLayout());
        lc.setSize(250, 270);
        lc.setBorders(false);
        return lc;
    }

    private CustomListView<Notification> initList() {
        store = new ListStore<Notification>();
        CustomListView<Notification> view = new CustomListView<Notification>();

        view.setTemplate(getTemplate());
        view.getSelectionModel().addSelectionChangedListener(
                new SelectionChangedListener<Notification>() {

                    @Override
                    public void selectionChanged(SelectionChangedEvent<Notification> se) {
                        final Notification notification = se.getSelectedItem();
                        if (notification == null) {
                            return;
                        }
                        NotificationHelper.getInstance().view(notification);
                        hide();
                    }
                });
        view.setItemSelector("div.search-item"); //$NON-NLS-1$
        view.setStore(store);
        view.setEmptyText(I18N.DISPLAY.noNewNotifications());
        return view;
    }

    @Override
    public void showAt(int x, int y) {
        super.showAt(x, y);
        highlightNewNotifications();
        NotificationCountUpdateEvent evnCountUpdateEvent = null;
        if (total_unseen > NEW_NOTIFICATIONS_LIMIT) {
            evnCountUpdateEvent = new NotificationCountUpdateEvent(total_unseen
                    - NEW_NOTIFICATIONS_LIMIT);
        } else {
            evnCountUpdateEvent = new NotificationCountUpdateEvent(0);
        }
        EventBus.getInstance().fireEvent(evnCountUpdateEvent);
        helper.markAsSeen(store.getModels());
    }

    private String getTemplate() {
        StringBuilder template = new StringBuilder();
        template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$
        template.append("<tpl if=\"context\"> <div class='notification_context'> </tpl>");
        template.append("{message} <tpl if=\"context\"> </div> </tpl></div></tpl>");
        return template.toString();
    }

    private void highlightNewNotifications() {
        List<Notification> new_notifications = store.getModels();
        for (Notification n : new_notifications) {
            if (n.get(Notification.SEEN) == null
                    || Boolean.parseBoolean(n.get(Notification.SEEN).toString()) == false) {
                view.highlight(view.getStore().indexOf(n), true);
            } else {
                view.highlight(view.getStore().indexOf(n), false);
            }

        }
    }

    public void fetchUnseenNotifications(final int count) {
        this.total_unseen = count;
        MessageServiceFacade facadeMessageService = new MessageServiceFacade();
        facadeMessageService.getNotifications(NEW_NOTIFICATIONS_LIMIT, 0, null, null, null,
                new AsyncCallback<String>() {

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

        JSONObject objMessages = JSONParser.parseStrict(json).isObject();
        int size = 0;
        store.removeAll();

        if (objMessages != null) {
            JSONArray arr = objMessages.get("messages").isArray(); //$NON-NLS-1$
            if (arr != null) {
                JSONObject objItem;
                size = arr.size();
                trimStore(size);
                for (int i = 0; i < size; i++) {
                    if (isVisible() && !isMasked()) {
                        mask(I18N.DISPLAY.loadingMask());
                    }
                    objItem = arr.get(i).isObject();
                    if (objItem != null) {
                        Notification n = buildNotification(objItem);
                        if (n != null && !isExists(n)) {
                            store.add(n);
                            displayNotificationPopup(n);

                        }
                    }
                }
                if (total_unseen > NEW_NOTIFICATIONS_LIMIT) {
                    NotifyInfo.display(I18N.DISPLAY.newNotifications(),
                            I18N.DISPLAY.newNotificationsAlert());
                }
                store.sort(Notification.PROP_TIMESTAMP, SortDir.DESC);
                highlightNewNotifications();
                unmask();
            }
        }

    }

    private void trimStore(int size) {

    }

    private Notification buildNotification(JSONObject objItem) {
        String type;
        boolean seen;
        type = JsonUtil.getString(objItem, "type"); //$NON-NLS-1$
        seen = JsonUtil.getBoolean(objItem, "seen", false);
        Notification n = helper.buildNotification(type, seen, objItem);
        return n;
    }

    private boolean isExists(Notification n) {
        Notification temp = store.findModel("id", n.getId());
        if (temp == null) {
            return false;
        } else {
            return true;
        }

    }

    private void displayNotificationPopup(Notification n) {
        if (n.getCategory().equals(Category.DATA)) {
            NotifyInfo.display(Category.DATA.toString(), n.getMessage());
        } else if (n.getCategory().equals(Category.ANALYSIS)) {
            NotifyInfo.display(Category.ANALYSIS.toString(), n.getMessage());
        }
    }

    private class CustomListView<M extends ModelData> extends ListView<M> {
        private String emptyText;

        @Override
        protected void afterRender() {
            super.afterRender();

            applyEmptyText();
        }

        @Override
        public void refresh() {
            super.refresh();

            applyEmptyText();
        }

        protected void applyEmptyText() {
            if (emptyText == null) {
                emptyText = "&nbsp;";
            }
            if (store.getModels().size() == 0 && isRendered()) {
                el().setInnerHtml("<div class='x-grid-empty'>" + emptyText + "</div>");
            }
        }

        public void setEmptyText(String emptyText) {
            this.emptyText = emptyText;
        }

        @SuppressWarnings("unused")
        public String getEmptyText() {
            return emptyText;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onClick(ListViewEvent<M> e) {
            super.onClick(e);
            ListViewSelectionModel<M> selectionModel = getSelectionModel();
            Notification md = (Notification)selectionModel.getSelectedItem();
            selectionModel.deselectAll();
            selectionModel.select(false, (M)md);
        }

        public void highlight(int index, boolean highLight) {
            Element e = getElement(index);
            if (e != null) {
                if (highLight) {
                    fly(e).setStyleName("new_notification", highLight);
                    if (highLight && GXT.isAriaEnabled()) {
                        setAriaState("aria-activedescendant", e.getId());
                    }
                } else {
                    fly(e).removeStyleName("new_notification");
                }
            }
        }
    }

}
