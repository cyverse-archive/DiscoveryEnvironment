package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessage;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageFactory;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class SystemMessageCallbackConverter extends AsyncCallbackConverter<String, SystemMessage> {

    private final SystemMessageFactory factory;

    public SystemMessageCallbackConverter(AsyncCallback<SystemMessage> callback, SystemMessageFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected SystemMessage convertFrom(String object) {
        Splittable split = StringQuoter.split(object);
        SystemMessage ret = null;
        if (!split.isUndefined("system-notification")) {
            final AutoBean<SystemMessage> decode = AutoBeanCodex.decode(factory, SystemMessage.class, split.get("system-notification"));
            ret = decode.as();
        }

        return ret;
    }

}
