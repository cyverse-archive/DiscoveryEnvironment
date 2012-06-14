/**
 * 
 */
package org.iplantc.admin.belphegor.client.views.panels;

import java.util.Date;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.models.CASCredentials;
import org.iplantc.admin.belphegor.client.models.ReferenceGenome;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.ReferenceGenomesServiceFacade;
import org.iplantc.admin.belphegor.client.util.FormFieldBuilderUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author sriram
 * 
 */
public class RefGenomeFormPanel extends LayoutContainer {

    private HiddenField<String> id;
    private HiddenField<String> uuid;
    private TextField<String> userName;
    private TextField<String> path;
    private CheckBox chkDeleted;
    private CheckBoxGroup chkGroup;
    private TextField<String> refGenName;
    private DateField creationDate;
    private TextField<String> lastModUserName;
    private DateField lastModDate;

    private Button btnSubmit;
    private Button btnCancel;

    private FormPanel panel;

    private FormData formData;

    private MODE mode;

    private AdminServiceCallback callback;

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

        this.mode = mode;
        this.callback = callback;

        userName.setEnabled(false);
        lastModUserName.setEnabled(false);
        creationDate.setEnabled(false);
        lastModDate.setEnabled(false);

        if (mode.equals(MODE.ADD)) {
            setDefaults();
            chkDeleted.setEnabled(false);
        }
    }

    private JSONObject toJson() {
        JSONObject obj = new JSONObject();

        obj.put(ReferenceGenome.NAME, (refGenName.getValue() == null) ? new JSONString("")
                : new JSONString(refGenName.getValue()));
        obj.put(ReferenceGenome.PATH, (path.getValue() == null) ? new JSONString("") : new JSONString(
                path.getValue()));
        if (mode.equals(MODE.EDIT)) {
            obj.put(ReferenceGenome.UUID, (uuid.getValue() == null) ? new JSONString("")
                    : new JSONString(uuid.getValue()));
            obj.put(ReferenceGenome.DELETED,
                    (chkDeleted.getValue() == null) ? JSONBoolean.getInstance(false) : JSONBoolean
                            .getInstance(chkDeleted.getValue()));
        }
        System.out.println(obj.toString());
        return obj;

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
        panel.setSize(595, 375);
        panel.setBodyBorder(false);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setLayout(new FormLayout(LabelAlign.TOP));
    }

    private void buildFields() {
        userName = FormFieldBuilderUtil.buildTextField(I18N.DISPLAY.createdBy(), false, null,
                "userName", null, 255);
        path = FormFieldBuilderUtil.buildTextField(I18N.DISPLAY.refGenPath(), false, null, "path", null,
                1024);
        refGenName = FormFieldBuilderUtil.buildTextField(I18N.DISPLAY.refGenName(), false, null,
                "refGenName", null, 512);
        creationDate = FormFieldBuilderUtil.buildDateField(I18N.DISPLAY.createdOn(), false);
        lastModUserName = FormFieldBuilderUtil.buildTextField(I18N.DISPLAY.lastModBy(), true, null,
                "lstModUser", null, 255);
        lastModDate = FormFieldBuilderUtil.buildDateField(I18N.DISPLAY.lastModOn(), true);
        chkDeleted = new CheckBox();
        chkDeleted.setBoxLabel(I18N.DISPLAY.deleted());
        id = new HiddenField<String>();
        id.setId("idId");
        uuid = new HiddenField<String>();
        uuid.setId("idUUID");
        buildDeletedCkhGrp();
    }

    private void buildDeletedCkhGrp() {
        chkGroup = new CheckBoxGroup();
        chkGroup.add(chkDeleted);
        chkGroup.setFieldLabel(I18N.DISPLAY.refDeletePrompt());
    }

    private void addFields() {
        panel.add(refGenName, formData);
        panel.add(path, formData);
        panel.add(userName, formData);
        panel.add(creationDate, formData);
        panel.add(lastModDate, formData);
        panel.add(lastModUserName, formData);
        panel.add(chkGroup, formData);
        panel.add(id, formData);
        panel.add(uuid, formData);
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
        ReferenceGenomesServiceFacade facade = new ReferenceGenomesServiceFacade();
        if (mode.equals(MODE.ADD)) {
            facade.createReferenceGenomes(toJson(), callback);
        } else {
            facade.editReferenceGenomes(toJson(), callback);
        }
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
        lastModDate.setValue((Date)genome.get(ReferenceGenome.LAST_MODIFIED_ON));
        lastModUserName.setValue((String)genome.get(ReferenceGenome.LAST_MODIFIED_BY));
        creationDate.setValue((Date)genome.get(ReferenceGenome.CREATED_ON));
        userName.setValue((String)genome.get(ReferenceGenome.CREATED_BY));
        id.setValue(genome.get(ReferenceGenome.ID).toString());
        uuid.setValue(genome.get(ReferenceGenome.UUID).toString());
        chkDeleted.setValue(Boolean.parseBoolean(genome.get(ReferenceGenome.DELETED).toString()));
    }
}
