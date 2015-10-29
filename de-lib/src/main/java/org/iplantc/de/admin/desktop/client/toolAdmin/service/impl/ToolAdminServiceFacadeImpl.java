package org.iplantc.de.admin.desktop.client.toolAdmin.service.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.*;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import java.util.List;

/**
 * Created by aramsey on 10/28/15.
 */


public class ToolAdminServiceFacadeImpl implements ToolAdminServiceFacade {

    private final String TOOLS = "org.iplantc.services.tools";
    private final String TOOLS_ADMIN = "org.iplantc.services.admin.tools";

    @Inject
    public ToolAdminServiceFacadeImpl(){}
    @Inject
    private ToolAutoBeanFactory factory;
    @Inject
    private DiscEnvApiService deService;

    @Override
    public void getTools(String searchTerm, AsyncCallback<List<Tool>> callback) {
        String address = TOOLS + "?search=" + URL.encodeQueryString(searchTerm);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new ToolListCallbackConverter(callback, factory));
    }

    @Override
    public void addTool(Tool tool, AsyncCallback<Tool> callback) {

    }

    @Override
    public void updateTool(Tool tool, AsyncCallback<Tool> callback) {

    }
}
