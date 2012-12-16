package org.iplantc.de.client.services.impl;

import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uidiskresource.client.services.FileEditorServiceFacade;
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
public class FileEditorServiceFacadeImpl implements FileEditorServiceFacade {
    @Override
    public void getManifest(String idFile, AsyncCallback<String> callback) {
        String address = "org.iplantc.services.de-data-mgmt.file-manifest?path=" //$NON-NLS-1$
                + URL.encodeQueryString(idFile);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedDataApiServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public String getServletDownloadUrl(final String path) {
        String address = Format.substitute("{0}{1}?url=display-download&user={2}&path={3}", //$NON-NLS-1$
                GWT.getModuleBaseURL(), Constants.CLIENT.fileDownloadServlet(), UserInfo.getInstance()
                        .getUsername(), path);

        return URL.encode(address);
    }

    @Override
    public void getData(String url, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getDataMgmtBaseUrl() + url;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedUnsecuredServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void getTreeUrl(String idFile, AsyncCallback<String> callback) {
        String address = "org.iplantc.services.buggalo.baseUrl?path=" + URL.encodeQueryString(idFile); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        SharedServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
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
