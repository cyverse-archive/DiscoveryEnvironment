package org.iplantc.admin.belphegor.client.services;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppTemplateAdminServiceFacade implements AppTemplateServiceFacade {

    public void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "get-only-analysis-groups" + "/1"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAnalysis(String analysisGroupId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getMuleServiceBaseUrl()
                + "get-analyses-in-group/" //$NON-NLS-1$
                + analysisGroupId;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

}