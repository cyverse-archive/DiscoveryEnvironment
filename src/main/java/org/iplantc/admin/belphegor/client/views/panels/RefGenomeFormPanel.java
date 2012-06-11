/**
 * 
 */
package org.iplantc.admin.belphegor.client.views.panels;

import java.util.Date;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.models.CASCredentials;
import org.iplantc.admin.belphegor.client.models.ReferenceGenome;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.admin.belphegor.client.util.FormFieldBuilderUtil;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

/**
 * @author sriram
 * 
 */
public class RefGenomeFormPanel extends LayoutContainer {

    private HiddenField<String> uuid;
    private TextField<String> userName;
    private TextField<String> path;
    private CheckBox chkDeleted;
    private TextField<String> refGenName;
    private DateField creationDate;
    private TextField<String> lastModUserName;
    private DateField lastModDate;

    private Button btnSubmit;
    private Button btnCancel;

    private FormPanel panel;

    private FormData formData;

    public static enum MODE {
        EDIT, ADD

    };

    public RefGenomeFormPanel(ReferenceGenome model, MODE mode, AdminServiceCallback callback) {
        initForm();
        buildFields();
        addFields();
        buildButtons();
        add(panel);
        if (model != null) {
            setValues(model);
        }

        if (mode.equals(MODE.ADD)) {
            setDefaults();
            userName.setEnabled(false);
            lastModUserName.setEnabled(false);
            creationDate.setEnabled(false);
            lastModDate.setEnabled(false);
            chkDeleted.setEnabled(false);
        }
    }

    private void setDefaults() {
        CASCredentials cas = CASCredentials.getInstance();
        userName.setValue(cas.getUsername());
        lastModUserName.setValue(cas.getUsername());
        creationDate.setValue(new Date());
        lastModDate.setValue(new Date());
    }

    private void initForm() {
        panel = new FormPanel();
        formData = new FormData("95%");
        panel.setHeaderVisible(false);
        panel.setSize(595, 350);
        panel.setBodyBorder(false);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setLayout(new FormLayout(LabelAlign.TOP));
    }

    private void buildFields() {
        userName = FormFieldBuilderUtil.buildTextField("Created By", false, null, "userName", null, 255);
        path = FormFieldBuilderUtil.buildTextField("Path", false, null, "path", null, 1024);
        refGenName = FormFieldBuilderUtil.buildTextField("Name", false, null, "refGenName", null, 512);
        creationDate = FormFieldBuilderUtil.buildDateField("Creation Date");
        lastModUserName = FormFieldBuilderUtil.buildTextField("Last Modified By", true, null,
                "lstModUser", null, 255);
        lastModDate = FormFieldBuilderUtil.buildDateField("Last Modified Date");
        chkDeleted = new CheckBox();
        chkDeleted.setFieldLabel("Deleted");
    }

    private void addFields() {
        panel.add(refGenName, formData);
        panel.add(path, formData);
        panel.add(userName, formData);
        panel.add(creationDate, formData);
        panel.add(lastModDate, formData);
        panel.add(lastModUserName, formData);
        panel.add(chkDeleted, formData);
    }

    private void buildButtons() {
        buildSubmitButton();
        buildCancelButton();
        panel.addButton(btnSubmit);
        panel.addButton(btnCancel);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(btnSubmit);
    }

    private void buildSubmitButton() {
        btnSubmit = new Button();

        btnSubmit.setText(I18N.DISPLAY.save());
        btnSubmit.setId("idBtnSubmit"); //$NON-NLS-1$

        btnSubmit.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                submit();
            }
        });
    }

    private void submit() {
        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade();
        // facade.updateApplication(toJson(), closeCallback);
    }

    private void buildCancelButton() {
        btnCancel = new Button();

        btnCancel.setText(I18N.DISPLAY.cancel());
        btnCancel.setId("idBtnCancel"); //$NON-NLS-1$
    }

    /**
     * Adds a SelectionListener to the Cancel button (e.g. for hiding parent components).
     * 
     * @param listener
     */
    public void addCancelButtonSelectionListener(SelectionListener<ButtonEvent> listener) {
        if (listener != null) {
            btnCancel.addSelectionListener(listener);
        }
    }

    private void setValues(ReferenceGenome genome) {
        refGenName.setValue((String)genome.get(ReferenceGenome.NAME));
        path.setValue((String)genome.get(ReferenceGenome.PATH));
        creationDate.setValue((Date)genome.get(ReferenceGenome.CREATED_ON));
        userName.setValue((String)genome.get(ReferenceGenome.CREATED_BY));
    }
}
