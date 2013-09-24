package org.iplantc.admin.belphegor.client.systemMessage.model;

import org.iplantc.core.uicommons.client.models.sysmsgs.MessageFactory;

import com.google.web.bindery.autobean.shared.AutoBean;

public interface SystemMessageFactory extends MessageFactory {

    AutoBean<SystemMessageList> systemMessageList();

    AutoBean<SystemMessageTypesList> systemMessageTypesList();

    AutoBean<SystemMessage> systemMessage();

}
