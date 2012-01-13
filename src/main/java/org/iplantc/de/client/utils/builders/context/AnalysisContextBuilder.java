package org.iplantc.de.client.utils.builders.context;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * Build a JSON string to provide context when a user clicks on an item with an analysis context
 * associated it.
 * 
 * @author amuir
 * 
 */
public class AnalysisContextBuilder extends AbstractContextBuilder {
    /**
     * {@inheritDoc}
     */
    @Override
    public String build(JSONObject objPayload) {
        String ret = null; // assume failure

        if (objPayload != null) {
            String action = getAction(objPayload);

            if (action != null) {
                if (action.equals("job_status_change")) { //$NON-NLS-1$
                    String id = JsonUtil.getString(objPayload, "id"); //$NON-NLS-1$

                    ret = "{\"id\": " + JsonUtil.quoteString(id) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        return ret;
    }
}
