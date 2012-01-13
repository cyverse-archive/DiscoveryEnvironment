package org.iplantc.de.client.services;

import com.google.gwt.json.client.JSONObject;

public class FileMoveCallback extends DiskResourceMoveCallback {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPayloadAction() {
        return "file_move"; //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessageForFiles(code, parsePathsToNameList(jsonError));
    }
}
