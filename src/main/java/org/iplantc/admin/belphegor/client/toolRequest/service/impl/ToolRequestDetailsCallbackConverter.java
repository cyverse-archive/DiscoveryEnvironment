package org.iplantc.admin.belphegor.client.toolRequest.service.impl;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ToolRequestDetailsCallbackConverter extends AsyncCallbackConverter<String, ToolRequestDetails> {

    public ToolRequestDetailsCallbackConverter(AsyncCallback<ToolRequestDetails> callback) {
        super(callback);
    }

    @Override
    protected ToolRequestDetails convertFrom(String object) {
        // TODO Auto-generated method stub
        return null;
    }

}
