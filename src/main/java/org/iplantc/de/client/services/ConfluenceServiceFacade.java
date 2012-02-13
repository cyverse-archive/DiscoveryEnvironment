package org.iplantc.de.client.services;

import org.iplantc.de.shared.services.ConfluenceService;
import org.iplantc.de.shared.services.ConfluenceServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A service for interfacing with Confluence (service facade).
 * 
 * @author hariolf
 * 
 */
public class ConfluenceServiceFacade {
    private static ConfluenceServiceFacade service;

    private ConfluenceServiceAsync proxy;

    private ConfluenceServiceFacade() {
        final String SESSION_SERVICE = "confluence"; //$NON-NLS-1$

        proxy = (ConfluenceServiceAsync)GWT.create(ConfluenceService.class);
        ((ServiceDefTarget)proxy).setServiceEntryPoint(GWT.getModuleBaseURL() + SESSION_SERVICE);
    }

    /**
     * Retrieve service facade singleton instance.
     * 
     * @return a singleton instance of the service facade.
     */
    public static ConfluenceServiceFacade getInstance() {
        if (service == null) {
            service = new ConfluenceServiceFacade();
        }

        return service;
    }

    /**
     * Creates a new page in the iPlant wiki as a child of the "List of Applications" page.
     * 
     * @param toolName the name of the tool which is used as the page title
     * @param description a tool description
     * @param callback called after the service call finishes
     */
    public void createDocumentationPage(String toolName, String description,
            AsyncCallback<String> callback) {
        proxy.addPage(toolName, description, callback);
    }

    /**
     * Adds a user comment to a tool description page.
     * 
     * @param toolName the name of the tool which is also the page title
     * @param comment a comment
     * @param callback called after the service call finishes
     */
    public void addComment(String toolName, String comment, AsyncCallback<String> callback) {
        proxy.addComment(toolName, comment, callback);
    }

    /**
     * Removes a user comment from a tool description page.
     * 
     * @param toolName the name of the tool which is also the page title
     * @param commentId the comment ID in Confluence
     * @param callback called after the service call finishes
     */
    void removeComment(String toolName, Long commentId, AsyncCallback<String> callback) {
        proxy.removeComment(toolName, commentId, callback);
    }
}
