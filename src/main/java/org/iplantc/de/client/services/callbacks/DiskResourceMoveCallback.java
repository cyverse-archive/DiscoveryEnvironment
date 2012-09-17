package org.iplantc.de.client.services.callbacks;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.DataPayloadEvent;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * An AsyncCallback that implements a common onFailure method, called when disk resources are moved. Move
 * actions do not use the MessageDispatcher and do not have an ActionType.
 * 
 * @author psarando
 * 
 */
public abstract class DiskResourceMoveCallback extends DiskResourceServiceCallback {

    protected JSONObject buildPayload(JSONObject response) {
        JSONObject payload = new JSONObject();

        // first build data object
        JSONObject data = new JSONObject();

        JSONArray paths = JsonUtil.getArray(response, "sources"); //$NON-NLS-1$

        data.put("paths", paths); //$NON-NLS-1$
        data.put("destination", response.get("dest")); //$NON-NLS-1$ //$NON-NLS-2$

        // build final payload object
        payload.put("action", new JSONString(getPayloadAction())); //$NON-NLS-1$
        payload.put("data", data); //$NON-NLS-1$

        return payload;
    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jsonResult = getJsonResponse(result);

            DataPayloadEvent event = new DataPayloadEvent(null, buildPayload(jsonResult));
            EventBus.getInstance().fireEvent(event);

            unmaskCaller();
        } catch (Throwable e) {
            onFailure(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.moveFailed();
    }

    /**
     * Gets the payload JSON "action" value for this callback ("file_move" or "folder_move").
     * 
     * @return The payload JSON "action" value for this callback.
     */
    protected abstract String getPayloadAction();
}
