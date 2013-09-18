package org.iplantc.admin.belphegor.client.systemMessage.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessageFactory;
import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.services.StringToVoidCallbackConverter;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SystemMessageServiceFacadeImpl implements SystemMessageServiceFacade {

    private final SystemMessageFactory factory;

    @Inject
    public SystemMessageServiceFacadeImpl(SystemMessageFactory factory) {
        this.factory = factory;
    }

    @Override
    public void getSystemMessages(AsyncCallback<List<Message>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageListCallbackConverter(callback, factory));
    }

    @Override
    public void addSystemMessage(Message msgToAdd, AsyncCallback<Message> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageCallbackConverter(callback));

    }

    @Override
    public void updateSystemMessage(Message updatedMsg, AsyncCallback<Message> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageCallbackConverter(callback));
    }

    @Override
    public void deleteSystemMessage(Message msgToDelete, AsyncCallback<Void> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void getSystemMessageTypes(AsyncCallback<List<String>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageTypesUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageTypeListCallbackConverter(callback, factory));
    }

}
