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
public interface Notification {

    @PropertyName("id")
    void setId(String id);

    @PropertyName("id")
    String getId();

    void setCategory(Category category);

    Category getCategory();

    @PropertyName("message")
    String getMessage();

    @PropertyName("message")
    void setMessage(String message);

    @PropertyName("context")
    String getContext();

    @PropertyName("context")
    void setContext(String context);

    @PropertyName("timestamp")
    void setTimeStamp(long date);

    @PropertyName("timestamp")
    long getTimeStamp();

}
