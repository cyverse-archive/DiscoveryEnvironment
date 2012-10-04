package org.iplantc.de.client.services;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
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
public class DiskResourceServiceFacadeImpl {
    private final String serviceNamePrefix = "org.iplantc.services.de-data-mgmt"; //$NON-NLS-1$

    /**
     * Call service to retrieve the root folder info for the current user
     * 
     * @param callback executed when RPC call completes.
     */
    public void getHomeFolder(AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".root-folders"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    /**
     * get user's default analyses output folder
     * 
     * @param folderName
     * @param callback
     */
    public void getDefaultOutput(final String folderName, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + folderName;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * set user's default analyses output folder
     * 
     * @param folderName
     * @param callback
     */
    public void putDefaultOutput(AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "default-output-dir?name="
                + DEProperties.getInstance().getDefaultOutputFolderName();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                "{\"path\":\"" + DEProperties.getInstance().getDefaultOutputFolderName() + "\"}");
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Called to retrieve the entire contents of a folder.
     * 
     * @param path path to requested folder.
     * @param callback executed when RPC call completes.
     */
    public void getFolderContents(final String path, AsyncCallback<String> callback) {
        getFolderContents(path, true, callback);
    }

    /**
     * Called to retrieve the contents of a folder, with or without its file listing.
     * 
     * @param path path to requested folder.
     * @param includeFiles whether or not to include the file listing of the given folder
     * @param callback executed when RPC call completes.
     */
    public void getFolderContents(final String path, boolean includeFiles, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory?includefiles=" + (includeFiles ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if (path != null && !path.isEmpty()) {
            fullAddress += "&path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(fullAddress);
        callService(callback, wrapper);
    }

    /**
     * Call service to create a new folder
     * 
     * @param folderpath path of the folder to be created
     * @param callback executed when RPC call completes.
     */
    public void createFolder(String folderpath, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-create"; //$NON-NLS-1$
        JSONObject obj = new JSONObject();
        obj.put("path", new JSONString(folderpath)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                obj.toString());
        callService(callback, wrapper);
    }

    /**
     * Check if a list of files or folders exist.
     * 
     * @param diskResourceIds paths to desired resources.
     * @param callback callback executed when RPC call completes.
     */
    public void diskResourcesExist(List<String> diskResourceIds, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".exists"; //$NON-NLS-1$

        JSONObject json = new JSONObject();
        json.put("paths", JsonUtil.buildArrayFromStrings(diskResourceIds)); //$NON-NLS-1$
        String body = json.toString();

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        callService(callback, wrapper);
    }

    /**
     * Fetch preview data for a file.
     * 
     * @param path path to desired file.
     * @param callback callback executed when RPC call completes.
     */
    public void previewFile(final String path, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".file-preview"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(path)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * Calls the move folder and move file services for the list of given disk resource ids.
     * 
     * @param diskResources list of file and folder ids to move.
     * @param idDestFolder id of the destination folder.
     */
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

    /**
     * Call service to move the given file ids to the given folder.
     * 
     * @param idSrcFiles list of file ids to move.
     * @param idDestFolder id of the destination folder.
     * @param callback service success/failure callback
     */
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

    /**
     * Call service to move the given folder ids to the given destination folder.
     * 
     * @param idSrcFolders list of folder ids to move.
     * @param idDestFolder id of the destination folder.
     * @param callback service success/failure callback
     */
    public void moveFolder(final List<String> idSrcFolders, final String idDestFolder,
            AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".directory-move"; //$NON-NLS-1$
        String body = "{" + JsonUtil.buildStringArray("sources", idSrcFolders) + ", \"dest\": \"" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + idDestFolder + "\"}"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address, body);
        callService(callback, wrapper);
    }

    /**
     * Call service rename a folder.
     * 
     * @param srcName
     * @param destName
     * @param callback service success/failure callback
     */
    public void renameFolder(final String srcName, final String destName, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-rename"; //$NON-NLS-1$
        JSONObject body = new JSONObject();
        body.put("source", new JSONString(srcName)); //$NON-NLS-1$
        body.put("dest", new JSONString(destName)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * Call service to rename a file.
     * 
     * @param srcId
     * @param destId
     * @param callback service success/failure callback
     */
    public void renameFile(String srcId, String destId, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".file-rename"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("source", new JSONString(srcId)); //$NON-NLS-1$
        body.put("dest", new JSONString(destId)); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body.toString());

        callService(callback, wrapper);
    }

    /**
     * Call service to upload a file from a given URL.
     * 
     * @param url
     * @param dest id of the destination folder.
     * @param description description of the file to upload.
     * @param callback service success/failure callback
     */
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

    /**
     * Call service to retrieve upload configuration values for idrop-lite.
     * 
     * @param callback executed when RPC call completes.
     */
    public void upload(AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".idrop-upload"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        callService(callback, wrapper);
    }

    /**
     * Call service to retrieve upload configuration values for idrop-lite.
     * 
     * @param callback executed when RPC call completes.
     */
    public void download(JSONArray paths, AsyncCallback<String> callback) {
        String address = serviceNamePrefix + ".idrop-download"; //$NON-NLS-1$

        JSONObject body = new JSONObject();
        body.put("paths", paths); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(callback, wrapper);
    }

    /**
     * Opens a window to download the file with the given path.
     * 
     * @param path Path of the file to download.
     */
    public void simpleDownload(String path) {
        // We must proxy the download requests through a servlet, since the actual download service may
        // be on a port behind a firewall that the servlet can access, but the client can not.
        String address = Format.substitute("{0}{1}?user={2}&path={3}", GWT.getModuleBaseURL(), //$NON-NLS-1$
                Constants.CLIENT.fileDownloadServlet(), UserInfo.getInstance().getUsername(), path);

        WindowUtil.open(URL.encode(address), "width=100,height=100"); //$NON-NLS-1$
    }

    /**
     * Call service to delete folders.
     * 
     * @param pathsAsJsonArray
     * @param callback executed when RPC call completes.
     */
    public void deleteFolders(String pathsAsJsonArray, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".directory-delete"; //$NON-NLS-1$
        String body = "{\"paths\": " + pathsAsJsonArray + "}"; //$NON-NLS-1$ //$NON-NLS-2$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /**
     * Call service to delete folders.
     * 
     * @param pathsAsJsonArray
     * @param callback executed when RPC call completes.
     */
    public void deleteFiles(String pathsAsJsonArray, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix + ".file-delete"; //$NON-NLS-1$
        String body = "{\"paths\": " + pathsAsJsonArray + "}"; //$NON-NLS-1$ //$NON-NLS-2$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /**
     * call service to get file metadata
     * 
     * @param path path of resource
     * @param callback execute when RPC call complete
     */
    public void getFileMetaData(String path, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".file-metadata" + "?path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);
    }

    /**
     * call service to get folder metadata
     * 
     * @param path path of resource
     * @param callback execute when RPC call complete
     */
    public void getFolderMetaData(String path, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".folder-metadata" + "?path=" + URL.encodePathSegment(path); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, fullAddress);
        callService(callback, wrapper);
    }

    /**
     * call service to set folder metadata
     * 
     * @param folderId id of folder resource
     * @param body metadata in json format
     * @param callback execute when RPC call complete
     */
    public void setFolderMetaData(String folderId, String body, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".folder-metadata-batch" + "?path=" + URL.encodePathSegment(folderId); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /**
     * call service to set file metadata
     * 
     * @param fileId id of file resource
     * @param body metadata in json format
     * @param callback execute when RPC call complete
     */
    public void setFileMetaData(String fileId, String body, AsyncCallback<String> callback) {
        String fullAddress = serviceNamePrefix
                + ".file-metadata-batch" + "?path=" + URL.encodePathSegment(fileId); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, fullAddress,
                body);
        callService(callback, wrapper);
    }

    /**
     * 
     * Share a resource with give user with permission
     * 
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
    public void shareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "share"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * UnShare a resource with give user with permission
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
    public void unshareDiskResource(JSONObject body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "unshare"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * get user permission info on selected disk resources
     * 
     * @param body - Post body in JSONObject format
     * @param callback callback object
     */
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
