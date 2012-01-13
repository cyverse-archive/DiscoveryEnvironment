package org.iplantc.de.client.events;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.utils.NotifyInfo;

import com.google.gwt.json.client.JSONObject;

public class AsyncUploadCompleteHandler extends DefaultUploadCompleteHandler {

    /**
     * {@inheritDoc}
     */
    public AsyncUploadCompleteHandler(String idParent) {
        super(idParent);
    }

    /**
     * {@inheritDoc}
     * "complete" in this case doesn't mean complete but rather "upload has started".
     */
    @Override
    public void onCompletion(String sourceUrl, String response) {
        try {
            JSONObject payload = buildPayload(sourceUrl, response);
            // TODO Need to handle asynchronous file upload and only display
            // file in UI once upload is completed.
            // String json = EventJSONFactory.build(EventJSONFactory.ActionType.UPLOAD_COMPLETE,
            // payload.toString());
            //
            // MessageDispatcher dispatcher = MessageDispatcher.getInstance();
            // dispatcher.processMessage(json);

            String filename = JsonUtil.getString(payload, DiskResource.LABEL);
            NotifyInfo.notify(NotificationManager.Category.DATA, I18N.DISPLAY.urlImport(),
                    I18N.DISPLAY.importRequestSubmit(filename), null);
        } catch (Exception e) {
            ErrorHandler.post(I18N.ERROR.importFailed(sourceUrl), e);
        } finally {
            // TODO: consider having onCompletion and onAfterCompletion called by superclass
            // method to more appropriately confirm w/ Template Method and Command patterns
            onAfterCompletion();
        }
    }

}
