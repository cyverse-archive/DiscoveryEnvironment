package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemMessageCallbackConverter extends AsyncCallbackConverter<String, Message> {

    public SystemMessageCallbackConverter(AsyncCallback<Message> callback) {
        super(callback);
    }

    @Override
    protected Message convertFrom(String object) {
        // TODO Auto-generated method stub
        return null;
    }

}
