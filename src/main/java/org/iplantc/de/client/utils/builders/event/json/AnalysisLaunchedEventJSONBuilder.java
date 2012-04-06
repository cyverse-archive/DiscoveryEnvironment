package org.iplantc.de.client.utils.builders.event.json;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

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
    public JSONObject build(JSONObject json) {
        JSONObject ret = null; // assume failure

        if (json != null) {
            ret = new JSONObject();
            // JSONObject jsonObj = JSONParser.parseStrict(json).isObject();
            //
            // if (jsonObj != null) {
            //                ret = "{\"type\": \"analysis\", \"message\": {\"id\": \"id_message\", \"text\": \"" //$NON-NLS-1$
            //                        + buildMessageText(jsonObj) + "\"}}"; //$NON-NLS-1$
            // }

            ret.put("type", new JSONString("analysis"));
            JSONObject message = new JSONObject();
            message.put("id", new JSONString("id_message"));
            message.put("text", new JSONString(buildMessageText(json)));
            ret.put("message", message);
        }

        return ret;
    }
}
