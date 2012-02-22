package org.iplantc.de.client.services;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.utils.DataUtils;

import com.google.gwt.json.client.JSONObject;

/**
 * 
 * 
 * @author sriram
 * 
 */
public class DiskResourceMetadataUpdateCallback extends DiskResourceServiceCallback {

    public static enum TYPE {
        FILE, FOLDER
    };

    private TYPE type;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(String result) {
        // do nothing intentionally
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.metadataUpdateFailed();
    }

    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        if (type.equals(TYPE.FILE)) {
            return getErrorMessageForFiles(code,
                    DataUtils.parseNameFromPath(JsonUtil.getString(jsonError, PATH)));
        } else {
            return getErrorMessageForFolders(code,
                    DataUtils.parseNameFromPath(JsonUtil.getString(jsonError, PATH)));
        }
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }
}