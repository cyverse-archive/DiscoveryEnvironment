package org.iplantc.de.client.services.callbacks;


import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.json.client.JSONObject;

public class FileMoveCallback extends DiskResourceMoveCallback {

    public FileMoveCallback(Component maskedCaller) {
        super(maskedCaller);
    }

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
