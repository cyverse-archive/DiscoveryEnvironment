/**
 * 
 */
package org.iplantc.de.client.notifications.models;

import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface NotificationMessageProperties extends PropertyAccess<NotificationMessage> {

    ValueProvider<NotificationMessage, Category> category();

    ValueProvider<NotificationMessage, String> message();

    ValueProvider<NotificationMessage, Long> timestamp();

}
