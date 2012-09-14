/**
 * 
 */
package org.iplantc.de.client.gxt3.model;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 * 
 */
public interface NotificationList {
    @PropertyName("messages")
    List<Notification> getNotifications();
}
