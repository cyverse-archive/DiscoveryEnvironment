package org.iplantc.de.client.utils.builders.context;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * Basic functionality shared by context builders.
 * 
 * @author amuir
 * 
 */
public abstract class AbstractContextBuilder {
    /**
     * Parse the action from payload JSON.
     * 
     * @param objPayload payload to parse.
     * @return String representing the action. null on failure.
     */
    protected String getAction(final JSONObject objPayload) {
        String ret = null; // assume failure

        if (objPayload != null) {
            ret = JsonUtil.getString(objPayload, "action"); //$NON-NLS-1$
        }

        return ret;
    }

    /**
     * Build a context info object from payload JSON.
     * 
     * @param objPayload payload to parse.
     * @return String representation of context JSON. null on failure.
     */
    public abstract String build(final JSONObject objPayload);
}
