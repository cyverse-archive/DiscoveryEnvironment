package org.iplantc.de.client.gxt3.model;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface Notification {

    @PropertyName("seen")
    void setSeen(boolean seen);

    @PropertyName("seen")
    boolean isSeen();

    @PropertyName("message")
    void setMessage(NotificationMessage message);

    @PropertyName("message")
    NotificationMessage getMessage();

    @PropertyName("type")
    void setCategory(String category);

    @PropertyName("type")
    String getCategory();

}
