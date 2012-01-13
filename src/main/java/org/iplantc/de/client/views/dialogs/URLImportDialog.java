package org.iplantc.de.client.views.dialogs;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.UploadCompleteHandler;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * A Dialog that enables user to input a file url and an optional description about the file. The url can
 * be either of http or ftp only
 * 
 * @author sriram
 * 
 */
public class URLImportDialog extends Dialog {
    private final FormPanel form;
    private final UploadCompleteHandler handler;
    private Status importStatus;
    private final String parentFolderId;

    /**
     * Instantiate from a parent folder id and handler.
     * 
     * @param parentFolderId id of destination folder.
     * @param handler called when upload completes.
     */
    public URLImportDialog(String parentFolderId, UploadCompleteHandler handler) {
        initDialog();

        form = new FormPanel();
        form.setHideLabels(true);
        form.setHeaderVisible(false);
        form.setAutoHeight(true);
        form.setFieldWidth(475);

        this.handler = handler;
        this.parentFolderId = parentFolderId;

        addUrlField();
        addButtons();
        setHeading(I18N.DISPLAY.urlImport());

        add(form);
    }

    private void initDialog() {
        setButtonAlign(HorizontalAlignment.RIGHT);
        setLayout(new FitLayout());
        setResizable(false);
        setModal(true);
        setHideOnButtonClick(false);
    }

    private void addButtons() {
        importStatus = new Status();
        getHeader().addTool(importStatus);

        setButtons(Dialog.OKCANCEL);

        final Button submitBtn = getOkButton();
        submitBtn.setText(I18N.DISPLAY.importLabel());
        submitBtn.setEnabled(false);

        submitBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                submitForm();
            }
        });

        Button cancelBtn = getButtonById(Dialog.CANCEL);

        cancelBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
    }

    /**
     * Retrieve the OK button for the dialog.
     * 
     * If a more generic retrieval is needed, use the inherited getButtonById() method.
     * 
     * @return a reference to the OK button for the dialog.
     */
    public Button getOkButton() {
        return getButtonById(Dialog.OK);
    }

    private void addUrlField() {
        form.add(new HTML(I18N.DISPLAY.urlPrompt()));

        final TextArea url = new TextArea();
        url.setAllowBlank(false);
        url.setRegex("^(?:ftp|FTP|HTTPS?|https?)://[^/]+/.*[^/ ]$"); //$NON-NLS-1$
        url.getMessages().setRegexText(I18N.DISPLAY.invalidImportUrl());
        url.setName(I18N.DISPLAY.url());
        url.setId(I18N.DISPLAY.url());
        url.setAutoValidate(true);

        url.addListener(Events.Valid, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                setOkButtonEnabled(true);
            }
        });

        url.addListener(Events.Invalid, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                setOkButtonEnabled(false);
            }
        });

        url.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                setOkButtonEnabled(url.isValid());

                if (url.isValid()) {
                    if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                        submitForm();
                    }
                }
            }

        });

        form.add(url);
    }

    private void setOkButtonEnabled(boolean state) {
        getOkButton().setEnabled(state);
    }

    /**
     * submit url and the description to the service
     */
    private void submitForm() {
        if (form.isValid()) {
            importStatus.setBusy(I18N.DISPLAY.fileImportStatus());
            List<Field<?>> fields = form.getFields();

            String url = null;
            String description = null;

            for (Field<?> f : fields) {
                if (f.getId().equals(I18N.DISPLAY.url())) {
                    url = (String)f.getValue();
                } else if (f.getId().equals(I18N.DISPLAY.description())) {
                    description = (String)f.getValue();
                }
            }

            setOkButtonEnabled(false);

            callImportService(URL.encode(url),
                    parentFolderId + "/" + DataUtils.parseNameFromPath(URL.encode(url)), //$NON-NLS-1$
                    (description != null) ? JsonUtil.escapeNewLine(description) : ""); //$NON-NLS-1$

            hide();
        }
    }

    /**
     * Calls the import from URL service.
     * 
     * @param url Location to import from.
     * @param dest The full path where the import should be saved.
     * @param description Description of the file to import.
     */
    private void callImportService(final String url, final String dest, final String description) {
        // setup an importFromUrl success/failure callback.
        final AsyncCallback<String> importCallback = new AsyncCallback<String>() {
            @Override
            public void onSuccess(String response) {
                handler.onCompletion(url, response);
                importStatus.clearStatus(null);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.importFailed(url), caught);

                handler.onAfterCompletion();
                importStatus.clearStatus(null);
            }
        };

        // check if file already exists before attempting to import
        final FolderServiceFacade facade = new FolderServiceFacade();

        AsyncCallback<String> dupCheckCallback = new AsyncCallback<String>() {

            @Override
            public void onSuccess(String newResourceIds) {
                facade.importFromUrl(url, dest, description, importCallback);
            }

            @Override
            public void onFailure(Throwable caught) {
                // could not check for duplicate file, so abort import
                importCallback.onFailure(caught);
            }
        };

        DataUtils.checkForDuplicateFilename(dest, dupCheckCallback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        getButtonBar().setStyleAttribute("outline", "none"); //$NON-NLS-1$ //$NON-NLS-2$
        getButtonBar().getElement().setTabIndex(-1);
        int tabIndex = 1;

        List<Field<?>> fields = form.getFields();
        for (Field<?> f : fields) {
            f.getElement().setTabIndex(tabIndex++);
        }
        getButtonBar().getItem(0).getElement().setTabIndex(tabIndex++);
        getButtonBar().getItem(1).getElement().setTabIndex(tabIndex++);

        setFocusWidget(form.getItemByItemId(I18N.DISPLAY.url()));

        setWidth(536);
    }
}
