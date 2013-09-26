package org.iplantc.admin.belphegor.client.toolRequest.model;

import java.util.List;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface ToolRequestList {

    @PropertyName("tool_requests")
    List<ToolRequest> getToolRequests();

}
