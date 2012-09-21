package org.iplantc.admin.belphegor.client.apps.views.widgets;

import org.iplantc.admin.belphegor.client.Constants;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppProperties;
import org.iplantc.core.uiapplications.client.models.autobeans.AppValidators;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class EditAppDetailsWidget implements IsWidget {

    private static EditAppDetailsWidgetUiBinder uiBinder = GWT
            .create(EditAppDetailsWidgetUiBinder.class);

    @UiField
    Dialog dialog;

    @UiField
    TextField appNameField;

    @UiField
    FieldLabel appNameFieldLabel;

    @UiField
    TextField integratorNameField;

    @UiField
    FieldLabel integratorNameFieldLabel;

    @UiField
    TextField integratorEmailField;

    @UiField
    FieldLabel integratorEmailFieldLabel;

    @UiField
    CheckBox appDisabledCheckBox;

    @UiField
    FieldLabel appDisabledCheckBoxLabel;

    @UiField
    TextArea appDescField;

    @UiField
    FieldLabel appDescFieldLabel;

    @UiField
    TextField wikiUrlField;

    @UiField
    FieldLabel wikiUrlFieldLabel;

    private final Widget widget;

    interface EditAppDetailsWidgetUiBinder extends UiBinder<Widget, EditAppDetailsWidget> {
    }

    public AppProperties props = GWT.create(AppProperties.class);

    private final Store<App>.Record record;

    public EditAppDetailsWidget(Store<App>.Record record) {
        this.record = record;
        widget = uiBinder.createAndBindUi(this);
        App app = this.record.getModel();
        appNameField.setValue(app.getName());
        integratorNameField.setValue(app.getIntegratorName());
        integratorEmailField.setValue(app.getIntegratorEmail());
        appDisabledCheckBox.setValue(app.isDisabled());
        appDescField.setValue(app.getDescription());
        wikiUrlField.setValue(app.getWikiUrl());

        appNameField.addValidator(AppValidators.APP_NAME_VALIDATOR);
        integratorEmailField.addValidator(AppValidators.APP_WIKI_URL_VALIDATOR);

        appNameFieldLabel.setLabelAlign(LabelAlign.TOP);
        integratorNameFieldLabel.setLabelAlign(LabelAlign.TOP);
        integratorEmailFieldLabel.setLabelAlign(LabelAlign.TOP);
        appDisabledCheckBoxLabel.setLabelAlign(LabelAlign.TOP);
        appDescFieldLabel.setLabelAlign(LabelAlign.TOP);
        wikiUrlFieldLabel.setLabelAlign(LabelAlign.TOP);


        wikiUrlFieldLabel.setHTML(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY
                .wikiUrlLabel(Constants.CLIENT.publishDocumentationUrl())));
        dialog.setHeadingText(app.getName());
        dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        // Override "Ok" button and set text to "Save"
        dialog.getButtonById(PredefinedButton.OK.name()).setText(I18N.DISPLAY.save());

    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @UiHandler("appNameField")
    public void onAppNameChange(ChangeEvent event) {
        record.addChange(props.name(), appNameField.getCurrentValue());
    }

    @UiHandler("integratorNameField")
    public void onIntegratorNameChange(ChangeEvent event) {
        record.addChange(props.integratorName(), integratorNameField.getCurrentValue());
    }

    @UiHandler("integratorEmailField")
    public void onIntegratorEmailChange(ChangeEvent event) {
        record.addChange(props.integratorEmail(), integratorEmailField.getCurrentValue());
    }

    @UiHandler("appDisabledCheckBox")
    public void onAppDisabledChange(ChangeEvent event) {
        record.addChange(props.disabled(), appDisabledCheckBox.getValue());
    }

    @UiHandler("appDescField")
    public void onAppDescChange(ChangeEvent event) {
        record.addChange(props.description(), appDescField.getCurrentValue());
    }

    @UiHandler("wikiUrlField")
    public void onWikiUrlFieldChange(ChangeEvent event) {
        record.addChange(props.wikiUrl(), wikiUrlField.getCurrentValue());
    }


    public String getAppName() {
        return appNameField.getCurrentValue();
    }

    public JSONObject appAsJson() {
        String jsonString = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(record.getModel()))
                .getPayload();

        return JsonUtil.getObject(jsonString);
    }

    public void setWikiUrl(String result) {
        // Setting the text on the field should activate the change event handler above, thus updating to
        // app variable.
        wikiUrlField.setText(result);
    }

    public void addHideHandler(HideHandler handler) {
        dialog.addHideHandler(handler);
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }

}
