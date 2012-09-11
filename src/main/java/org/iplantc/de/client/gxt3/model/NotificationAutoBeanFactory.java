package org.iplantc.de.client.gxt3.model;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface NotificationAutoBeanFactory extends AutoBeanFactory {
    AutoBean<Notification> getNotification();

    AutoBean<NotificationList> getNotificationList();
}
