package org.iplantc.de.admin.desktop.client.toolAdmin.service.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;

import java.util.List;

/**
 * Created by aramsey on 10/28/15.
 */


public class ToolContainerCallbackConverter extends AsyncCallbackConverter<String, ToolContainer>{

    private final ToolAutoBeanFactory factory;

    public ToolContainerCallbackConverter(AsyncCallback<ToolContainer> callback, ToolAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected ToolContainer convertFrom(String object) {
        final AutoBean<ToolContainer> decode = AutoBeanCodex.decode(factory, ToolContainer.class, object);
        return decode.as().getToolContainer();
    }
}
