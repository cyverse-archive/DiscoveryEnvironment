package org.iplantc.de.client.events;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.notifications.util.NotificationHelper;
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
     * Notify user that the upload has successfully started.
     * 
     * @param sourceUrl
     * @param response
     */
    public void onImportSuccess(String sourceUrl, String response) {
        try {
            JSONObject payload = buildPayload(sourceUrl, response);
            // TODO Is it possible to only display file in UI once asynchronous upload is complete?
            String filename = JsonUtil.getString(payload, DiskResource.LABEL);
            NotifyInfo.notify(NotificationHelper.Category.DATA, I18N.DISPLAY.urlImport(),
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
