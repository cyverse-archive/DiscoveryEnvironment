package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.Constants;
import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.client.widgets.BoundedTextArea;
import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.validator.BasicEmailValidator;
import org.iplantc.core.uiapplications.client.models.Analysis;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditAppDetailsPanel extends LayoutContainer {
    private static final String INTEGRATOR_NAME = "integName"; //$NON-NLS-1$
    private static final String EMAIL = "email"; //$NON-NLS-1$
    private static final String WIKI_URL = "wiki_url"; //$NON-NLS-1$
    private static final String DESC = "desc"; //$NON-NLS-1$
    private static final String ID = "id_"; //$NON-NLS-1$

    private TextField<String> integratorNameField;
    private TextField<String> emailField;

    private TextField<String> appNameField;
    private TextArea descField;

    private TextField<String> urlField;
    private Button btnSubmit;
    private Button btnCancel;
    private FormPanel form;
    private Analysis analysis;
    private AsyncCallback<String> closeCallback;

    /**
     * Creates a new instance of EditAppDetailsPanel
     * 
     * @param analysis the analysis for editing
     * @param closeCallback a command to execute when one of the buttons is clicked. onSuccess is called
     *            when the publish form is successfully submitted, and onFailure is called when the
     *            publish fails, or the user cancels the form.
     */
    public EditAppDetailsPanel(Analysis analysis, AsyncCallback<String> closeCallback) {
        this.analysis = analysis;
        this.closeCallback = closeCallback;
        setLayout(new FormLayout());
        initForm();
    }

    private void initForm() {
        form = new FormPanel() {
            @Override
            public boolean isValid(boolean preventMark) {
                return super.isValid(preventMark);
            }
        };

        form.setLayout(new FormLayout(LabelAlign.TOP));
        form.setSize(595, 350);
        form.setHeaderVisible(false);
        form.setBodyBorder(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);
        form.setScrollMode(Scroll.AUTOY);

        buildFields();
        addFields();
        initFields();
        buildButtons();

        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.add(form);
        add(panel);
    }

    private void initFields() {
        appNameField.setValue(analysis.getName());
        descField.setValue(analysis.getDescription());
        integratorNameField.setValue(analysis.getIntegratorsName());
        if (analysis.get(Analysis.WIKI_URL) != null
                && !analysis.get(Analysis.WIKI_URL).toString().isEmpty()) {
            urlField.setValue(analysis.get(Analysis.WIKI_URL).toString());
        }

        emailField.setValue(analysis.getIntegratorsEmail());

    }

    private JSONObject toJson() {
        analysis.setName(getNonNullString(appNameField.getValue()));
        analysis.setIntegratorEmail(getNonNullString(emailField.getValue()));
        analysis.setIntegratorName(getNonNullString(integratorNameField.getValue()));
        analysis.setWikiUrl(getNonNullString(urlField.getValue()));
        analysis.setDescription(getNonNullString(descField.getValue()));

        return analysis.toJson();

    }

    private String getNonNullString(String value) {
        return (value == null ? "" : value); //$NON-NLS-1$
    }

    private void addFields() {
        FormData formData = new FormData("93%"); //$NON-NLS-1$

        LayoutContainer top = buildColumnLayoutContainer();
        LayoutContainer left = buildLeftLayoutContainer(buildFormLayout());

        left.add(appNameField, formData);
        left.add(integratorNameField, formData);
        left.add(emailField, formData);

        top.add(left, new ColumnData(.5));
        form.add(top);

        form.add(createSpacer());

        form.add(descField, formData);

        form.add(new Label(buildRequiredFieldLabel(I18N.DISPLAY.wikiUrlLabel(Constants.CLIENT
                .publishDocumentationUrl()) + ":"))); //$NON-NLS-1$
        form.add(buildUrlField(), formData);
    }

    private void buildButtons() {
        buildSubmitButton();
        buildCancelButton();
        form.addButton(btnSubmit);
        form.addButton(btnCancel);
        FormButtonBinding binding = new FormButtonBinding(form);
        binding.addButton(btnSubmit);
    }

    private LayoutContainer buildLeftLayoutContainer(FormLayout left2Layout) {
        LayoutContainer left2 = new LayoutContainer(left2Layout);
        left2.setStyleAttribute("paddingRight", "10px"); //$NON-NLS-1$ //$NON-NLS-2$
        return left2;
    }

    private FormLayout buildFormLayout() {
        FormLayout left1Layout = new FormLayout();
        left1Layout.setLabelAlign(LabelAlign.TOP);
        return left1Layout;
    }

    private LayoutContainer buildColumnLayoutContainer() {
        ColumnLayout colLayout1 = new ColumnLayout();
        colLayout1.setAdjustForScroll(true);
        LayoutContainer pnl1 = new LayoutContainer(colLayout1);
        return pnl1;
    }

    /**
     * Spacer for non-Field elements
     * 
     * @return
     */
    private Component createSpacer() {
        return new Html("<div style=\"margin-top:5px;\"/>"); //$NON-NLS-1$
    }

    private void buildFields() {
        appNameField = buildTextField(I18N.DISPLAY.name(), false, null, Analysis.NAME, null, 255);

        integratorNameField = buildTextField(I18N.DISPLAY.integratorName(), false, null,
                INTEGRATOR_NAME, null, 32);

        emailField = buildTextField(I18N.DISPLAY.integratorEmail(), false, null, EMAIL,
                new BasicEmailValidator(), 256);

        descField = buildTextArea(I18N.DISPLAY.analysisDesc(), true, analysis.getDescription(), DESC,
                255);

        urlField = buildTextField(null, false, null, WIKI_URL, null, 1024);

    }

    private TextField<String> buildTextField(String label, boolean allowBlank, String defaultVal,
            String name, Validator validator, int maxLength) {
        BoundedTextField<String> field = new BoundedTextField<String>();
        field.setMaxLength(maxLength);
        field.setName(name);
        field.setId(ID + name);
        field.setFieldLabel(allowBlank ? label : buildRequiredFieldLabel(label));
        field.setAllowBlank(allowBlank);
        field.setValidateOnBlur(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        if (defaultVal != null) {
            field.setValue(defaultVal);
        }
        if (validator != null) {
            field.setValidator(validator);
        }

        return field;
    }

    private TextArea buildTextArea(String label, boolean allowBlank, String defaultVal, String name,
            int maxLength) {
        TextArea field = new BoundedTextArea();
        field.setMaxLength(maxLength);
        field.setName(name);
        field.setId(ID + name);
        field.setFieldLabel(label);
        field.setAllowBlank(allowBlank);
        field.setValidateOnBlur(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        if (defaultVal != null) {
            field.setValue(defaultVal);
        }
        return field;
    }

    private String buildRequiredFieldLabel(String label) {
        if (label == null) {
            return null;
        }

        return "<span class='required_marker'>*</span> " + label; //$NON-NLS-1$
    }

    private VerticalPanel buildUrlField() {
        urlField = new BoundedTextField<String>();

        urlField.setMaxLength(1024);
        urlField.setName(WIKI_URL);
        urlField.setId(ID + WIKI_URL);
        urlField.setAllowBlank(false);
        urlField.setAutoValidate(true);
        urlField.setWidth(520);
        urlField.setEmptyText(Constants.CLIENT.validAppWikiUrlExample());

        urlField.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {
                // make sure the URL protocol is http or https, has a valid iPlant host name, and has at
                // least one character under the validAppWikiUrlPath.
                if (!value.matches("https?://[^/]*iplantc(ollaborative)?\\.org" //$NON-NLS-1$
                        + Constants.CLIENT.validAppWikiUrlPath() + ".+")) { //$NON-NLS-1$
                    return I18N.DISPLAY.notValidAppWikiUrl(); //$NON-NLS-1$
                }

                return null;
            }
        });

        VerticalPanel panel = new VerticalPanel();
        panel.setBorders(true);
        panel.setSpacing(5);
        panel.add(urlField);

        return panel;
    }

    private void buildSubmitButton() {
        btnSubmit = new Button();

        btnSubmit.setText(I18N.DISPLAY.submit());
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
        facade.updateApplication(toJson(), closeCallback);
    }

    private void buildCancelButton() {
        btnCancel = new Button();

        btnCancel.setText(I18N.DISPLAY.cancel());
        btnCancel.setId("idBtnCancel"); //$NON-NLS-1$

        btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                closeCallback.onFailure(null);
            }
        });
    }
}
