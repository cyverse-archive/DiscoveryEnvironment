package org.iplantc.de.client.notifications.models;


import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface NotificationAutoBeanFactory extends AutoBeanFactory {
    AutoBean<NotificationMessage> getNotificationMessage();

    AutoBean<NotificationList> getNotificationList();

    AutoBean<Notification> getNotification();

    AutoBean<NotificationPayload> getNotificationPayload();

}
