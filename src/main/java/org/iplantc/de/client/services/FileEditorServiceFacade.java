package org.iplantc.de.client.services;

import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.Constants;
import org.iplantc.de.shared.SharedDataApiServiceFacade;
import org.iplantc.de.shared.SharedServiceFacade;
import org.iplantc.de.shared.SharedUnsecuredServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Facade for file editors.
 */
public class FileEditorServiceFacade {
    /**
     * Call service to retrieve the manifest for a requested file
     * 
     * @param idFile desired manifest's file ID (path).
     * @param callback executes when RPC call is complete.
     */
    public void getManifest(String idFile, DiskResourceServiceCallback callback) {
        String address = "org.iplantc.services.de-data-mgmt.file-manifest?path=" //$NON-NLS-1$
                + URL.encodeQueryString(idFile);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedDataApiServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Construct a servlet download URL for the given file ID.
     * 
     * @param path the desired file path to be used in the return URL
     * @return a URL for the given file ID.
     */
    public String getServletDownloadUrl(final String path) {
        String address = Format.substitute("{0}{1}?url=display-download&user={2}&path={3}", //$NON-NLS-1$
                GWT.getModuleBaseURL(), Constants.CLIENT.fileDownloadServlet(), UserInfo.getInstance()
                        .getUsername(), path);

        return URL.encode(address);
    }

    /**
     * Call service to retrieve data for a requested file
     * 
     * @param idFile file to retrieve raw data from.
     * @param callback executes when RPC call is complete.
     */
    public void getData(String url, DiskResourceServiceCallback callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + url;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedUnsecuredServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Get Tree URLs for the given tree's file ID.
     * 
     * @param idFile file ID (path) of the tree.
     * @param callback executes when RPC call is complete.
     */
    public void getTreeUrl(String idFile, AsyncCallback<String> callback) {
        String address = "org.iplantc.services.buggalo.baseUrl?path=" + URL.encodeQueryString(idFile); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    public void uploadTextAsFile(String destination, String fileContents, AsyncCallback<String> callback) {
        String fullAddress = "org.iplantc.services.de-data-mgmt.saveas";
        JSONObject obj = new JSONObject();
        obj.put("dest", new JSONString(destination)); //$NON-NLS-1$
        obj.put("content", new JSONString(fileContents));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                obj.toString());
        SharedDataApiServiceFacade.getInstance().getServiceData(wrapper, callback);

    }

}
