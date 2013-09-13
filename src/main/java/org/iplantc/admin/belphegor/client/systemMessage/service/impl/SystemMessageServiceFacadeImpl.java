package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.services.StringToVoidCallbackConverter;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemMessageServiceFacadeImpl implements SystemMessageServiceFacade {

    @Override
    public void getSystemMessages(AsyncCallback<List<Message>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, new SystemMessageListCallbackConverter(callback));

    }

    @Override
    public void addSystemMessage(Message msgToAdd, AsyncCallback<Message> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        callService(wrapper, new SystemMessageCallbackConverter(callback));

    }

    @Override
    public void updateSystemMessage(Message updatedMsg, AsyncCallback<Message> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address);
        callService(wrapper, new SystemMessageCallbackConverter(callback));

    }

    @Override
    public void deleteSystemMessage(Message msgToDelete, AsyncCallback<Void> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        callService(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void getSystemMessageTypes(AsyncCallback<List<String>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageTypesUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, new SystemMessageTypeListCallbackConverter(callback));
    }

    /**
     * Performs the actual service call.
     * 
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(ServiceCallWrapper wrapper, AsyncCallback<String> callback) {
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

}
