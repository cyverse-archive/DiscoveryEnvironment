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

    public void testEnforcingMaxNotificationsLimit() {
        final NotificationManager notMgr = NotificationManager.getInstance();

        NotificationManager.MAX_NOTIFICATIONS = 5;

        for (int i = 0; i < NotificationManager.MAX_NOTIFICATIONS; i++) {
            Notification n = new Notification("Job" + i + " failed to be created."); //$NON-NLS-1$ //$NON-NLS-2$

            notMgr.add(NotificationManager.Category.SYSTEM, n);

            long startTime = System.currentTimeMillis() / 1000;

            while (System.currentTimeMillis() / 1000 == startTime) {
                // we are just using this as a low-fi alternative to sleep()
            }
        }

        List<Notification> notList = getAllNotifications(notMgr);
        assertNotNull(notList);
        // only want to enforce the limit of notifications.
        assertTrue(notList.size() >= NotificationManager.MAX_NOTIFICATIONS);

        notMgr.add(NotificationManager.Category.SYSTEM, new Notification("Job" //$NON-NLS-1$
                + NotificationManager.MAX_NOTIFICATIONS + " failed to be created.")); //$NON-NLS-1$

        notList = getAllNotifications(notMgr);

        assertTrue(notList.size() == NotificationManager.MAX_NOTIFICATIONS);

        for (int i = 0; i < NotificationManager.MAX_NOTIFICATIONS; i++) {
            assertEquals("Job" + (NotificationManager.MAX_NOTIFICATIONS - i) + " failed to be created.", //$NON-NLS-1$ //$NON-NLS-2$
                    notList.get(i).getMessage());
        }
    }
}
