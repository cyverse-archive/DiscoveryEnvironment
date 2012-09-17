package org.iplantc.de.client.services.callbacks;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;

import com.google.gwt.json.client.JSONObject;

/**
 * Defines an asynchronous callback for a file Rename event.
 * 
 * @author psarando
 * 
 */
public class FileRenameCallback extends DiskResourceRenameCallback {

    public FileRenameCallback(String id, String name) {
        super(id, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ActionType getActionType() {
        return ActionType.FILE_RENAMED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.renameFileFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        String fileName = DiskResourceUtil.parseNameFromPath(JsonUtil.getString(jsonError, PATH));

        return getErrorMessageForFiles(code, fileName);
    }

}
