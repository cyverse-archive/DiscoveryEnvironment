package org.iplantc.de.client.services;

import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Provides access to remote services for analyses management operations.
 */
public class AnalysisServiceFacade {
    /**
     * Get all the analyses for a given workspace.
     * 
     * @param workspaceId unique id for a user's workspace.
     * @param callback executed when RPC call completes.
     */
    public void getAnalyses(String workspaceId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "workspaces/" //$NON-NLS-1$
                + workspaceId + "/executions/list"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Delete an analysis execution
     * 
     * @param workspaceId unique id for a user's workspace.
     * @param json id of analysis to delete.
     * @param callback executed when RPC call completes.
     */
    public void deleteAnalysis(String workspaceId, String json, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "workspaces/" //$NON-NLS-1$
                + workspaceId + "/executions" + "/delete"; //$NON-NLS-1$ //$NON-NLS-2$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address, json);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Stop a currently running analysis
     * 
     * @param analysisId id of the analysis to be stopped.
     * @param callback executed when RPC call completes.
     */
    public void stopAnalysis(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "stop-analysis/"
                + analysisId;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void getAnalysisParams(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "get-property-values/" + analysisId;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Launch a wizard analysis
     * 
     * @param workspaceId unique id for a user's workspace.
     * @param json JSON configuration of analysis to launch.
     * @param callback executed when RPC call completes.
     */
    public void launchAnalysis(String workspaceId, String json, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "workspaces/" //$NON-NLS-1$
                + workspaceId + "/newexperiment"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address, json);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * get status updates for all running/submitted/ idle analyses
     * 
     * @param jsonBody
     * @param callback
     */
    public void getAnalysesStatus(String workspaceId, String jsonBody, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "workspaces/" //$NON-NLS-1$
                + workspaceId + "/executions/list"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                jsonBody);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

}