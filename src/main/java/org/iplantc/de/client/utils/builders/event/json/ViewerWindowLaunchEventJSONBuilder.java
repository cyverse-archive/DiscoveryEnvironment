package org.iplantc.de.client.utils.builders.event.json;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Build JSON for a window launch event.
 * 
 * @author amuir
 * 
 */
public class ViewerWindowLaunchEventJSONBuilder extends AbstractEventJSONBuilder {
    /**
     * Instantiate from an action.
     * 
     * @param action string representation of our action.
     */
    public ViewerWindowLaunchEventJSONBuilder(String action) {
        super(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String build(final String json) {
        String ret = null; // assume failure

        if (json != null) {
            JSONObject jsonObj = JSONParser.parseStrict(json).isObject();

            if (jsonObj != null) {
                ret = "{\"type\": \"window\", \"payload\": " + buildPayload(jsonObj) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return ret;
    }
}
