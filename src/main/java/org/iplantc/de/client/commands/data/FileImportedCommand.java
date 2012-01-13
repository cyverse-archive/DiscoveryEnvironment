package org.iplantc.de.client.commands.data;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.controllers.DataMonitor;
import org.iplantc.de.client.utils.DataUtils;

import com.google.gwt.json.client.JSONObject;

/**
 * File imported command. Parses out necessary data and calls the DataMonitor's file adding function.
 * 
 * @author amuir
 * 
 */
public class FileImportedCommand implements DataCommand {
    private File getFileInfo(final JSONObject objData) {
        File ret = new File(objData);
        if (ret.getName() == null || ret.getName().isEmpty()) {
            ret.setName(DataUtils.parseNameFromPath(ret.getId()));
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final DataMonitor monitor, final JSONObject objData) {
        if (monitor != null && objData != null) {
            String idParentFolder = JsonUtil.getString(objData, "parentFolderId"); //$NON-NLS-1$
            File info = getFileInfo(objData);

            monitor.addFile(idParentFolder, info);
        }
    }
}
