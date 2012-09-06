package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.client.widgets.BoundedTextArea;
import org.iplantc.core.client.widgets.BoundedTextField;
import org.iplantc.core.client.widgets.utils.FormLabel;
import org.iplantc.core.client.widgets.validator.BasicEmailValidator;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uiapplications.client.store.AnalysisToolGroupStoreWrapper;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.dialogs.CategorySelectionDialog;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SubmitAppForPublicUsePanel extends LayoutContainer {
    private static final String INTEGRATOR_NAME = "integName"; //$NON-NLS-1$
    private static final String EMAIL = "email"; //$NON-NLS-1$
    private static final String DESC = "desc"; //$NON-NLS-1$
    private static final String ID = "id_"; //$NON-NLS-1$

    private TextField<String> integratorNameField;
    private TextField<String> emailField;
    private TextArea selectedCategories;
    private TextArea descField;
    private VerticalPanel categoryField;
    private String wikiUrl;
    private Button btnSubmit;
    private Button btnCancel;
    private FormPanel form;
    private final Analysis analysis;
    private final AsyncCallback<String> closeCallback;

    private CategorySelectionDialog dialog;
    private List<AnalysisGroupTreeModel> selectedItems;

    private ReferenceEditorGridPanel refPanel;

    private static AppTemplateUserServiceFacade templateService = GWT
            .create(AppTemplateUserServiceFacade.class);

    /**
     * Creates a new instance of PublishToWorldPanel
     * 
     * @param analysis the analysis to make public
     * @param closeCallback a command to execute when one of the buttons is clicked. onSuccess is called
     *            when the publish form is successfully submitted, and onFailure is called when the
     *            publish fails, or the user cancels the form.
     */
    public SubmitAppForPublicUsePanel(Analysis analysis, AsyncCallback<String> closeCallback) {
        this.analysis = analysis;
        this.closeCallback = closeCallback;
        setLayout(new FormLayout());
        initForm();
    }

    private void initForm() {
        form = new FormPanel() {
            @Override
            public boolean isValid(boolean preventMark) {
                return super.isValid(preventMark) && refPanel.isValid();
            }
        };

        form.setLayout(new FormLayout(LabelAlign.TOP));
        form.setSize(595, 440);
        form.setHeaderVisible(false);
        form.setBodyBorder(false);
        form.setButtonAlign(HorizontalAlignment.CENTER);
        form.setScrollMode(Scroll.AUTOY);

        buildFields();
        addFields();
        buildButtons();

        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.add(form);
        add(panel);
    }

    private JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("analysis_id", getJsonString(analysis.getId())); //$NON-NLS-1$
        json.put("integrator", getJsonString(integratorNameField.getValue())); //$NON-NLS-1$
        json.put("email", getJsonString(emailField.getValue())); //$NON-NLS-1$
        json.put("desc", getJsonString(descField.getValue())); //$NON-NLS-1$
        json.put("groups", buildSelectedCategoriesAsJson()); //$NON-NLS-1$
        json.put("wiki_url", getJsonString(wikiUrl)); //$NON-NLS-1$
        json.put("references", refPanel.toJson()); //$NON-NLS-1$

        return json;
    }

    private JSONString getJsonString(String value) {
        return new JSONString(value == null ? "" : value); //$NON-NLS-1$
    }

    private JSONArray buildSelectedCategoriesAsJson() {
        JSONArray arr = new JSONArray();
        int index = 0;
        for (AnalysisGroupTreeModel model : selectedItems) {
            arr.set(index++, new JSONString(model.getId()));
        }
        return arr;
    }

    private void addFields() {
        FormData formData = new FormData("93%"); //$NON-NLS-1$

        LayoutContainer top = buildColumnLayoutContainer();
        LayoutContainer left = buildLeftLayoutContainer(buildFormLayout());
        LayoutContainer right = buildRightLayoutContainer(buildFormLayout());

        left.add(integratorNameField, formData);
        left.add(emailField, formData);

        right.add(new Label(buildRequiredFieldLabel(I18N.DISPLAY.categorySelect())), formData);
        right.add(categoryField, formData);

        top.add(left, new ColumnData(.5));
        top.add(right, new ColumnData(.5));
        form.add(top);

        form.add(createSpacer());

        form.add(descField, formData);

        form.add(new FormLabel(I18N.DISPLAY.referencesLabel()), formData);
        form.add(refPanel, formData);
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

    private LayoutContainer buildRightLayoutContainer(FormLayout right1Layout) {
        LayoutContainer right1 = new LayoutContainer(right1Layout);
        right1.setStyleAttribute("paddingLeft", "10px"); //$NON-NLS-1$ //$NON-NLS-2$
        return right1;
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
        integratorNameField = buildTextField(I18N.DISPLAY.integratorName(), false, null,
                INTEGRATOR_NAME, null, 255);

        emailField = buildTextField(I18N.DISPLAY.integratorEmail(), false, getEmail(), EMAIL,
                new BasicEmailValidator(), 256);

        descField = buildTextArea(I18N.DISPLAY.analysisDesc(), true, analysis.getDescription(), DESC,
                255);

        categoryField = buildCategoryField();

        setDefaultValues();

        integratorNameField.setEnabled(false);
        emailField.setEnabled(false);

        refPanel = new ReferenceEditorGridPanel(525, 90);
    }

    private void setDefaultValues() {
        UserInfo info = UserInfo.getInstance();
        if (info.getFirstName() == null || info.getLastName() == null) {
            integratorNameField.setValue(info.getUsername());
        } else {
            integratorNameField.setValue(info.getFirstName() + " " + info.getLastName());
        }
        emailField.setValue(info.getEmail());
    }

    private TextField<String> buildTextField(String label, boolean allowBlank, String defaultVal,
            String name, Validator validator, int maxLength) {
        BoundedTextField<String> field = new BoundedTextField<String>();
        field.setMaxLength(maxLength);
        field.setName(name);
        field.setId(ID + name);
        field.setFieldLabel(allowBlank ? label : buildRequiredFieldLabel(label));
        field.setAllowBlank(allowBlank);
        field.setAutoValidate(true);
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
        field.setAutoValidate(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        if (defaultVal != null) {
            field.setValue(defaultVal);
        }
        return field;
    }

    private VerticalPanel buildCategoryField() {
        selectedCategories = new TextArea();
        selectedCategories.setSize(240, 60);
        selectedCategories.setReadOnly(true);
        selectedCategories.setAllowBlank(false);
        Button select = new Button(I18N.DISPLAY.browse(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                loadCategories();
            }
        });

        select.setStyleAttribute("float", "right"); //$NON-NLS-1$ //$NON-NLS-2$
        select.setHeight(20);

        VerticalPanel catSelectionPanel = new VerticalPanel();
        catSelectionPanel.setBorders(true);
        catSelectionPanel.setSpacing(2);
        catSelectionPanel.add(selectedCategories);
        catSelectionPanel.add(select);
        return catSelectionPanel;
    }

    private String buildRequiredFieldLabel(String label) {
        if (label == null) {
            return null;
        }

        return "<span class='required_marker'>*</span> " + label; //$NON-NLS-1$
    }

    protected void showCategorySelectionDialog(TreeStore<AnalysisGroupTreeModel> store) {
        dialog = new CategorySelectionDialog(store);
        dialog.setSelectedItems(selectedItems);
        dialog.getButtonById(Dialog.OK).addSelectionListener(new OkBtnSelectionListener());
        dialog.show();
    }

    private class OkBtnSelectionListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            StringBuffer sb = new StringBuffer();
            boolean first = true;

            selectedItems = dialog.getSelectedGroups();
            for (AnalysisGroupTreeModel model : selectedItems) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", "); //$NON-NLS-1$
                }

                sb.append(model.getName());
            }

            selectedCategories.setValue(sb.toString());
        }
    }

    private void loadCategories() {
        templateService.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AnalysisToolGroupStoreWrapper wrapper = new AnalysisToolGroupStoreWrapper();

                        // Update the store wrapper with only public categories.
                        wrapper.updateWrapper(result, true);

                        // Also remove the public "Beta" category
                        TreeStore<AnalysisGroupTreeModel> store = wrapper.getStore();
                        store.remove(store.findModel(AnalysisGroupTreeModel.ID, DEProperties
                                .getInstance().getDefaultBetaCategoryId()));

                        showCategorySelectionDialog(store);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                    }
                });
    }

    private void buildSubmitButton() {
        btnSubmit = new Button();

        btnSubmit.setText(I18N.DISPLAY.submit());
        btnSubmit.setId("idBtnSubmit"); //$NON-NLS-1$

        btnSubmit.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                createDocumentationPage();
            }
        });
    }

    /**
     * Adds a page to the wiki.
     */
    private void createDocumentationPage() {
        ConfluenceServiceFacade.getInstance().createDocumentationPage(analysis.getName(),
                analysis.getDescription(), new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.cantCreateConfluencePage(analysis.getName()),
                                caught);
                    }

                    @Override
                    public void onSuccess(String url) {
                        wikiUrl = url;
                        templateService.publishToWorld(toJson(), new AsyncCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                closeCallback.onSuccess(wikiUrl);
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                closeCallback.onFailure(caught);
                            }
                        });
                    }
                });
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

    private String getEmail() {
        UserInfo info = UserInfo.getInstance();

        return info.getEmail();
    }
}
