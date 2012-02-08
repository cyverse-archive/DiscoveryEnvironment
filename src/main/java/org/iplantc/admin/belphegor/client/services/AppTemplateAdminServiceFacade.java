package org.iplantc.admin.belphegor.client.services;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.de.client.I18N;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppTemplateAdminServiceFacade implements AppTemplateServiceFacade {
    private final Component maskingCaller;

    public AppTemplateAdminServiceFacade() {
        this(null);
    }

    public AppTemplateAdminServiceFacade(Component maskingCaller) {
        this.maskingCaller = maskingCaller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getCategoryListServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAnalysis(String analysisGroupId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAppsInCategoryServiceUrl()
                + "/" + analysisGroupId; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void searchAnalysis(String search, AsyncCallback<String> callback) {
        // FIXME integrate with a Conrad service
        callback.onFailure(new Exception("Remote Search not implemented yet."));
        // String address = ToolIntegrationAdminProperties.getInstance().getSearchAppServiceUrl() + "/" //$NON-NLS-1$
        // + URL.encode(search);
        //
        // ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        // callService(wrapper, callback);
    }

    /**
     * Adds a new Category with the given category name.
     * 
     * @param name
     * @param destCategoryId
     * @param callback
     */
    public void addCategory(String name, String destCategoryId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAddCategoryServiceUrl();

        JSONObject body = new JSONObject();
        body.put("parentCategoryId", new JSONString(destCategoryId)); //$NON-NLS-1$
        body.put("name", new JSONString(name)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address,
                body.toString());
        callService(wrapper, callback);
    }

    /**
     * Renames a Category with the given category ID to the given name.
     * 
     * @param categoryId
     * @param name
     * @param callback
     */
    public void renameCategory(String categoryId, String name, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getRenameCategoryServiceUrl();

        JSONObject body = new JSONObject();
        body.put("categoryId", new JSONString(categoryId)); //$NON-NLS-1$
        body.put("name", new JSONString(name)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(wrapper, callback);
    }

    /**
     * Moves a Category with the given category ID to a parent Category with the given parentCategoryId.
     * 
     * @param categoryId
     * @param parentCategoryId
     * @param callback
     */
    public void moveCategory(String categoryId, String parentCategoryId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getMoveCategoryServiceUrl();

        JSONObject body = new JSONObject();
        body.put("categoryId", new JSONString(categoryId)); //$NON-NLS-1$
        body.put("parentCategoryId", new JSONString(parentCategoryId)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(wrapper, callback);
    }

    /**
     * Deletes the Category with the given category ID.
     * 
     * @param categoryId
     * @param callback
     */
    public void deleteCategory(String categoryId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getDeleteCategoryServiceUrl()
                + "/" + categoryId; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        callService(wrapper, callback);
    }

    /**
     * Updates an app with the given values in application.
     * 
     * @param application
     * @param callback
     */
    public void updateApplication(JSONObject application, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getUpdateAppServiceUrl();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                application.toString());
        callService(wrapper, callback);
    }

    /**
     * Moves an App with the given applicationId to the category with the given groupId.
     * 
     * @param applicationId
     * @param groupId
     * @param callback
     */
    public void moveApplication(String applicationId, String groupId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getMoveAppServiceUrl();

        JSONObject body = new JSONObject();
        body.put("id", new JSONString(applicationId)); //$NON-NLS-1$
        body.put("categoryId", new JSONString(groupId)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(wrapper, callback);
    }

    /**
     * Deletes an App with the given applicationId.
     * 
     * @param applicationId
     * @param callback
     */
    public void deleteApplication(String applicationId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getDeleteAppServiceUrl() + "/" //$NON-NLS-1$
                + applicationId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.DELETE, address);
        callService(wrapper, callback);
    }

    /**
     * Deletes an App with the given applicationId.
     * 
     * @param applicationId
     * @param callback
     */
    public void restoreApplication(String applicationId, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getRestoreAppServiceUrl() + "/" //$NON-NLS-1$
                + applicationId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, callback);
    }

    /**
     * Performs the actual service call, masking any calling component.
     * 
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(ServiceCallWrapper wrapper, AsyncCallback<String> callback) {
        if (callback instanceof AdminServiceCallback) {
            ((AdminServiceCallback)callback).setMaskedCaller(maskingCaller);
        }

        if (maskingCaller != null) {
            maskingCaller.mask(I18N.DISPLAY.loadingMask());
        }

        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}