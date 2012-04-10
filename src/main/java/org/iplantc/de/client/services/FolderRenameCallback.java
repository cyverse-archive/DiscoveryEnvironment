package org.iplantc.de.client.services;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;

import com.google.gwt.json.client.JSONObject;

/**
 * Defines an asynchronous callback for a folder Rename event.
 * 
 * @author psarando
 * 
 */
public class FolderRenameCallback extends DiskResourceRenameCallback {

    public FolderRenameCallback(String id, String name) {
        super(id, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActionType getActionType() {
        return ActionType.FOLDER_RENAMED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.renameFolderFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        String folderName = DiskResourceUtil.parseNameFromPath(JsonUtil.getString(jsonError, PATH));

        return getErrorMessageForFolders(code, folderName);
    }
}
