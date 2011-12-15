package org.iplantc.admin.belphegor.client.services;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A common callback for disk resource service calls that implements a common onFailure method that will
 * parse JSON error messages from the given Throwable::getMessage method to display to the user.
 * 
 * @author psarando
 * 
 */
public abstract class AdminServiceCallback implements AsyncCallback<String> {
    public static final String SUCCESS = "success"; //$NON-NLS-1$

    protected Component maskedCaller;

    public void setMaskedCaller(Component maskedCaller) {
        this.maskedCaller = maskedCaller;
    }

    protected void unmaskCaller() {
        if (maskedCaller != null) {
            maskedCaller.unmask();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jsonResult = getJsonResponse(result);

            onSuccess(jsonResult);

            unmaskCaller();
        } catch (Throwable e) {
            onFailure(e);
        }
    }

    /**
     * Builds a JSON object used for MessageDispatcher event JSON.
     * 
     * @param jsonResult The parsed JSON results of the successful service call.
     * @return JSON object used for MessageDispatcher event JSON.
     */
    protected abstract void onSuccess(final JSONObject jsonResult);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure(Throwable caught) {
        unmaskCaller();

        ErrorHandler.post(getErrorMessage(), caught);
    }

    /**
     * Gets a default error message to display to the user on failure, if no error code could be parsed
     * from the service response.
     * 
     * @return Default error message to display to the user.
     */
    protected abstract String getErrorMessage();

    /**
     * Parses result as a JSON object, checks for a failure status field, then returns the parsed object.
     * Throws an exception with result as its message if the given string is not a JSON object or if a
     * status failure field was parsed from the object.
     * 
     * @param result The JSON object string.
     * @return The parsed JSON object.
     * @throws Throwable if result is not a JSON object or if a failure status is parsed from result.
     */
    protected JSONObject getJsonResponse(String result) throws Throwable {

        JSONObject ret = JsonUtil.getObject(result);
        if (ret == null) {
            throw new Exception(result);
        }

        if (!JsonUtil.getBoolean(ret, SUCCESS, true)) {
            throw new Exception(result);
        }

        return ret;
    }
}
