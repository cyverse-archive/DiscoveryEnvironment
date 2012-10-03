package org.iplantc.de.client.services.callbacks;


import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.json.client.JSONObject;

public class FolderMoveCallback extends DiskResourceMoveCallback {

    public FolderMoveCallback(Component maskedCaller) {
        super(maskedCaller);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPayloadAction() {
        return "folder_move"; //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessageForFolders(code, parsePathsToNameList(jsonError));
    }
}
