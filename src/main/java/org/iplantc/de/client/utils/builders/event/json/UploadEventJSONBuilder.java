package org.iplantc.de.client.utils.builders.event.json;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.I18N;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

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
        String filename = JsonUtil.getString(jsonObj, File.LABEL);

        if (!filename.isEmpty()) {
            return I18N.DISPLAY.fileUploadSuccess(filename);
        }

        String sourceUrl = JsonUtil.getString(jsonObj, "sourceUrl"); //$NON-NLS-1$

        return I18N.ERROR.importFailed(sourceUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject build(final JSONObject json) {
        JSONObject ret = null; // assume failure

        if (json != null) {
            ret = new JSONObject();
            ret.put("type", new JSONString("data"));
            ret.put("subject", new JSONString(buildMessageText(json)));
            ret.put("payload", buildPayload(json));
        }

        return ret;
    }
}
