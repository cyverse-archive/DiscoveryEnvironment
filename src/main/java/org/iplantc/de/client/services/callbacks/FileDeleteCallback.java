package org.iplantc.de.client.services.callbacks;

import java.util.List;

import org.iplantc.de.client.I18N;

import com.google.gwt.json.client.JSONObject;

/**
 * Defines an asynchronous callback for a file Delete event.
 * 
 * @author psarando
 * 
 */
public class FileDeleteCallback extends DiskResourceDeleteCallback {

    public FileDeleteCallback(List<String> listFolders) {
        super(listFolders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.deleteFileFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessageForFiles(code, parsePathsToNameList(jsonError));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getResourceListKey() {
        return "files"; //$NON-NLS-1$
    }
}
