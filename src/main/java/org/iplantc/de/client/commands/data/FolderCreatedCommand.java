package org.iplantc.de.client.commands.data;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.controllers.DataMonitor;

import com.google.gwt.json.client.JSONObject;

/**
 * Folder creation command. Parses out necessary data and calls the DataMonitor's folder create function.
 * 
 * @author amuir
 * 
 */
public class FolderCreatedCommand implements DataCommand {
    @Override
    public void execute(DataMonitor monitor, JSONObject objData) {
        if (monitor != null && objData != null) {
            String idParentFolder = JsonUtil.getString(objData, "parentFolderId"); //$NON-NLS-1$

            monitor.folderCreated(idParentFolder, objData);
        }
    }
}
