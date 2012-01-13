package org.iplantc.de.client.utils.builders.event.json;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * General event JSON builder. Simply wraps the data. No message support is provided.
 * 
 * @author amuir
 * 
 */
public class BasicEventJSONBuilder extends AbstractEventJSONBuilder {
    protected final String type;

    /**
     * Instantiate from an action.
     * 
     * @param action action tag to be added to our payload.
     */
    public BasicEventJSONBuilder(final String type, final String action) {
        super(action);

        this.type = type;
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
                ret = "{\"type\": " + JsonUtil.quoteString(type) + ", \"payload\": " //$NON-NLS-1$ //$NON-NLS-2$
                        + buildPayload(jsonObj) + "}"; //$NON-NLS-1$
            }
        }

        return ret;
    }
}
