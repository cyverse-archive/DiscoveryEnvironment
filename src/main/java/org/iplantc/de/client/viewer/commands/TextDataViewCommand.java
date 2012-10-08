package org.iplantc.de.client.viewer.commands;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceCallback;
import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.de.client.viewer.views.TextViewerImpl;

import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;

/**
 * 
 * 
 * @author sriram
 * 
 */
public class TextDataViewCommand implements ViewCommand {

    @Override
    public FileViewer execute(final FileIdentifier file) {
        final FileViewer view = new TextViewerImpl();
        String url = "file/preview?user=" + URL.encodeQueryString(UserInfo.getInstance().getUsername())
                + "&path=" + URL.encodeQueryString(file.getFileId());
        Services.FILE_EDITOR_SERVICE.getData(url, new DiskResourceServiceCallback(null) {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                view.setData(JsonUtil.getString(JsonUtil.getObject(result), "preview"));
            }

            @Override
            protected String getErrorMessageDefault() {
                return I18N.ERROR.unableToRetrieveFileData(file.getFilename());
            }

            @Override
            protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
                return getErrorMessageForFiles(code, file.getFilename());
            }
        });

        return view;
    }
}
