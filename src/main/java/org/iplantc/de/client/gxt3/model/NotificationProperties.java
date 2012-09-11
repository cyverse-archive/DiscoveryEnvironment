/**
 * 
 */
package org.iplantc.de.client.gxt3.model;

import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface NotificationProperties extends PropertyAccess<Notification> {

    ValueProvider<Notification, Category> category();

    ValueProvider<Notification, String> message();

    ValueProvider<Notification, Long> timestamp();

}
