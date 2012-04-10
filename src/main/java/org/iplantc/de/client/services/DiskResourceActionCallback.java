package org.iplantc.de.client.services;

import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.google.gwt.json.client.JSONObject;

/**
 * A common callback for disk resource service calls that require an action to be dispatched via the
 * MessageDispatcher, encoded in a JSON payload by the EventJSONFactory.
 * 
 * @author psarando
 * 
 */
public abstract class DiskResourceActionCallback extends DiskResourceServiceCallback {
    /**
     * @return The ActionType for this callback, used to build MessageDispatcher event JSON.
     */
    protected abstract EventJSONFactory.ActionType getActionType();

    /**
     * Builds a JSON object used for MessageDispatcher event JSON.
     * 
     * @param jsonResult The parsed JSON results of the successful service call.
     * @return JSON object used for MessageDispatcher event JSON.
     */
    protected abstract JSONObject buildPayload(final JSONObject jsonResult);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jsonResult = getJsonResponse(result);
            JSONObject jsonPayload = buildPayload(jsonResult);

            JSONObject json = EventJSONFactory.build(getActionType(), jsonPayload);

            MessageDispatcher.getInstance().processMessage(json);

            unmaskCaller();
        } catch (Throwable e) {
            onFailure(e);
        }
    }
}
