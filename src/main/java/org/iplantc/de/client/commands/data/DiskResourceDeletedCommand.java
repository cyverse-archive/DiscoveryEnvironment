package org.iplantc.de.client.commands.data;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.controllers.DataMonitor;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * Disk resource deleted command. Parses out necessary data and calls the DataMonitor's delete resources
 * function.
 * 
 * @author amuir
 * 
 */
public class DiskResourceDeletedCommand implements DataCommand {
    private List<String> parseStringList(final String key, final JSONObject objData) {
        List<String> ret = new ArrayList<String>();

        JSONValue valItems = objData.get(key);

        if (!JsonUtil.isEmpty(valItems)) {
            JSONArray items = valItems.isArray();

            // loop through items and add them to our array
            for (int i = 0,len = items.size(); i < len; i++) {
                JSONValue item = items.get(i);
                ret.add(item.isString().stringValue());
            }
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final DataMonitor monitor, final JSONObject objData) {
        if (monitor != null && objData != null) {
            List<String> folders = parseStringList("folders", objData); //$NON-NLS-1$
            List<String> files = parseStringList("files", objData); //$NON-NLS-1$

            monitor.deleteResources(folders, files);
        }
    }
}
