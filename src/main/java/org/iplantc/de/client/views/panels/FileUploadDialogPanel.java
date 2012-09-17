package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AsyncUploadCompleteHandler;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.services.callbacks.DiskResouceDuplicatesCheckCallback;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel component for uploading files.
 *
 * @author lenards
 *
 */
public class FileUploadDialogPanel extends IPlantDialogPanel {
    private static final String ID_WRAP = "idWrap"; //$NON-NLS-1$

    private static final String ID_FILE_UPLD = "idFileUpld"; //$NON-NLS-1$

    private static final String ID_BTN_RESET = "idBtnReset"; //$NON-NLS-1$

    private static final int FIELD_WIDTH = 475;

    private static final String URL_REGEX = "^(?:ftp|FTP|HTTPS?|https?)://[^/]+/.*[^/ ]$"; //$NON-NLS-1$

    public static final String HDN_USER_ID_KEY = "user"; //$NON-NLS-1$
    public static final String HDN_PARENT_ID_KEY = "parentfolderid"; //$NON-NLS-1$
    public static final String FILE_TYPE = "type"; //$NON-NLS-1$
    public static final String URL_FIELD = "url"; //$NON-NLS-1$

    private static final int MAX_UPLOADS = 5;

    private final FormPanel form;
    private final ContentPanel pnlLayout;
    private final AsyncUploadCompleteHandler hdlrUpload;
    private final Status fileStatus;
    private final List<FileUploadField> fupload;
    private final List<TextArea> urls;
    private final String destFolder;
    private final MODE mode;

    public static enum MODE {
        URL_ONLY, FILE_ONLY, FILE_AND_URL
    };

    /**
     * Instantiate from hidden fields, URL, and handler.
     *
     * @param hiddenFields collection of hidden form fields.
     * @param servletActionUrl servlet URL for the upload action.
     * @param handler handler to be executed on upload completion.
     */
    public FileUploadDialogPanel(FastMap<String> hiddenFields, String servletActionUrl,
            AsyncUploadCompleteHandler handler, MODE mode) {
        hdlrUpload = handler;
        this.mode = mode;
        destFolder = hiddenFields.get(HDN_PARENT_ID_KEY);

        form = new FormPanel();
        fupload = new ArrayList<FileUploadField>();
        urls = new ArrayList<TextArea>();

        fileStatus = buildFileStatus();

        initForm(servletActionUrl);
        buildInternalLayout(hiddenFields);

        pnlLayout = new ContentPanel();
        pnlLayout.setLayoutData(new FitLayout());
        pnlLayout.setHeaderVisible(false);
        pnlLayout.setHeight(350);
        pnlLayout.add(form);
    }

    private void initForm(String servletActionUrl) {
        form.setStyleName("iplantc-form-layout-panel"); //$NON-NLS-1$

        form.setHideLabels(true);
        form.setHeaderVisible(false);
        form.setFieldWidth(FIELD_WIDTH);

        form.setAction(servletActionUrl);
        form.setMethod(Method.POST);
        form.setEncoding(Encoding.MULTIPART);
        form.setHeight(350);
        form.setScrollMode(Scroll.AUTOY);
        form.addListener(Events.Submit, new SubmitListener());
    }

    private void buildInternalLayout(FastMap<String> hiddenFields) {

        form.add(fileStatus);

        // add any key/value pairs provided as hidden field
        for (String field : hiddenFields.keySet()) {
            Hidden hdn = new Hidden(field, hiddenFields.get(field));
            form.add(hdn);
        }

        if (mode != MODE.URL_ONLY) {
            // then add the visual widgets
            form.add(new HTML(I18N.DISPLAY.fileUploadMaxSizeWarning()));
            for (int i = 0; i < MAX_UPLOADS; i++) {
                HorizontalPanel uploadFieldPnl = buildFileUpload(i);
                fupload.add((FileUploadField)uploadFieldPnl.getItemByItemId(ID_FILE_UPLD + i));
                form.add(uploadFieldPnl);
            }
        }

        if (mode == MODE.FILE_AND_URL) {
            form.add(new HTML("<br/>")); //$NON-NLS-1$
        }

        if (mode != MODE.FILE_ONLY) {
            form.add(new HTML(I18N.DISPLAY.urlPrompt()));

            for (int i = 0; i < MAX_UPLOADS; i++) {
                TextArea url = buildUrlField();
                urls.add(url);
                form.add(url);
            }
        }
    }

