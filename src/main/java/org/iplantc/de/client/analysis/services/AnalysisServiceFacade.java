package org.iplantc.de.client.analysis.services;

import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;

/**
 * Provides access to remote services for analyses management operations.
 */
public class AnalysisServiceFacade {

    /**
     * Get all the analyses for a given workspace.
     * 
     * @param workspaceId unique id for a user's workspace.
     * @param pagingConfig optional remote paging and sorting configs.
     * @param callback executed when RPC call completes.
     */
    public void getAnalyses(String workspaceId, PagingLoadConfig pagingConfig,
            AsyncCallback<String> callback) {
        StringBuilder address = new StringBuilder(DEProperties.getInstance().getMuleServiceBaseUrl());

        address.append("workspaces/"); //$NON-NLS-1$
        address.append(workspaceId);
        address.append("/executions/list"); //$NON-NLS-1$

        if (pagingConfig != null) {
            address.append("?limit="); //$NON-NLS-1$
            address.append(pagingConfig.getLimit());

            address.append("&offset="); //$NON-NLS-1$
            address.append(pagingConfig.getOffset());

            // String sortField = pagingConfig.getSortField();
            // if (sortField != null && !sortField.isEmpty()) {
            //                address.append("&sort-field="); //$NON-NLS-1$
            // address.append(sortField);
            // }
            //
            // SortDir sortDir = pagingConfig.getSortDir();
            // if (sortDir == SortDir.ASC || sortDir == SortDir.DESC) {
            //                address.append("&sort-order="); //$NON-NLS-1$
            // address.append(sortDir.toString());
            // }
            //
            // List<FilterConfig> filters = pagingConfig.getFilterConfigs();
            // if (filters != null && !filters.isEmpty()) {
            //                address.append("&filter="); //$NON-NLS-1$
            //
            // JSONArray jsonFilters = new JSONArray();
            // int filterIndex = 0;
            // for (FilterConfig filter : filters) {
            // JSONObject jsonFilter = new JSONObject();
            //
            // jsonFilter.put("field", new JSONString(filter.getField()));
            // jsonFilter.put("value", new JSONString((String)filter.getValue()));
            //
            // jsonFilters.set(filterIndex++, jsonFilter);
            // }
            //
            // address.append(URL.encodeQueryString(jsonFilters.toString()));
            // }
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

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

}