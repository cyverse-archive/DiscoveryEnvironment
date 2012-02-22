package org.iplantc.de.client.views.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.DiskResourceServiceCallback;
import org.iplantc.de.client.services.FileEditorServiceFacade;

import com.google.gwt.json.client.JSONObject;

public class DataPreviewPanel extends DataTextAreaPanel {
    public DataPreviewPanel() {
        super();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void updateDisplay() {
        final String idFile = file.getId();
        final String fileName = file.getName();

        FileEditorServiceFacade facade = new FileEditorServiceFacade();
        facade.getManifest(idFile, new DiskResourceServiceCallback() {
            @Override
            public void onSuccess(String result) {
                // Check the current File ID against the requested ID for this response.
                if (!isCurrentFileId(idFile)) {
                    // This response is no longer valid (a race condition occurred).
                    // The last update call will already have hidden or shown this panel appropriately.
                    return;
                }

                if (result == null || result.isEmpty()) {
                    // the response is empty.
                    hide();
                    return;
                }

                // only display the preview panel if a "preview" or "rawcontents" URL is present.
                JSONObject jsonResult = JsonUtil.getObject(result);
                String urlPreview = JsonUtil.getString(jsonResult, "preview"); //$NON-NLS-1$
                String urlDownload = JsonUtil.getString(jsonResult, "rawcontents"); //$NON-NLS-1$

                if (!urlPreview.isEmpty()) {
                    displayPreviewData(urlPreview, new GetPreviewDataCallback(idFile, fileName));
                } else if (!urlDownload.isEmpty()) {
                    displayPreviewData(urlDownload, new GetRawDataCallback(idFile, fileName));
                } else {
                    hide();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                hide();
                super.onFailure(caught);
            }

            @Override
            protected String getErrorMessageDefault() {
                return I18N.ERROR.unableToRetrieveFileManifest(fileName);
            }

            @Override
            protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
                return getErrorMessageForFiles(code, fileName);
            }
        });
    }

    /**
     * Calls the FileEditorServiceFacade.getData service to retrieve data from the given url, and
     * displays the results with the given callback.
     * 
     * @param url
     * @param callback
     */
    private void displayPreviewData(final String url, DiskResourceServiceCallback callback) {
        FileEditorServiceFacade facade = new FileEditorServiceFacade();
        facade.getData(url, callback);
    }

    @Override
    protected String getHeadingText() {
        return I18N.DISPLAY.preview();
    }

    @Override
    protected int getInitialHeight() {
        return 140;
    }

    private boolean isCurrentFileId(String idFile) {
        // check the current File ID against the given File ID. A null file means this display should
        // remain hidden.
        String currentId = ""; //$NON-NLS-1$

        if (file != null) {
            currentId = file.getId();
        }

        return currentId.equals(idFile);
    }

    /**
     * A FileEditorServiceFacade callback for displaying preview data only when the file ID for the
     * response matches the last requested file ID.
     * 
     * @author psarando
     * 
     */
    private abstract class PreviewManifestCallback extends DiskResourceServiceCallback {

        private final String idFile;
        private final String fileName;

        public PreviewManifestCallback(String idFile, String fileName) {
            this.idFile = idFile;
            this.fileName = fileName;
        }

        @Override
        public void onSuccess(String result) {
            // Check the current File ID against the requested ID for this response.
            if (!isCurrentFileId(idFile)) {
                // This response is no longer valid for the current File ID (a race condition occurred).
                // The last update call will already have hidden or shown this panel appropriately.
                return;
            }

            displayValue(result);
            show();
        }

        @Override
        public void onFailure(Throwable caught) {
            hide();
            super.onFailure(caught);
        }

        @Override
        protected String getErrorMessageDefault() {
            return I18N.ERROR.unableToRetrieveFileData(fileName);
        }

        @Override
        protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
            return getErrorMessageForFiles(code, fileName);
        }
    }

    /**
     * A FileEditorServiceFacade callback for displaying preview data.
     * 
     * @author psarando
     * 
     */
    private class GetPreviewDataCallback extends PreviewManifestCallback {

        public GetPreviewDataCallback(String idFile, String fileName) {
            super(idFile, fileName);
        }

        @Override
        public void onSuccess(String result) {
            super.onSuccess(JsonUtil.getString(JsonUtil.getObject(result), "preview")); //$NON-NLS-1$
        }
    }

    /**
     * A FileEditorServiceFacade callback for displaying raw download data.
     * 
     * @author psarando
     * 
     */
    private class GetRawDataCallback extends PreviewManifestCallback {

        public GetRawDataCallback(String idFile, String fileName) {
            super(idFile, fileName);
        }
    }
}
