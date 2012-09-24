package org.iplantc.admin.belphegor.client.apps.views.editors;

import org.iplantc.admin.belphegor.client.Constants;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppValidators;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AppEditor implements Editor<App>, IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void onAppEditorSave(App app);

    }

    private static BINDER uiBinder = GWT.create(BINDER.class);

    interface BINDER extends UiBinder<Widget, AppEditor> {
    }

    interface Driver extends SimpleBeanEditorDriver<App, AppEditor> {
    }

    Driver driver = GWT.create(Driver.class);

    private final Presenter presenter;

    @UiField
    @Ignore
    Dialog dialog;

    @UiField
    TextField name;

    @UiField
    FieldLabel appNameFieldLabel;

    @UiField
    TextField integratorName;

    @UiField
    FieldLabel integratorNameFieldLabel;

    @UiField
    TextField integratorEmail;

    @UiField
    FieldLabel integratorEmailFieldLabel;

    @UiField
    CheckBox disabled;

    @UiField
    FieldLabel appDisabledCheckBoxLabel;

    @UiField
    TextArea description;

    @UiField
    FieldLabel appDescFieldLabel;

    @UiField
    TextField wikiUrl;

    @UiField
    FieldLabel wikiUrlFieldLabel;

    private final Widget widget;

    public AppEditor(App app, Presenter presenter) {
        widget = uiBinder.createAndBindUi(this);
        this.presenter = presenter;

        // Add validators
        name.addValidator(AppValidators.APP_NAME_VALIDATOR);
        integratorEmail.addValidator(AppValidators.APP_WIKI_URL_VALIDATOR);

        wikiUrlFieldLabel.setHTML(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY
                .wikiUrlLabel(Constants.CLIENT.publishDocumentationUrl())));

        // dialog.setHeadingText(app.getName());
        dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        // Override "Ok" button and set text to "Save"
        dialog.getButtonById(PredefinedButton.OK.name()).setText(I18N.DISPLAY.save());

        driver.initialize(this);
        driver.edit(app);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }


    public void show() {
        dialog.show();
    }

    @UiHandler("dialog")
    void onHide(BeforeHideEvent event) {
        Dialog btn = (Dialog)event.getSource();
        String text = btn.getHideButton().getItemId();
        if (text.equals(PredefinedButton.OK.name())) {
            event.setCancelled(true);
            presenter.onAppEditorSave(driver.flush());
        } else if (text.equals(PredefinedButton.CANCEL.name())) {

        }
    }

}
