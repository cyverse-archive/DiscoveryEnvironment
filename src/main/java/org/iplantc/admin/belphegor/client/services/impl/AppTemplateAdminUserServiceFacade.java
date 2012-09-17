package org.iplantc.admin.belphegor.client.services.impl;

import org.iplantc.admin.belphegor.client.Services;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is a dummy class to satisfy GWT deferred-binding. By design, the Belphegor admin module
 * does not require the methods defined by {@link AppTemplateUserServiceFacade}. See {@link Services} for
 * available services within this module.
 * 
 * @author jstroot
 * 
 */
public class AppTemplateAdminUserServiceFacade implements AppTemplateUserServiceFacade {

    @Override
    public void getAnalysis(String analysisGroupId, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void getPagedAnalysis(String analysisGroupId, int limit, String sortField, int offset,
            SortDir sortDir, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void searchAnalysis(String search, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void favoriteAnalysis(String workspaceId, String analysisId, boolean fav,
            AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void rateAnalysis(String analysisId, int rating, String appName, String comment,
            String authorEmail, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void updateRating(String analysisId, int rating, String appName, Long commentId,
            String comment, String authorEmail, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void deleteRating(String analysisId, String toolName, Long commentId,
            AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void getDataObjectsForAnalysis(String analysisId, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void publishWorkflow(String body, AsyncCallback<String> callback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void getTemplate(String tag, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void getDCDetails(String appId, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void analysisExportable(String id, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void editAnalysis(String id, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void copyAnalysis(String id, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void deleteAnalysisFromWorkspace(String username, String fullUsername, String id,
            AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

    @Override
    public void publishToWorld(JSONObject json, AsyncCallback<String> asyncCallback) {
        assert false : "Dummy Class to satisfy deferred-binding, this class not used in this module.";
    }

}
