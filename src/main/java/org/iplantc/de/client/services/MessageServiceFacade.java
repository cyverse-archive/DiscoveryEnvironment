package org.iplantc.de.client.services;

import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.iplantc.core.uicommons.client.DEServiceFacade;

/**
 * Provides access to remote services to acquire messages and notifications.
 * 
 * @author amuir
 * 
 */
public class MessageServiceFacade {
    /**
     * Get notifications from the server.
     * 
     * @param username the user's name.
     * @param maxNotifications the maximum number of notifications to retrieve.
     * @param callback called on RPC completion.
     */
    public void getNotifications(final String username, int maxNotifications,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "notifications/get-messages"; //$NON-NLS-1$

        String disposition = "{\"limit\":" + maxNotifications + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                disposition);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Get messages from the server.
     * 
     * @param idWorkspace the user's name.
     * @param callback called on RPC completion.
     */
    public void getMessages(final String username, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "notifications/get-unseen-messages"; //$NON-NLS-1$
        String disposition = "{}"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                disposition);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Delete messages from the server.
     * 
     * @param arrDeleteIds array of notification ids to delete from the server.
     * @param callback called on RPC completion.
     */
    public void deleteMessages(final String arrDeleteIds, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "notifications/delete"; //$NON-NLS-1$
        String disposition = "{\"uuids\": " + arrDeleteIds + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                disposition);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
