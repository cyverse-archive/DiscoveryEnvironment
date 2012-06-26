package org.iplantc.de.client;

import java.util.Date;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestNotification extends GWTTestCase {
    public void testNullString() {
        String param = null;
        Notification notification = new Notification(param);

        validateEmptyNotification(notification, false);
    }

    public void testNullJSONObject() {
        JSONObject param = null;
        Notification notification = new Notification(param);

        validateEmptyNotification(notification, false);
    }

    public void testInstantiateByString() {
        long createTime = System.currentTimeMillis();
        Notification notification = new Notification("foo bar"); //$NON-NLS-1$

        String test = notification.getId();
        assertNotNull(test);
        assertFalse(test.equals("")); //$NON-NLS-1$

        Category cat = notification.getCategory();
        assertNull(cat);

        test = notification.getMessage();
        assertNotNull(test);
        assertTrue(test.equals("foo bar")); //$NON-NLS-1$

        Date timestamp = notification.getTimestamp();
        assertNotNull(timestamp);
        assertTrue(Math.abs(System.currentTimeMillis() - createTime) < 1000); // check that the timestamp
                                                                              // roughly equals the time
                                                                              // when the Notification
                                                                              // was created
    }

    public void testInstantiateByEmptyJsonObject() {
        String json = "{}"; //$NON-NLS-1$
        JSONObject jsonObj = JsonUtil.getObject(json);

        Notification notification = new Notification(jsonObj);

        validateEmptyNotification(notification, false);
    }

    public void testInstantiateByFullJsonObject() {
        Date origTimestamp = new Date(1291530527000L);
        String json = "{\"id\": \"someId\", \"category\": \"data\", \"text\": \"here's a message\"," //$NON-NLS-1$
                + "\"timestamp\": \"" + Notification.TIMESTAMP_FORMAT.format(origTimestamp) //$NON-NLS-1$
                + " (MST)\"}"; //$NON-NLS-1$
        JSONObject jsonObj = JsonUtil.getObject(json);

        long createTime = System.currentTimeMillis();
        Notification notification = new Notification(jsonObj);

        String test = notification.getId();
        assertNotNull(test);
        assertTrue(test.equals("someId")); //$NON-NLS-1$

        Category cat = notification.getCategory();
        assertNotNull(cat);
        assertTrue(cat.equals(Category.DATA));

        test = notification.getMessage();
        assertNotNull(test);
        assertTrue(test.equals("here's a message")); //$NON-NLS-1$

        Date timestamp = notification.getTimestamp();
        assertNotNull(timestamp);
        assertTrue(Math.abs(System.currentTimeMillis() - createTime) < 1000); // check that the timestamp
                                                                              // roughly equals the time
                                                                              // when the Notification
                                                                              // was created
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    private void validateEmptyNotification(final Notification notification, boolean checkTimestampNull) {
        String test = notification.getId();
        assertNotNull(test);
        assertTrue(test.equals("")); //$NON-NLS-1$

        Category cat = notification.getCategory();
        assertNull(cat);

        test = notification.getMessage();
        assertNotNull(test);
        assertTrue(test.equals("")); //$NON-NLS-1$

        if (checkTimestampNull) {
            assertNull(notification.getTimestamp());
        } else {
            assertNotNull(notification.getTimestamp());
        }
    }
}
