package org.iplantc.admin.belphegor.client.toolRequest.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.admin.belphegor.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.SortInfo;

public class ToolRequestServiceFacadeImpl implements ToolRequestServiceFacade {

    @Override
    public void getToolRequestDetails(HasId toolRequest, AsyncCallback<ToolRequestDetails> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getToolRequestServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, new ToolRequestDetailsCallbackConverter(callback));
    }

    @Override
    public void updateToolRequest(ToolRequestUpdate trUpdate, AsyncCallback<ToolRequestDetails> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getToolRequestServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address);
        callService(wrapper, new ToolRequestDetailsCallbackConverter(callback));
    }

    @Override
    public void getToolRequests(SortInfo sortInfo, String userName, AsyncCallback<List<ToolRequest>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getListToolRequestsServiceUrl();
        // TODO Pending rest of endpoint setup. Waiting for generic get all tool requests endpoint.

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, new ToolRequestListCallbackConverter(callback));
    }

    /**
     * Performs the actual service call, masking any calling component.
     * 
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(ServiceCallWrapper wrapper, AsyncCallback<String> callback) {
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

}
