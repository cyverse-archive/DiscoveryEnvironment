package org.iplantc.admin.belphegor.client.systemMessage.model;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface SystemMessageTypesList {

    @PropertyName("types")
    List<String> getSystemMessageTypes();
}
