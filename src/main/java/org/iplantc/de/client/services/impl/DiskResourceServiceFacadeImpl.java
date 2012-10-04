package org.iplantc.de.client.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.shared.SharedDataApiServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Provides access to remote services for folder operations.
 * 
 * @author amuir
 * 
 */
public class DiskResourceServiceFacadeImpl implements DiskResourceServiceFacade {
    private final String serviceNamePrefix = "org.iplantc.services.de-data-mgmt"; //$NON-NLS-1$

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getHomeFolder(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getHomeFolder(AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".root-folders"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getDefaultOutput(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getDefaultOutput(final String folderName, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + folderName;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#putDefaultOutput(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void putDefaultOutput(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + DEProperties.getInstance().getDefaultOutputFolderName();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                "{\"path\":\"" + DEProperties.getInstance().getDefaultOutputFolderName() + "\"}");
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getFolderContents(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getFolderContents(final String path, AsyncCallback<String> callback) {
        getFolderContents(path, true, callback);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getFolderContents(java.lang.String, boolean, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getFolderContents(final String path, boolean includeFiles, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory?includefiles=" + (includeFiles ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if (path != null && !path.isEmpty()) {
            fullAddress += "&path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(fullAddress);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#createFolder(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void createFolder(String folderpath, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-create"; //$NON-NLS-1$
        JSONObject obj = new JSONObject();
        obj.put("path", new JSONString(folderpath)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                obj.toString());
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#diskResourcesExist(java.util.List, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void diskResourcesExist(List<String> diskResourceIds, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".exists"; //$NON-NLS-1$

        JSONObject json = new JSONObject();
        json.put("paths", JsonUtil.buildArrayFromStrings(diskResourceIds)); //$NON-NLS-1$
        String body = json.toString();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#previewFile(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void previewFile(final String path, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".file-preview"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(path)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#moveDiskResources(java.util.List, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void moveDiskResources(final List<DiskResource> diskResources, final String idDestFolder,
            AsyncCallback<String> callback) {
        ArrayList<String> srcFolders = new ArrayList<String>();
        ArrayList<String> srcFiles = new ArrayList<String>();

        for (DiskResource src : diskResources) {
            String srcId = src.getId();
            if (!srcId.equals(idDestFolder)) {
                if (src instanceof Folder) {
                    srcFolders.add(srcId);
                } else if (src instanceof File) {
                    srcFiles.add(srcId);
                }
            }
        }

        if (srcFolders.size() > 0) {
            // call service to move folders
            moveFolder(srcFolders, idDestFolder, callback);
        }

        if (srcFiles.size() > 0) {
            // call service to move files
            moveFile(srcFiles, idDestFolder, callback);
        }
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#moveFile(java.util.List, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void moveFile(final List<String> idSrcFiles, final String idDestFolder,
            final AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".file-move"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("dest", new JSONString(idDestFolder)); //$NON-NLS-1$
        body.put("sources", JsonUtil.buildArrayFromStrings(idSrcFiles)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#moveFolder(java.util.List, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void moveFolder(final List<String> idSrcFolders, final String idDestFolder,
            AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".directory-move"; //$NON-NLS-1$
        String body = "{" + JsonUtil.buildStringArray("sources", idSrcFolders) + ", \"dest\": \"" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + idDestFolder + "\"}"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#renameFolder(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void renameFolder(final String srcName, final String destName, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-rename"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(srcName)); //$NON-NLS-1$
        body.put("dest", new JSONString(destName)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#renameFile(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void renameFile(String srcId, String destId, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".file-rename"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("source", new JSONString(srcId)); //$NON-NLS-1$
        body.put("dest", new JSONString(destId)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());

        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#importFromUrl(java.lang.String, java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void importFromUrl(final String url, final String dest, final String description,
            AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".file-urlupload"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("dest", new JSONString(dest)); //$NON-NLS-1$
        body.put("address", new JSONString(url)); //$NON-NLS-1$
        body.put("description", new JSONString(description)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#upload(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void upload(AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".idrop-upload"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#download(com.google.gwt.json.client.JSONArray, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void download(JSONArray paths, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".idrop-download"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("paths", paths); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#simpleDownload(java.lang.String)
     */
    @Override
    public void simpleDownload(String path) {
        // We must proxy the download requests through a servlet, since the actual download service may
        // be on a port behind a firewall that the servlet can access, but the client can not.
        String address = Format.substitute("{0}{1}?user={2}&path={3}", GWT.getModuleBaseURL(), //$NON-NLS-1$
                Constants.CLIENT.fileDownloadServlet(), UserInfo.getInstance().getUsername(), path);

        WindowUtil.open(URL.encode(address), "width=100,height=100"); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#deleteFolders(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void deleteFolders(String pathsAsJsonArray, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-delete"; //$NON-NLS-1$
        String body = "{\"paths\": " + pathsAsJsonArray + "}"; //$NON-NLS-1$ //$NON-NLS-2$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#deleteFiles(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void deleteFiles(String pathsAsJsonArray, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".file-delete"; //$NON-NLS-1$
        String body = "{\"paths\": " + pathsAsJsonArray + "}"; //$NON-NLS-1$ //$NON-NLS-2$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getFileMetaData(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getFileMetaData(String path, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".file-metadata" + "?path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getFolderMetaData(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getFolderMetaData(String path, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".folder-metadata" + "?path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#setFolderMetaData(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void setFolderMetaData(String folderId, String body, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".folder-metadata-batch" + "?path=" + URL.encodePathSegment(folderId); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#setFileMetaData(java.lang.String, java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void setFileMetaData(String fileId, String body, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".file-metadata-batch" + "?path=" + URL.encodePathSegment(fileId); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#shareDiskResource(com.google.gwt.json.client.JSONObject, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void shareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "share"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#unshareDiskResource(com.google.gwt.json.client.JSONObject, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void unshareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "unshare"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.client.services.IDiskResourceServiceFacade#getPermissions(com.google.gwt.json.client.JSONObject, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getPermissions(JSONObject body, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".permissions"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * Performs the actual service call.
     * 
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {

        SharedDataApiServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
