package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemMessageTypeListCallbackConverter extends AsyncCallbackConverter<String, List<String>> {

    public SystemMessageTypeListCallbackConverter(AsyncCallback<List<String>> callback) {
        super(callback);
    }

    @Override
    protected List<String> convertFrom(String object) {
        // TODO Auto-generated method stub
        return null;
    }

}
