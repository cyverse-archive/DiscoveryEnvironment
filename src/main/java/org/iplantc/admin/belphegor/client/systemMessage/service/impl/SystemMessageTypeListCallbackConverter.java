package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageFactory;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageTypesList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class SystemMessageTypeListCallbackConverter extends AsyncCallbackConverter<String, List<String>> {

    private final SystemMessageFactory factory;

    public SystemMessageTypeListCallbackConverter(AsyncCallback<List<String>> callback, SystemMessageFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<String> convertFrom(String object) {
        final AutoBean<SystemMessageTypesList> decode = AutoBeanCodex.decode(factory, SystemMessageTypesList.class, object);
        return decode.as().getSystemMessageTypes();
    }

}
