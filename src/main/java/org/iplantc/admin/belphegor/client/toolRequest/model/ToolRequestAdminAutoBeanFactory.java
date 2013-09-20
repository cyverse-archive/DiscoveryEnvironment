package org.iplantc.admin.belphegor.client.toolRequest.model;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestAutoBeanFactory;

import com.google.web.bindery.autobean.shared.AutoBean;

public interface ToolRequestAdminAutoBeanFactory extends ToolRequestAutoBeanFactory {

    AutoBean<ToolRequestList> toolRequestList();

}
