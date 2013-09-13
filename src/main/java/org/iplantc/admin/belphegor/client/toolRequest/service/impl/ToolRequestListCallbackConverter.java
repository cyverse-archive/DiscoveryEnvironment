package org.iplantc.admin.belphegor.client.toolRequest.service.impl;

import java.util.List;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ToolRequestListCallbackConverter extends AsyncCallbackConverter<String, List<ToolRequest>> {

    public ToolRequestListCallbackConverter(AsyncCallback<List<ToolRequest>> callback) {
        super(callback);
    }

    @Override
    protected List<ToolRequest> convertFrom(String object) {
        // TODO Auto-generated method stub
        return null;
    }

}
