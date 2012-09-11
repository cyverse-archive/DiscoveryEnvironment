/**
 * 
 */
package org.iplantc.de.client.views.windows;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.gxt3.model.NotificationProperties;
import org.iplantc.de.client.gxt3.presenter.NotificationPresenter;
import org.iplantc.de.client.gxt3.views.NotificationView;
import org.iplantc.de.client.gxt3.views.NotificationView.Presenter;
import org.iplantc.de.client.gxt3.views.NotificationViewImpl;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 * 
 */
public class NotificationWindow3 extends Gxt3IplantWindow {

    public NotificationWindow3(String tag, WindowConfig config) {
        super(tag, config);
        NotificationKeyProvider keyProvider = new NotificationKeyProvider();
        ListStore<Notification> store = new ListStore<Notification>(keyProvider);
        ColumnModel<Notification> cm = buildNotificationColumnModel();
        NotificationView view = new NotificationViewImpl(store, cm);
        Presenter presenter = new NotificationPresenter(view, new MessageServiceFacade());
        setSize("800", "410");
        presenter.go(this);
    }

    @SuppressWarnings("unchecked")
    private static ColumnModel<Notification> buildNotificationColumnModel() {
        NotificationProperties props = GWT.create(NotificationProperties.class);
        List<ColumnConfig<Notification, ?>> configs = new LinkedList<ColumnConfig<Notification, ?>>();

        CheckBoxSelectionModel<Notification> checkBoxModel = new CheckBoxSelectionModel<Notification>(
                new IdentityValueProvider<Notification>());
        @SuppressWarnings("rawtypes")
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        configs.add(colCheckBox);

        ColumnConfig<Notification, Category> colCategory = new ColumnConfig<Notification, Category>(
                props.category(), 100);
        colCategory.setHeader(I18N.CONSTANT.category());
        configs.add(colCategory);
        colCategory.setMenuDisabled(true);
        colCategory.setSortable(false);

        ColumnConfig<Notification, String> colMessage = new ColumnConfig<Notification, String>(
                props.message(), 420);
        colMessage.setHeader(I18N.DISPLAY.messagesGridHeader());
        configs.add(colMessage);
        colMessage.setSortable(false);
        colMessage.setMenuDisabled(true);

        ColumnConfig<Notification, Long> colTimestamp = new ColumnConfig<Notification, Long>(
                props.timestamp(), 170);
        colTimestamp.setHeader(I18N.DISPLAY.createdDateGridHeader());

        configs.add(colTimestamp);
        ColumnModel<Notification> columnModel = new ColumnModel<Notification>(configs);
        return columnModel;
    }

    private class NotificationKeyProvider implements ModelKeyProvider<Notification> {

        @Override
        public String getKey(Notification item) {
            return item.getId();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.views.windows.IPlantWindowInterface#getWindowState()
     */
    @Override
    public JSONObject getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }

}
