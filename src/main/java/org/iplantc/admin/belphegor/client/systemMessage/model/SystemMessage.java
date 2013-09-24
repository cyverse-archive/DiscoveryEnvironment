package org.iplantc.admin.belphegor.client.systemMessage.model;

import java.util.Date;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface SystemMessage extends Message {

    @PropertyName("date_created")
    void setCreationTime(Date creationTime);

    @PropertyName("activation_date")
    void setActivationTime(Date activationTime);

    @PropertyName("deactivation_date")
    void setDeactivationTime(Date deactivationTime);

    void setType(String type);

    @PropertyName("message")
    void setBody(String body);

    void setDismissible(boolean dismissible);
}
