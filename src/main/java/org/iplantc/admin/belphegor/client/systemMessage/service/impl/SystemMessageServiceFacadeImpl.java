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
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

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
        Splittable body = StringQuoter.createSplittable();
        StringQuoter.create(msgToAdd.getType()).assign(body, "type");
        StringQuoter.create(msgToAdd.getBody()).assign(body, "message");
        final Long deActTime = msgToAdd.getDeactivationTime().getTime();
        final Long actTime = msgToAdd.getActivationTime().getTime();
        StringQuoter.create(deActTime).assign(body, "deactivation-date");
        StringQuoter.create(actTime).assign(body, "activation-date");
        StringQuoter.create(msgToAdd.isDismissible()).assign(body, "dismissible");
        StringQuoter.create(msgToAdd.isLoginsDisabled()).assign(body, "logins-disabled");

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(msgToAdd));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address, encode.getPayload());
        // ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address,
        // body.getPayload());
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageCallbackConverter(callback, factory));
    }

    @Override
    public void updateSystemMessage(Message updatedMsg, AsyncCallback<Message> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl();
        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(updatedMsg));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, encode.getPayload());
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new SystemMessageCallbackConverter(callback, factory));
    }

    @Override
    public void deleteSystemMessage(Message msgToDelete, AsyncCallback<Void> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAdminSystemMessageServiceUrl() + "/" + msgToDelete.getId();

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
