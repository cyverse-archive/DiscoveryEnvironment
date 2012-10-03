package org.iplantc.de.client.services.callbacks;

import java.util.List;

import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.json.client.JSONObject;

/**
 * Defines an asynchronous callback for a folder Delete event.
 * 
 * @author psarando
 * 
 */
public class FolderDeleteCallback extends DiskResourceDeleteCallback {

    public FolderDeleteCallback(List<String> listFolders, Component maskingParent) {
        super(listFolders, maskingParent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.deleteFolderFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessageForFolders(code, parsePathsToNameList(jsonError));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getResourceListKey() {
        return "folders"; //$NON-NLS-1$
    }
}
