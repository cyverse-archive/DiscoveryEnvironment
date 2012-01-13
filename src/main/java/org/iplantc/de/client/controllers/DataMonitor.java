package org.iplantc.de.client.controllers;

import java.util.List;
import java.util.Map;

import org.iplantc.core.uidiskresource.client.models.File;

import com.google.gwt.json.client.JSONObject;

public interface DataMonitor {
    /**
     * Handle the addition of a new folder.
     * 
     * @param idParentFolder id of parent folder.
     * @param jsonFolder JSON object of new folder.
     */
    void folderCreated(String idParentFolder, JSONObject jsonFolder);

    /**
     * Handle the addition of a new file.
     * 
     * @param idParentFolder id of destination folder for this file.
     * @param info File info model.
     */
    void addFile(String path, File info);

    /**
     * Handle a file being saved with a different name.
     * 
     * @param idOrig id of original file.
     * @param idParent id of parent folder.
     * @param info File info model.
     */
    void fileSavedAs(String idOrig, String idParentFolder, File info);

    /**
     * Rename a file.
     * 
     * @param id id of file to re-name.
     * @param name new file name.
     */
    void fileRename(String id, String name);

    /**
     * Rename a folder.
     * 
     * @param id id of file to re-name.
     * @param name new file name.
     */
    void folderRename(String id, String name);

    /**
     * Delete disk resources by id.
     * 
     * @param folders list of folder ids to be deleted.
     * @param files list of file ids to be deleted.
     */
    void deleteResources(List<String> folders, List<String> files);

    /**
     * Move disk resource
     * 
     * @param folders map containing source and destination
     * 
     */
    void folderMove(Map<String, String> folders);

    /**
     * Move disk resource
     * 
     * @param files map containing source and destination
     * 
     */
    void fileMove(Map<String, String> files);
}
