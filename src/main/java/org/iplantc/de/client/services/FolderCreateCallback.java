package org.iplantc.de.client.services;

import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Defines an asynchronous callback for folder create events.
 */
public class FolderCreateCallback extends DiskResourceActionCallback {
    private final String idParentFolder;
    private final String name;

    /**
     * Instantiate from a parent id and name.
     * 
     * @param idParentFolder unique id of parent folder.
     * @param name name of created folder.
     */
    public FolderCreateCallback(final String idParentFolder, final String name) {
        this.idParentFolder = idParentFolder;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActionType getActionType() {
        return ActionType.FOLDER_CREATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JSONObject buildPayload(final JSONObject jsonResult) {
        if (jsonResult == null) {
            return null;
        }

        jsonResult.put("parentFolderId", new JSONString(idParentFolder)); //$NON-NLS-1$
        jsonResult.put(Folder.LABEL, new JSONString(name));
        jsonResult.put(Folder.ID, jsonResult.get("path")); //$NON-NLS-1$

        return jsonResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.createFolderFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessageForFolders(code, name);
    }
}
