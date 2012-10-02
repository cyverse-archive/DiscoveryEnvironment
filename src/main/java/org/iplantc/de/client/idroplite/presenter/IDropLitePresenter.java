/**
 * 
 */
package org.iplantc.de.client.idroplite.presenter;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantSubmittableDialog;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.SimpleDownloadWindowDispatcher;
import org.iplantc.de.client.events.AsyncUploadCompleteHandler;
import org.iplantc.de.client.idroplite.util.IDropLiteUtil;
import org.iplantc.de.client.idroplite.views.IDropLiteView;
import org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter;
import org.iplantc.de.client.models.IDropLiteWindowConfig;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceFacade;
import org.iplantc.de.client.views.panels.FileUploadDialogPanel;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

/**
 * @author sriram
 * 
 */
public class IDropLitePresenter implements Presenter {

    private IDropLiteView view;
    private Dialog dlgUpload;
    private final int CONTENT_PADDING = 12;
    final private IDropLiteWindowConfig config;

    public IDropLitePresenter(IDropLiteView view, IDropLiteWindowConfig config) {
        this.view = view;
        this.config = config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter#buildUploadApplet()
     */
    @Override
    public void buildUploadApplet() {
        view.mask();
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        facade.upload(new IDropLiteServiceCallback() {
            @Override
            protected HtmlLayoutContainer buildAppletHtml(JSONObject appletData) {
                int adjustSize = CONTENT_PADDING * 2;

                appletData.put("uploadDest", new JSONString(config.getUploadDest())); //$NON-NLS-1$

                return IDropLiteUtil.getAppletForUpload(appletData, view.getViewWidth()
                        - CONTENT_PADDING, view.getViewHeight() - adjustSize);
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter#buildDownloadApplet()
     */
    @Override
    public void buildDownloadApplet() {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        view.mask();
        facade.download(config.getDownloadPaths(), new IDropLiteServiceCallback() {
            @Override
            protected HtmlLayoutContainer buildAppletHtml(JSONObject appletData) {
                int adjustSize = CONTENT_PADDING * 2;

                return IDropLiteUtil.getAppletForDownload(appletData, view.getViewWidth()
                        - CONTENT_PADDING, view.getViewHeight() - adjustSize);
            }
        });

    }

    /**
     * Common success and failure handling for upload and download service calls.
     * 
     * @author psarando
     * 
     */
    private abstract class IDropLiteServiceCallback implements AsyncCallback<String> {
        /**
         * Builds the Html for the idrop-lite applet from the given JSON applet data returned by the
         * service call.
         * 
         * @param appletData
         * @return Html applet with the given applet params.
         */
        protected abstract HtmlLayoutContainer buildAppletHtml(JSONObject appletData);

        @Override
        public void onSuccess(String response) {
            view.setApplet(buildAppletHtml(JsonUtil.getObject(JsonUtil.getObject(response), "data"))); //$NON-NLS-1$
            view.unmask();
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter#onSimpleUploadClick()
     */
    @Override
    public void onSimpleUploadClick() {
        String uploadDestId = config.getUploadDest();
        String username = UserInfo.getInstance().getUsername();

        // provide key/value pairs for hidden fields
        FastMap<String> hiddenFields = new FastMap<String>();
        hiddenFields.put(FileUploadDialogPanel.HDN_PARENT_ID_KEY, uploadDestId);
        hiddenFields.put(FileUploadDialogPanel.HDN_USER_ID_KEY, username);

        // define a handler for upload completion
        AsyncUploadCompleteHandler handler = new AsyncUploadCompleteHandler(uploadDestId) {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onAfterCompletion() {
                if (dlgUpload != null) {
                    dlgUpload.hide();
                }
            }
        };

        FileUploadDialogPanel pnlUpload = new FileUploadDialogPanel(hiddenFields,
                Constants.CLIENT.fileUploadServlet(), handler, FileUploadDialogPanel.MODE.FILE_AND_URL);

        dlgUpload = new IPlantSubmittableDialog(I18N.DISPLAY.upload(), 536, pnlUpload);
        dlgUpload.show();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter#onSimpleDownloadClick()
     */
    @Override
    public void onSimpleDownloadClick() {
        SimpleDownloadWindowDispatcher dispatcher = new SimpleDownloadWindowDispatcher();
        dispatcher.launchDownloadWindow(config.getFileDownloadPaths());
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        int mode = config.getDisplayMode();
        if (mode == IDropLiteUtil.DISPLAY_MODE_UPLOAD) {
            buildUploadApplet();
        } else if (mode == IDropLiteUtil.DISPLAY_MODE_DOWNLOAD) {
            buildDownloadApplet();
        }
        view.setToolBarButton(mode);
    }
}
