package org.iplantc.de.client;

import java.util.List;

import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.utils.NotificationManager;

import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestNotificationManager extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    public void testSingletonStatus() {
        NotificationManager nm1 = NotificationManager.getInstance();
        NotificationManager nm2 = NotificationManager.getInstance();

        assertSame(nm1, nm2);
    }

    private List<Notification> getAllNotifications(NotificationManager notMgr) {
        ListStore<Notification> store = notMgr.getNotifications();
        assertNotNull(store);

        return store.getModels();
    }

    public void testAddingNotifications() {
        Notification n = new Notification("Something really bad happened."); //$NON-NLS-1$

        NotificationManager notMgr = NotificationManager.getInstance();

        ListStore<Notification> store = notMgr.getNotifications();
        assertNotNull(store);

        List<Notification> notList = getAllNotifications(notMgr);
        assertTrue(notList.isEmpty());

        notMgr.add(NotificationManager.Category.SYSTEM, n);
        notList = getAllNotifications(notMgr);
        assertTrue(notList.size() == 1);
    }
}
