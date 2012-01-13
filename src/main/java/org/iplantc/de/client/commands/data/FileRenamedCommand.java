package org.iplantc.de.client.commands.data;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.controllers.DataMonitor;

import com.google.gwt.json.client.JSONObject;

/**
 * File renamed command. Parses out necessary data and calls the DataMonitor's file rename function.
 * 
 * @author amuir
 * 
 */
public class FileRenamedCommand implements DataCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(DataMonitor monitor, JSONObject objData) {
        if (monitor != null && objData != null) {
            String id = JsonUtil.getString(objData, "id"); //$NON-NLS-1$
            String name = JsonUtil.getString(objData, "name"); //$NON-NLS-1$

            monitor.fileRename(id, name);
        }
    }
}
