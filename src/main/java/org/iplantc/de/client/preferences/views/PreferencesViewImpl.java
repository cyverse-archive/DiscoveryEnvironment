package org.iplantc.de.client.preferences.views;

import org.iplantc.core.uicommons.client.models.UserSettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Radio;

/**
 * A view imple for preferences screen
 * 
 * @author sriram
 * 
 */
public class PreferencesViewImpl implements PreferencesView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("PreferencesView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, PreferencesViewImpl> {
    }

    private final Widget widget;

    private Presenter presenter;

    @UiField
    VerticalLayoutContainer container;

    @UiField
    CheckBox cboNotifyEmail;

    @UiField
    CheckBox cboLastPath;

    @UiField
    Radio radioSaveSession;

    @UiField
    Radio radioNoSaveSession;

    public PreferencesViewImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        presenter = p;
    }

    @Override
    public void setDefaultValues() {
        cboNotifyEmail.setValue(true);
        cboLastPath.setValue(true);
        radioSaveSession.setValue(true);
    }

    @Override
    public void setValues() {
        UserSettings us = UserSettings.getInstance();
        cboNotifyEmail.setValue(us.isEnableEmailNotification());
        cboLastPath.setValue(us.isRememberLastPath());
        if (us.isSaveSession()) {
            radioSaveSession.setValue(true);
            radioNoSaveSession.setValue(false);
        } else {
            radioSaveSession.setValue(false);
            radioNoSaveSession.setValue(true);
        }

    }

    @Override
    public UserSettings getValues() {
        UserSettings us = UserSettings.getInstance();
        us.setEnableEmailNotification(cboNotifyEmail.getValue());
        us.setRememberLastPath(cboLastPath.getValue());
        if (radioNoSaveSession.getValue()) {
            us.setSaveSession(false);
        } else {
            us.setSaveSession(true);
        }
        return us;
    }

}
