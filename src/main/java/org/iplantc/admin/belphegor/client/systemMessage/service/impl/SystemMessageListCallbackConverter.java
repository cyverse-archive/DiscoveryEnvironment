package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemMessageListCallbackConverter extends AsyncCallbackConverter<String, List<Message>> {

    public SystemMessageListCallbackConverter(AsyncCallback<List<Message>> callback) {
        super(callback);
    }

    @Override
    protected List<Message> convertFrom(String object) {
        // TODO Auto-generated method stub
        return null;
    }

}
