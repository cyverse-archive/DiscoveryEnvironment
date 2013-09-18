package org.iplantc.admin.belphegor.client.systemMessage.model;

import java.util.List;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface SystemMessageList {

    @PropertyName("system-messages")
    List<Message> getSystemMessages();

}
