package org.iplantc.de.client.services;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.utils.DataUtils;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * 
 * @author sriram
 * 
 */

public abstract class DiskResouceDuplicatesCheckCallback extends DiskResourceServiceCallback implements
        AsyncCallback<String> {

    private List<String> diskResourceIds;

    public DiskResouceDuplicatesCheckCallback(List<String> diskResourceIds) {
        this.diskResourceIds = diskResourceIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.duplicateCheckFailed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessage(code, parsePathsToNameList(jsonError));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(String response) {
        JSONObject jsonResponse = JsonUtil.getObject(response);

        String status = JsonUtil.getString(jsonResponse, "status"); //$NON-NLS-1$
        JSONObject paths = JsonUtil.getObject(jsonResponse, "paths"); //$NON-NLS-1$

        if (!status.equalsIgnoreCase("success") || paths == null) { //$NON-NLS-1$
            onFailure(new Exception(JsonUtil.getString(jsonResponse, "reason"))); //$NON-NLS-1$
            return;
        }

        List<String> duplicateFiles = new ArrayList<String>();

        for (final String resourceId : diskResourceIds) {
            // TODO add an extra check to make sure the resourceId key is found in paths?
            boolean fileExists = JsonUtil.getBoolean(paths, resourceId, false);

            if (fileExists) {
                duplicateFiles.add(DataUtils.parseNameFromPath(resourceId));
            }
        }

        // always call mark duplicates. if no duplicates are found the list is empty.
        // clients implementing this class then just needs to override only on method
        markDuplicates(duplicateFiles);
        return;
    }

    public abstract void markDuplicates(List<String> duplicates);

}
