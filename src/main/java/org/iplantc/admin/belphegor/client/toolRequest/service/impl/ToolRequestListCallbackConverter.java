package org.iplantc.admin.belphegor.client.toolRequest.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.toolRequest.model.ToolRequestAdminAutoBeanFactory;
import org.iplantc.admin.belphegor.client.toolRequest.model.ToolRequestList;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class ToolRequestListCallbackConverter extends AsyncCallbackConverter<String, List<ToolRequest>> {

    private final ToolRequestAdminAutoBeanFactory factory;

    public ToolRequestListCallbackConverter(AsyncCallback<List<ToolRequest>> callback, ToolRequestAdminAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<ToolRequest> convertFrom(String object) {
        final AutoBean<ToolRequestList> decode = AutoBeanCodex.decode(factory, ToolRequestList.class, object);
        return decode.as().getToolRequests();
    }

}
