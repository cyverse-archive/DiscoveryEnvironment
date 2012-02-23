package org.iplantc.de.client.utils.builders.context;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.utils.DataUtils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

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

                        ret = "{"; //$NON-NLS-1$

                        ret += "\"id\": " + JsonUtil.quoteString(id) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
                        ret += "\"name\": " + JsonUtil.quoteString(name) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
                        ret += "\"idParent\": " + JsonUtil.quoteString(idParent); //$NON-NLS-1$

                        ret += "}"; //$NON-NLS-1$
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Build context json from a disk resource and parent folder.
     * 
     * @param resource resource containing id, and name of resource.
     * @param idParentFolder unique identifier for this disk resource's parent folder.
     * @return String representation of context JSON. null on failure.
     */
    public String build(final DiskResource resource, final String idParentFolder) {
        String ret = null; // assume failure

        if (resource != null) {
            ret = "{"; //$NON-NLS-1$

            ret += "\"id\": " + JsonUtil.quoteString(resource.getId()) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
            ret += "\"name\": " + JsonUtil.quoteString(resource.getName()) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
            ret += "\"idParent\": " + JsonUtil.quoteString(idParentFolder); //$NON-NLS-1$

            ret += "}"; //$NON-NLS-1$
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
        String ret = null; // assume failure

        if (idDiskResource != null) {
            ret = "{"; //$NON-NLS-1$

            ret += "\"id\": " + JsonUtil.quoteString(idDiskResource) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
            ret += "\"name\": " + JsonUtil.quoteString(DataUtils.parseNameFromPath(idDiskResource)) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
            ret += "\"idParent\": " + JsonUtil.quoteString(DataUtils.parseParent(idDiskResource)); //$NON-NLS-1$

            ret += "}"; //$NON-NLS-1$
        }

        return ret;
    }
}
