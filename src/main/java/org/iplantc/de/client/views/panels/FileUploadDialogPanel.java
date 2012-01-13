package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.UploadCompleteHandler;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel component for uploading files.
 * 
 * @author lenards
 * 
 */
public class FileUploadDialogPanel extends IPlantDialogPanel {
    public static final String HDN_USER_ID_KEY = "user"; //$NON-NLS-1$
    public static final String HDN_PARENT_ID_KEY = "parentfolderid"; //$NON-NLS-1$
    public static final String FILE_TYPE = "type"; //$NON-NLS-1$
    private static final int MAX_UPLOADS = 5;

    private final FormPanel form;
    private final VerticalPanel pnlLayout;
    private final UploadCompleteHandler hdlrUpload;
    private final Status fileStatus;
    private final List<FileUpload> fupload;
    private final String destFolder;

    /**
     * Instantiate from hidden fields, URL, and handler.
     * 
     * @param hiddenFields collection of hidden form fields.
     * @param servletActionUrl servlet URL for the upload action.
     * @param handler handler to be executed on upload completion.
     */
    public FileUploadDialogPanel(FastMap<String> hiddenFields, String servletActionUrl,
            UploadCompleteHandler handler) {
        hdlrUpload = handler;
        destFolder = hiddenFields.get(HDN_PARENT_ID_KEY);

        form = new FormPanel();
        form.setHeaderVisible(false);
        form.setAction(servletActionUrl);
        form.setMethod(Method.POST);
        form.setEncoding(Encoding.MULTIPART);

        fileStatus = buildFileStatus();

        VerticalPanel vpnlWidget = new VerticalPanel();
        vpnlWidget.setSpacing(5);

        VerticalPanel pnlInternalLayout = new VerticalPanel();
        pnlInternalLayout.setStyleName("iplantc-form-internal-layout-panel"); //$NON-NLS-1$

        // add any key/value pairs provided as hidden field
        for (String field : hiddenFields.keySet()) {
            Hidden hdn = new Hidden(field, hiddenFields.get(field));
            pnlInternalLayout.add(hdn);
        }

        // then add the visual widgets
        fupload = new ArrayList<FileUpload>();
        for (int i = 0; i < MAX_UPLOADS; i++) {
            FileUpload uploadField = buildFileUpload();
            fupload.add(uploadField);
            pnlInternalLayout.add(uploadField);
        }

        pnlInternalLayout.add(fileStatus);

        vpnlWidget.add(new LabelField(I18N.DISPLAY.fileUploadMaxSizeWarning()));
        vpnlWidget.add(pnlInternalLayout);

        form.add(vpnlWidget);

        form.addListener(Events.Submit, new Listener<FormEvent>() {
            @Override
            public void handleEvent(FormEvent fe) {
                String response = fe.getResultHtml();

                try {
                    JSONObject jsonResponse = JsonUtil.getObject(JsonUtil.formatString(response));
                    JSONArray results = JsonUtil.getArray(jsonResponse, "results"); //$NON-NLS-1$
                    if (results == null) {
                        throw new Exception(I18N.ERROR.fileUploadFailed(fupload.get(0).getFilename()));
                    }

                    for (int i = 0; i < results.size(); i++) {
                        JSONObject jsonFileUploadStatus = results.get(i).isObject();
                        if (jsonFileUploadStatus != null) {
                            hdlrUpload.onCompletion(JsonUtil.getString(jsonFileUploadStatus, "label"), //$NON-NLS-1$
                                    jsonFileUploadStatus.toString());
                        }
                    }
                } catch (Exception e) {
                    hdlrUpload.onCompletion(fupload.get(0).getFilename(), response);
                }

                // we're done, so clear the busy notification
                fileStatus.clearStatus(""); //$NON-NLS-1$
            }
        });

        form.setStyleName("iplantc-form-layout-panel"); //$NON-NLS-1$

        pnlLayout = new VerticalPanel();
        pnlLayout.setLayoutData(new FitLayout());
        pnlLayout.add(form);
    }

    private Status buildFileStatus() {
        Status ret = new Status();

        ret.setStyleName("iplantc-file-status"); //$NON-NLS-1$

        return ret;
    }

    private FileUpload buildFileUpload() {
        FileUpload ret = new FileUpload();

        ret.setStyleName("iplantc-file-upload"); //$NON-NLS-1$
        ret.setName("file"); //$NON-NLS-1$

        ret.addChangeHandler(new ChangeHandler() {
            /**
             * When the file upload has changed, enable the upload button.
             * 
             * This is only fired when an actual file is selected, not merely when the browse button is
             * clicked.
             */
            @Override
            public void onChange(ChangeEvent event) {
                validateForm();
            }
        });

        return ret;
    }

    private void validateForm() {
        getOkButton().setEnabled(isValidUploadForm());
    }

    private void initOkButton() {
        Button btnParentOk = getOkButton();
        btnParentOk.setText(I18N.DISPLAY.upload());
        btnParentOk.disable();
    }

    private void doUpload() {
        if (isValidUploadForm()) {
            fileStatus.setBusy(""); //$NON-NLS-1$
            fileStatus.show();

            getOkButton().disable();

            // check for duplicate files already on the server, excluding any invalid upload fields
            final List<String> destResourceIds = new ArrayList<String>();
            for (FileUpload uploadField : fupload) {
                String filename = uploadField.getFilename().replaceAll(".*[\\\\/]", ""); //$NON-NLS-1$//$NON-NLS-2$
                boolean validFilename = isValidFilename(filename);

                uploadField.setEnabled(validFilename);

                if (validFilename) {
                    destResourceIds.add(buildResourceId(filename));
                }
            }

            if (!destResourceIds.isEmpty()) {
                DataUtils.checkListForDuplicateFilenames(destResourceIds, new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String response) {
                        form.submit();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught.getMessage(), caught);
                        hdlrUpload.onAfterCompletion();
                    }
                });
            }
        } else {
            ErrorHandler.post(I18N.ERROR.invalidFilenameEntered(), null);
        }
    }

    private String buildResourceId(String filename) {
        return destFolder + "/" + filename; //$NON-NLS-1$
    }

    private boolean isValidUploadForm() {
        for (FileUpload uploadField : fupload) {
            String filename = uploadField.getFilename();
            if (isValidFilename(filename)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidFilename(String filename) {
        return filename != null && !filename.isEmpty() && !filename.equalsIgnoreCase("null"); //$NON-NLS-1$
    }

    private Button getOkButton() {
        return (Button)parentButtons.getItemByItemId(Dialog.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getDisplayWidget() {
        return pnlLayout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        fileStatus.show();
        doUpload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setButtonBar(ButtonBar buttons) {
        super.setButtonBar(buttons);
        initOkButton();
    }
}