    private TextArea buildUrlField() {
        TextArea url = new TextArea();

        url.setName(URL_FIELD);
        url.setWidth(FIELD_WIDTH);

        url.setAllowBlank(true);
        url.setAutoValidate(true);
        url.setRegex(URL_REGEX);
        url.getMessages().setRegexText(I18N.DISPLAY.invalidImportUrl());

        url.addListener(Events.Valid, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                validateForm();

            }

        });

        url.addListener(Events.Invalid, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                getOkButton().setEnabled(false);

            }

        });

        url.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    handleOkClick();
                }
            }
        });

        return url;
    }

    private Status buildFileStatus() {
        Status ret = new Status();

        ret.setStyleName("iplantc-file-status"); //$NON-NLS-1$
        ret.setText(I18N.DISPLAY.fileUploadFolder(destFolder));
        return ret;
    }

    private HorizontalPanel buildFileUpload(int index) {
        HorizontalPanel wrapper = new HorizontalPanel();
        wrapper.setSpacing(5);
        wrapper.setId(ID_WRAP + index);
        FileUploadField ret = new FileUploadField();
        ret.setId(ID_FILE_UPLD + index);
        ret.setName("file"); //$NON-NLS-1$
        ret.addListener(Events.OnChange, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                FileUploadField fileUploadField = (FileUploadField)be.getBoxComponent();
                fileUploadField.setValue(fileUploadField.getValue().replaceAll(".*[\\\\/]", ""));
            }

        });
        ret.addListener(Events.Valid, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                validateForm();
            }

        });

        ret.addListener(Events.Invalid, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                getOkButton().setEnabled(false);
            }

        });
        ret.setAutoValidate(true);
        ret.setWidth(275);

        wrapper.add(ret);
        wrapper.add(new Html("&nbsp;&nbsp;&nbsp;")); //$NON-NLS-1$
        wrapper.add(buildResetButton(ret));

        return wrapper;
    }

    private Button buildResetButton(final FileUploadField field) {
        Button reset = new Button();
        reset.setToolTip(I18N.DISPLAY.reset());
        reset.setId(ID_BTN_RESET);
        reset.setIcon(AbstractImagePrototype.create(Resources.ICONS.arrow_undo()));
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                field.reset();
            }
        });

        return reset;
    }

    private void validateForm() {
        boolean fileStatusIsBusy = "x-status-busy".equals(fileStatus.getIconStyle()); //$NON-NLS-1$

        getOkButton().setEnabled(isValidUploadForm() && !fileStatusIsBusy);
    }

    private void initOkButton() {
        Button btnParentOk = getOkButton();
        btnParentOk.setText(I18N.DISPLAY.upload());
        btnParentOk.disable();
    }

    private void doUpload() {
        if (isValidUploadForm()) {
            fileStatus.setBusy(fileStatus.getText());
            fileStatus.show();

            getOkButton().disable();

            // check for duplicate files already on the server, excluding any invalid upload fields
            final FastMap<TextField<String>> destResourceMap = new FastMap<TextField<String>>();
            if (mode != MODE.URL_ONLY) {
                for (FileUploadField uploadField : fupload) {
                    // Remove any path from the filename.
                    if (uploadField.getValue() != null) {
                        String filename = uploadField.getValue().replaceAll(".*[\\\\/]", ""); //$NON-NLS-1$//$NON-NLS-2$
                        boolean validFilename = isValidFilename(filename);
                        uploadField.setEnabled(validFilename);
                        if (validFilename) {
                            destResourceMap.put(buildResourceId(filename), uploadField);
                        }
                    }
                }
            }

            if (mode != MODE.FILE_ONLY) {
                for (TextArea urlField : urls) {
                    String url = urlField.getValue();
                    boolean validUrl = isValidFilename(url);

                    urlField.setEnabled(validUrl);

                    if (validUrl) {
                        urlField.setValue(url.trim());
                        destResourceMap.put(buildResourceId(DiskResourceUtil.parseNameFromPath(url)),
                                urlField);
                    }
                }
            }

            if (!destResourceMap.isEmpty()) {
                List<String> ids = new ArrayList<String>();
                ids.addAll(destResourceMap.keySet());
                DataUtils.checkListForDuplicateFilenames(ids, new CheckDuplicatesCallback(ids,
                        destResourceMap));
            }
        } else {
            ErrorHandler.post(I18N.ERROR.invalidFilenameEntered(), null);
        }
    }

    private String buildResourceId(String filename) {
        return destFolder + "/" + filename; //$NON-NLS-1$
    }

    private boolean isValidUploadForm() {
        if (mode != MODE.URL_ONLY) {
            for (FileUploadField uploadField : fupload) {
                String filename = uploadField.getValue();
                if (isValidFilename(filename)) {
                    return true;
                }
            }
        }

        if (mode != MODE.FILE_ONLY) {
            for (TextArea urlField : urls) {
                if (isValidFilename(urlField.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValidFilename(String filename) {
        return filename != null && !filename.trim().isEmpty() && !filename.equalsIgnoreCase("null"); //$NON-NLS-1$
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

    private final class CheckDuplicatesCallback extends DiskResouceDuplicatesCheckCallback {

        private final FastMap<TextField<String>> destResourceMap;

        public CheckDuplicatesCallback(List<String> diskResourceIds,
                FastMap<TextField<String>> destResourceMap) {
            super(diskResourceIds);
            this.destResourceMap = destResourceMap;
        }

        @Override
        public void markDuplicates(List<String> duplicates) {
            if (destResourceMap != null && duplicates != null && duplicates.size() > 0) {
                for (String id : duplicates) {
                    @SuppressWarnings("rawtypes")
                    Field f = destResourceMap.get(buildResourceId(id));
                    f.markInvalid(I18N.ERROR.fileExist());
                }
                fileStatus.clearStatus(fileStatus.getText());
                return;
            } else {
                if (mode != MODE.URL_ONLY) {
                    for (int i = 0; i < MAX_UPLOADS; i++) {
                        if (!isValidFilename(fupload.get(i).getValue())) {
                            form.remove(form.getItemByItemId(ID_WRAP + i));
                        }
                    }
                }
                form.submit();
            }
        }
    }

    private class SubmitListener implements Listener<FormEvent> {
        @Override
        public void handleEvent(FormEvent fe) {
            String response = fe.getResultHtml();

            try {
                JSONObject jsonResponse = JsonUtil.getObject(JsonUtil.formatString(response));
                JSONArray results = JsonUtil.getArray(jsonResponse, "results"); //$NON-NLS-1$
                if (results == null) {
                    throw new Exception(response);
                }

                for (int i = 0; i < results.size(); i++) {
                    JSONObject jsonFileUploadStatus = JsonUtil.getObjectAt(results, i);
                    if (jsonFileUploadStatus != null) {
                        String action = JsonUtil.getString(jsonFileUploadStatus, "action"); //$NON-NLS-1$

                        if (action.equals("file-upload")) { //$NON-NLS-1$
                            JSONObject file = JsonUtil.getObject(jsonFileUploadStatus, "file"); //$NON-NLS-1$

                            if (file != null) {
                                hdlrUpload.onCompletion(JsonUtil.getString(file, File.LABEL),
                                        file.toString());
                            }
                        } else if (action.equals("url-upload")) { //$NON-NLS-1$
                            String sourceUrl = JsonUtil.getString(jsonFileUploadStatus, "url"); //$NON-NLS-1$

                            hdlrUpload.onImportSuccess(sourceUrl, jsonFileUploadStatus.toString());
                        }
                    }
                }
            } catch (Exception caught) {
                String firstFileName = ""; //$NON-NLS-1$

                if (!fupload.isEmpty()) {
                    firstFileName = fupload.get(0).getValue();
                } else if (!urls.isEmpty()) {
                    firstFileName = DiskResourceUtil.parseNameFromPath(urls.get(0).getValue());
                }

                ErrorHandler.post(I18N.ERROR.fileUploadFailed(firstFileName), caught);
                hdlrUpload.onAfterCompletion();
            }

            // we're done, so clear the busy notification
            fileStatus.clearStatus(fileStatus.getText());
        }
    }
}
