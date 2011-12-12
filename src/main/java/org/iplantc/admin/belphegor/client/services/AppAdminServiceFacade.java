package org.iplantc.admin.belphegor.client.services;

import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppAdminServiceFacade implements AppTemplateServiceFacade {

    @Override
    public void favoriteAnalysis(String workspaceId, String analysisId, boolean fav,
            AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rateAnalysis(String analysisId, int rating, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteRating(String analysisId, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getAnalysis(String analysisGroupId, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getDataObjectsForAnalysis(String analysisId, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void publishWorkflow(String body, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

}
