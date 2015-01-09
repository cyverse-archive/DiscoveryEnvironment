/**
 * 
 */
package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface NotificationMessageProperties extends PropertyAccess<NotificationMessage> {

    ValueProvider<NotificationMessage, NotificationCategory> category();

    ValueProvider<NotificationMessage, String> message();

    ValueProvider<NotificationMessage, Long> timestamp();

}
