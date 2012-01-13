package org.iplantc.de.client.utils.builders.event.json;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Builder class to create JSON for an upload payload event.
 * 
 * @author amuir
 * 
 */
public class UploadEventJSONBuilder extends AbstractEventJSONBuilder {
    /**
     * Instantiate from an action.
     * 
     * @param action action tag to be added to our payload.
     */
    public UploadEventJSONBuilder(String action) {
        super(action);
    }

    private String buildMessageText(final JSONObject jsonObj) {
        String status = JsonUtil.getString(jsonObj, "status"); //$NON-NLS-1$
        String filename = JsonUtil.getString(jsonObj, "label"); //$NON-NLS-1$

        if (status != null && status.equals("success") && filename != null && !filename.isEmpty()) {
            return I18N.DISPLAY.fileUploadSuccess(filename);
        }

        String sourceUrl = JsonUtil.getString(jsonObj, "sourceUrl"); //$NON-NLS-1$

        return I18N.ERROR.importFailed(sourceUrl);
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
                ret = "{\"type\": \"data\", \"message\": {\"id\": \"someId\", \"text\": \"" //$NON-NLS-1$
                        + buildMessageText(jsonObj) + "\"}, \"payload\": " + buildPayload(jsonObj) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return ret;
    }
}
