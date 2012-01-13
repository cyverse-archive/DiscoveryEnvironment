package org.iplantc.de.client.services;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines an asynchronous callback for raw data "save as" events.
 * 
 * @author amuir
 * 
 */
public class RawDataSaveAsCallback implements AsyncCallback<String> {
    private String idParent;
    private String idOrig;
    private MessageBox wait;

    /**
     * Instantiate from a parent folder id, original file id and message box.
     * 
     * @param idParent parent folder id.
     * @param idOrig original file identifier.
     * @param wait message box to display while waiting.
     */
    public RawDataSaveAsCallback(String idParent, String idOrig, MessageBox wait) {
        this.idParent = idParent;
        this.idOrig = idOrig;
        this.wait = wait;

        if (wait != null) {
            wait.show();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFailure(Throwable caught) {
        ErrorHandler.post(I18N.ERROR.rawDataSaveFailed(), caught);
        wait.close();
    }

    private String buildPayload(final String json) {
        StringBuffer ret = new StringBuffer();

        ret.append("{"); //$NON-NLS-1$

        if (json != null) {
            JSONObject jsonObj = JsonUtil.getObject(json);
            ret.append("\"created\": " + jsonObj.get("created").toString() + ", "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ret.append("\"idParent\": \"" + idParent + "\", "); //$NON-NLS-1$ //$NON-NLS-2$
            ret.append("\"idOrig\": \"" + idOrig + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }

        ret.append("}"); //$NON-NLS-1$

        return ret.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(String result) {
        String json = EventJSONFactory.build(EventJSONFactory.ActionType.SAVE_AS, buildPayload(result));

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);

        wait.close();
    }
}
