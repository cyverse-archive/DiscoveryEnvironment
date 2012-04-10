package org.iplantc.de.client.utils.builders.context;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Build a JSON string to provide context when a user clicks on an item with a data context associated
 * it.
 * 
 * @author amuir
 * 
 */
public class DataContextBuilder extends AbstractContextBuilder {
    private JSONArray getFiles(final JSONObject objPayload) {
        JSONArray ret = null; // assume failure

        if (objPayload != null) {
            JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

            if (objData != null) {
                ret = objData.containsKey("created") ? objData.get("created").isArray() : null; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return ret;
    }

    private String getParentId(final JSONObject objPayload) {
        String ret = null; // assume failure

        if (objPayload != null) {
            JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

            if (objData != null) {
                ret = JsonUtil.getString(objData, "parentFolderId"); //$NON-NLS-1$
            }
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String build(final JSONObject objPayload) {
        String ret = null; // assume failure

        String action = getAction(objPayload);

        if (action != null) {
            if (action.equals("file_uploaded")) { //$NON-NLS-1$
                JSONArray arr = getFiles(objPayload);

                if (arr != null) {
                    JSONObject file = JsonUtil.getObjectAt(arr, 0);

                    if (file != null) {
                        String id = JsonUtil.getString(file, "id"); //$NON-NLS-1$
                        String name = JsonUtil.getString(file, "name"); //$NON-NLS-1$
                        String idParent = getParentId(objPayload);
                        JSONObject obj = new JSONObject();

                        obj.put("id", new JSONString(id));
                        obj.put("name", new JSONString(name));
                        obj.put("idParent", new JSONString(idParent));

                        ret = obj.toString();
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Build context json from a disk resource id.
     * 
     * @param resource resource containing id, and name of resource.
     * @return String representation of context JSON. null on failure.
     */
    public String build(final String idDiskResource) {
        if (idDiskResource == null || idDiskResource.isEmpty()) {
            return null;
        }

        JSONObject obj = new JSONObject();

        obj.put("id", new JSONString(idDiskResource));
        obj.put("name", new JSONString(DiskResourceUtil.parseNameFromPath(idDiskResource)));
        obj.put("idParent", new JSONString(DiskResourceUtil.parseParent(idDiskResource)));

        return obj.toString();
    }
}
