package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessage;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageFactory;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class SystemMessageListCallbackConverter extends AsyncCallbackConverter<String, List<SystemMessage>> {

    private final SystemMessageFactory factory;

    public SystemMessageListCallbackConverter(AsyncCallback<List<SystemMessage>> callback, SystemMessageFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<SystemMessage> convertFrom(String object) {
        final AutoBean<SystemMessageList> decode = AutoBeanCodex.decode(factory, SystemMessageList.class, object);
        return decode.as().getSystemMessages();
    }

}
