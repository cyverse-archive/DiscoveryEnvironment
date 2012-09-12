package org.iplantc.de.client.gxt3.model;

import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * 
 * Notification bean
 * 
 * @author sriram
 * 
 */
public interface NotificationMessage {

    @PropertyName("id")
    void setId(String id);

    @PropertyName("id")
    String getId();

    @PropertyName("text")
    String getMessage();

    @PropertyName("text")
    void setMessage(String message);

    @PropertyName("timestamp")
    void setTimestamp(long date);

    @PropertyName("timestamp")
    long getTimestamp();

    Category getCategory();

    void setCategory(Category type);

}
