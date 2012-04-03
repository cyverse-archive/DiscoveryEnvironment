package org.iplantc.de.client.commands.data;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.controllers.DataMonitor;

import com.google.gwt.json.client.JSONObject;

/**
 * File save as command. Parses out necessary data and calls the DataMonitor's file save ad function.
 * 
 * @author amuir
 * 
 */
public class FileSaveAsCommand implements DataCommand {
    private File getFileInfo(final JSONObject objData) {
        File ret = new File(objData);
        if (ret.getName() == null || ret.getName().isEmpty()) {
            ret.setName(DiskResourceUtil.parseNameFromPath(ret.getId()));
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final DataMonitor monitor, final JSONObject objData) {
        if (monitor != null && objData != null) {
            String idOrig = JsonUtil.getString(objData, "idOrig"); //$NON-NLS-1$
            String idParent = JsonUtil.getString(objData, "idParent"); //$NON-NLS-1$
            File info = getFileInfo(objData);

            monitor.fileSavedAs(idOrig, idParent, info);
        }
    }
}
