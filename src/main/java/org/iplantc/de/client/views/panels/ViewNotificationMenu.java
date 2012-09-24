/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.notifications.views.NotificationListView;

/**
 * @author sriram
 * 
 */
public class ViewNotificationMenu extends com.sencha.gxt.widget.core.client.menu.Menu {

    private NotificationListView view;

    public ViewNotificationMenu() {
        view = new NotificationListView();
        add(view.asWidget());
    }

    @Override
    public void showAt(int x, int y) {
        super.showAt(x, y);
        view.highlightNewNotifications();
        view.markAsSeen();
    }

    //
    // /**
    // *
    // * persist total notification count
    // *
    // * @param total
    // */
    // public void setTotalNotificationCount(int total) {
    // totalNotificationCount = total;
    // }
    //
    // /**
    // * get total notification count
    // *
    // * @return
    // */
    // public int getTotalNotificationCount() {
    // return totalNotificationCount;
    // }
    //
    // private Notification addItemToStore(final Category category, final JSONObject objMessage,
    // final String context, JSONObject payload) {
    // Notification ret = null; // assume failure
    //
    // if (objMessage != null) {
    // ret = new Notification(objMessage, context);
    //
    // Notification model = store.findModel("id", ret.get("id"));
    //
    // if (model == null) {
    // add(category, ret);
    // totalNotificationCount = totalNotificationCount + 1;
    // fireEvents(category, payload);
    // } else {
    // ret = null;
    // }
    // }
    // return ret;
    // }
    //
    // private void fireEvents(final Category category, JSONObject payload) {
    // NotificationCountUpdateEvent ncue = new NotificationCountUpdateEvent(getTotalNotificationCount());
    // EventBus instance = EventBus.getInstance();
    // instance.fireEvent(ncue);
    // if (category.equals(NotificationHelper.Category.ANALYSIS)) {
    // AnalysisUpdateEvent aue = new AnalysisUpdateEvent(payload);
    // instance.fireEvent(aue);
    // }
    // }
    //
    // /**
    // * reset all notification count
    // *
    // */
    // public void resetCount() {
    // totalNotificationCount = 0;
    // }
    //
    // private void addFromEventHandler(final Category category, final String header,
    // final JSONObject objMessage, final String context, final JSONObject payload) {
    // Notification notification = addItemToStore(category, objMessage, context, payload);
    //
    // if (notification != null) {
    // NotifyInfo.display(header, notification.getMessage());
    // }
    // }
    //
    // public void add(final Category category, final Notification notification) {
    // // did we get a valid notification?
    // if (category != Category.ALL && notification != null) {
    // notification.setCategory(category);
    // store.add(notification);
    // }
    //
    // store.sort(Notification.PROP_TIMESTAMP, SortDir.DESC);
    // }
    //
    // private String getTemplate() {
    // StringBuilder template = new StringBuilder();
    //        template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$
    // template.append("<tpl if=\"context\"> <div class='notification_context'> </tpl>");
    // template.append("{message} <tpl if=\"context\"> </div> </tpl></div></tpl>");
    // return template.toString();
    // }
    //
    // private void highlightNewNotifications() {
    // List<Notification> new_notifications = store.getModels();
    // for (Notification n : new_notifications) {
    // if (n.get(Notification.SEEN) == null
    // || Boolean.parseBoolean(n.get(Notification.SEEN).toString()) == false) {
    // view.highlight(view.getStore().indexOf(n), true);
    // } else {
    // view.highlight(view.getStore().indexOf(n), false);
    // }
    //
    // }
    // }
    //
    // private class CustomListView<M extends ModelData> extends ListView<M> {
    // private String emptyText;
    //
    // @Override
    // protected void afterRender() {
    // super.afterRender();
    //
    // applyEmptyText();
    // }
    //
    // @Override
    // public void refresh() {
    // super.refresh();
    //
    // applyEmptyText();
    // }
    //
    // protected void applyEmptyText() {
    // if (emptyText == null) {
    // emptyText = "&nbsp;";
    // }
    // if (store.getModels().size() == 0) {
    // el().setInnerHtml("<div class='x-grid-empty'>" + emptyText + "</div>");
    // }
    // }
    //
    // public void setEmptyText(String emptyText) {
    // this.emptyText = emptyText;
    // }
    //
    // @SuppressWarnings("unused")
    // public String getEmptyText() {
    // return emptyText;
    // }
    //
    // @SuppressWarnings("unchecked")
    // @Override
    // protected void onClick(ListViewEvent<M> e) {
    // super.onClick(e);
    // ListViewSelectionModel<M> selectionModel = getSelectionModel();
    // Notification md = (Notification)selectionModel.getSelectedItem();
    // selectionModel.deselectAll();
    // selectionModel.select(false, (M)md);
    // }
    //
    // public void highlight(int index, boolean highLight) {
    // Element e = getElement(index);
    // if (e != null) {
    // if (highLight) {
    // fly(e).setStyleName("new_notification", highLight);
    // if (highLight && GXT.isAriaEnabled()) {
    // setAriaState("aria-activedescendant", e.getId());
    // }
    // } else {
    // fly(e).removeStyleName("new_notification");
    // }
    // }
    // }
    // }

}
