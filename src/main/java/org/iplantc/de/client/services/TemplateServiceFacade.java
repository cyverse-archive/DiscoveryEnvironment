package org.iplantc.de.client.services;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;
import org.iplantc.de.shared.services.EmailServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Provides access to remote services for operations related to analysis submission templates.
 * 
 * @author Dennis Roberts
 */
public class TemplateServiceFacade implements AppTemplateUserServiceFacade {
    /**
     * Retrieves a template from the database.
     * 
     * @param templateId unique identifier for the template.
     * @param callback called when the RPC call is complete.
     */
    public void getTemplate(String templateId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "template/" + templateId; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "get-only-analysis-groups/" + workspaceId; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAnalysis(String analysisGroupId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "get-analyses-in-group/" //$NON-NLS-1$
                + analysisGroupId;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * 
     * @param appId
     * @param callback
     */
    public void getDCDetails(String appId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "get-components-in-analysis/" + appId; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getDataObjectsForAnalysis(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "analysis-data-objects/" + analysisId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Adds an app to the given public categories.
     * 
     * @param application
     * @param callback
     */
    public void publishToWorld(JSONObject application, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "make-analysis-public"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                application.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void rateAnalysis(final String analysisId, final int rating, final String appName,
            String comment, final String authorEmail, final AsyncCallback<String> callback) {
        // add comment to wiki page, then call rating service, then update avg on wiki page
        final ConfluenceServiceFacade confluenceService = ConfluenceServiceFacade.getInstance();
        String username = UserInfo.getInstance().getUsername();
        confluenceService.addComment(appName, rating, username, comment, new AsyncCallback<String>() {
            @Override
            public void onSuccess(final String commentId) {
                // wrap the callback so it returns the comment id on success
                rateAnalysis(appName, analysisId, rating, commentId, authorEmail,
                        new AsyncCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                callback.onSuccess(commentId);
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                callback.onFailure(caught);
                            }
                        });
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    /** calls /rate-analysis and if that is successful, calls updateDocumentationPage() */
    private void rateAnalysis(final String appName, String analysisId, int rating,
            final String commentId, final String authorEmail, final AsyncCallback<String> callback) {
        JSONObject body = new JSONObject();
        body.put("analysis_id", new JSONString(analysisId)); //$NON-NLS-1$
        body.put("rating", new JSONNumber(rating)); //$NON-NLS-1$
        body.put("comment_id", new JSONNumber(Long.valueOf(commentId))); //$NON-NLS-1$

        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "rate-analysis"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                sendRatingEmail(appName, authorEmail);
                updateDocumentationPage(appName, result);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    private void sendRatingEmail(final String appName, final String emailAddress) {
        EmailServiceFacade.getInstance().sendEmail(I18N.DISPLAY.ratingEmailSubject(appName),
                I18N.DISPLAY.ratingEmailText(appName), "noreply@iplantcollaborative.org", emailAddress, //$NON-NLS-1$
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String arg0) {
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        // don't bother the user if email sending fails
                    }
                });
    }

    private void updateDocumentationPage(String appName, String avgJson) {
        JSONObject json = JSONParser.parseStrict(avgJson).isObject();
        if (json != null) {
            Number avg = JsonUtil.getNumber(json, "avg"); //$NON-NLS-1$
            int avgRounded = (int)Math.round(avg.doubleValue());
            ConfluenceServiceFacade.getInstance().updateDocumentationPage(appName, avgRounded,
                    new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(I18N.ERROR.confluenceError(), caught);

                        }

                        @Override
                        public void onSuccess(String result) {
                            // Do nothing intentionally

                        }
                    });
        }
    }

    @Override
    public void updateRating(final String analysisId, final int rating, final String appName,
            final Long commentId, final String comment, final String authorEmail,
            final AsyncCallback<String> callback) {
        // update comment on wiki page, then call rating service, then update avg on wiki page
        String username = UserInfo.getInstance().getUsername();
        ConfluenceServiceFacade.getInstance().editComment(appName, rating, username, commentId, comment,
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        rateAnalysis(appName, analysisId, rating, String.valueOf(commentId),
                                authorEmail, callback);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }
                });
    }

    @Override
    public void deleteRating(final String analysisId, final String toolName, final Long commentId,
            final AsyncCallback<String> callback) {
        // call rating service, then delete comment from wiki page
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "delete-rating"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("analysis_id", new JSONString(analysisId)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                updateDocumentationPage(toolName, result);
                if (commentId != null) {
                    try {
                        removeComment(toolName, commentId);
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }

                callback.onSuccess(result);

            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    private void removeComment(String toolName, long commentId) {
        ConfluenceServiceFacade.getInstance().removeComment(toolName, commentId,
                new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.confluenceError(), caught);

                    }

                    @Override
                    public void onSuccess(String result) {
                        // Do nothing intentionally

                    }
                });
    }

    @Override
    public void favoriteAnalysis(String workspaceId, String analysisId, boolean fav,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "update-favorites";

        JSONObject body = new JSONObject();
        body.put("workspace_id", new JSONString(workspaceId));
        body.put("analysis_id", new JSONString(analysisId));
        body.put("user_favorite", JSONBoolean.getInstance(fav));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Checks if the given analysisId is able to be exported to TITo via a copy or edit. The service will
     * respond with JSON that contains a boolean "can-export" key, and a "cause" key if "can-export" is
     * false:
     * 
     * <code>
     * { "can-export": false, "cause": "Analysis has multiple templates." }
     * </code>
     * 
     * @param analysisId
     * @param callback
     */
    public void analysisExportable(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
                + "can-export-analysis";

        JSONObject body = new JSONObject();
        body.put("analysis_id", new JSONString(analysisId));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void editAnalysis(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "edit-template/"
                + analysisId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void copyAnalysis(String analysisId, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "copy-template/"
                + analysisId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void deleteAnalysisFromWorkspace(String user, String email, String analysisId,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() + "delete-workflow";

        JSONObject body = new JSONObject();
        body.put("analysis_id", new JSONString(analysisId));
        body.put("user", new JSONString(user));
        body.put("email", new JSONString(email));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void searchAnalysis(String search, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "search-analyses?search=" //$NON-NLS-1$
                + URL.encodeQueryString(search);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishWorkflow(String body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() + "update-workflow";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
