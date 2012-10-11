package org.iplantc.de.client.services;

import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
     * @param maxNotifications the maximum number of notifications to retrieve.
     * @param callback called on RPC completion.
     */
    public void getNotifications(int limit, int offset, String filter, String sortDir, Boolean seen,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl(); //$NON-NLS-1$

        StringBuilder builder = new StringBuilder("notifications/messages?limit=" + limit + "&offset="
                + offset);
        if (filter != null && !filter.isEmpty()) {
            builder.append("&filter=" + filter);
        }

        if (sortDir != null && !sortDir.isEmpty() && !sortDir.equalsIgnoreCase("NONE")) {
            builder.append("&sortDir=" + sortDir);
        }

        if (seen != null) {
            builder.append("&seen=" + seen);
        }

        address = address + builder.toString();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Get messages from the server.
     * 
     * @param callback called on RPC completion.
     */
    public void getRecentMessages(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "notifications/last-ten-messages"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void markAsSeen(final JSONObject seenIds, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "notifications/seen"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                seenIds.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Delete messages from the server.
     * 
     * @param arrDeleteIds array of notification ids to delete from the server.
     * @param callback called on RPC completion.
     */
    public void deleteMessages(final JSONObject deleteIds, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "notifications/delete"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                deleteIds.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void getUnSeenMessageCount(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "notifications/count-messages?seen=false"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
