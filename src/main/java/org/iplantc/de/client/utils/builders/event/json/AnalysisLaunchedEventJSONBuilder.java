package org.iplantc.de.client.utils.builders.event.json;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Builder class to create JSON for an analysis launched payload event.
 * 
 * @author amuir
 * 
 */
public class AnalysisLaunchedEventJSONBuilder extends AbstractEventJSONBuilder {
    /**
     * Creates a new AnalysisLaunchedEventJSONBuilder.
     * 
     * @param action
     */
    public AnalysisLaunchedEventJSONBuilder(String action) {
        super(action);
    }

    private String buildMessageText(final JSONObject jsonObj) {
        String name = JsonUtil.getString(jsonObj, "name"); //$NON-NLS-1$

        return Format.substitute(I18N.DISPLAY.launchSuccess(), name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String build(String json) {
        String ret = null; // assume failure

        if (json != null) {
            JSONObject jsonObj = JSONParser.parseStrict(json).isObject();

            if (jsonObj != null) {
                ret = "{\"type\": \"analysis\", \"message\": {\"id\": \"id_message\", \"text\": \"" //$NON-NLS-1$
                        + buildMessageText(jsonObj) + "\"}}"; //$NON-NLS-1$
            }
        }

        return ret;
    }
}
