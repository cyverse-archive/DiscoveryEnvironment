/**
 * 
 */
package org.iplantc.de.client.gxt3.views;

import java.util.List;

import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * @author sriram
 * 
 */
public class NotificationViewImpl implements NotificationView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("NotificationView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, NotificationViewImpl> {
    }

    @UiField(provided = true)
    final ListStore<Notification> listStore;
    @UiField(provided = true)
    final ColumnModel<Notification> cm;
    @UiField
    GridView gridView;

    @UiField
    Grid<Notification> grid;

    @UiField
    FramedPanel mainPanel;

    @UiField
    BorderLayoutContainer con;

    private final Widget widget;
    private Presenter presenter;

    public NotificationViewImpl(ListStore<Notification> listStore, ColumnModel<Notification> cm) {
        this.widget = uiBinder.createAndBindUi(this);
        this.cm = cm;
        this.listStore = listStore;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return widget;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#filterBy(org.iplantc.de.client.utils.
     * NotificationHelper.Category)
     */
    @Override
    public void filterBy(Category category) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getCurrentFilter()
     */
    @Override
    public Category getCurrentFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getCurrentSortDir()
     */
    @Override
    public SortDir getCurrentSortDir() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getCurrentOffset()
     */
    @Override
    public int getCurrentOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getSelectedItems()
     */
    @Override
    public List<Notification> getSelectedItems() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.gxt3.views.NotificationView#setPresenter(org.iplantc.de.client.gxt3.views
     * .NotificationView.Presenter)
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getListStore()
     */
    @Override
    public ListStore<Notification> getListStore() {
        return listStore;
    }

    @Override
    public void setNotifications(List<Notification> notifications) {
        listStore.clear();
        listStore.addAll(notifications);

    }

}
