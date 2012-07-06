/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.UserSessionServiceFacade;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.views.FolderSelector;
import org.iplantc.de.client.views.dialogs.UserPreferencesDialog;

/**
 * @author sriram
 * 
 */
public class UserSettingPanel extends LayoutContainer {

    private static final String ID_REM_LAST_PATH = "idRemLastPath";
    private static final String ID_CHK_NOTIFY = "idChkNotify";
    private static final String ID_DEFAULT_OUTPUT_FOLDER = "idDefaultOutputFolder";
    private UserPreferencesDialog userPreferencesDialog;
    private CheckBox chkEnableEmailNotifications;
    private CheckBox chkRememberLastFileSelectorPath;
    private RadioGroup radioGrp;
    private Radio enable;
    private Radio disable;
    private FolderSelector defaultOutputFolder;

    public UserSettingPanel(UserPreferencesDialog userPreferencesDialog) {
        this.userPreferencesDialog = userPreferencesDialog;
        add(buildNotifyField());
        add(buildRememberField());
        add(buildClearSessionPanel());
        add(buildDefaultOutputFolderPanel());
        setValues();
    }

    private HorizontalPanel buildNotifyField() {

        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        initNotifyField();

        ret.add(chkEnableEmailNotifications, td);
        ret.add(new Html("<div class='form_prompt'>" + I18N.DISPLAY.notifyemail() + "</div>"), td);

        return ret;
    }

    private HorizontalPanel buildRememberField() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        initRememberField();

        ret.add(chkRememberLastFileSelectorPath, td);
        ret.add(new Html("<div class='form_prompt'>" + I18N.DISPLAY.rememberFileSectorPath() + "</div>"),
                td);

        return ret;
    }

    private HorizontalPanel buildClearSessionPanel() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        initSaveOption();

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        add(new Html("<div class='form_prompt'>&nbsp;&nbsp;" + I18N.DISPLAY.saveSession() + ": </div>"));
        ret.add(radioGrp, td);

        return ret;
    }

    private VerticalPanel buildDefaultOutputFolderPanel() {
        VerticalPanel ret = new VerticalPanel();
        ret.setSpacing(5);

        initDefaultOutputFolder();

        add(new Html("<div class='form_prompt'>&nbsp;&nbsp;" + I18N.DISPLAY.defaultOutputFolder() + ": </div>"));
        ret.add(defaultOutputFolder.getWidget());

        return ret;
    }

    private void initSaveOption() {
        enable = new Radio();
        enable.setBoxLabel(I18N.DISPLAY.enabled());
        enable.setId("id" + I18N.DISPLAY.enabled());

        disable = new Radio();
        disable.setBoxLabel(I18N.DISPLAY.disabled());
        disable.setId("id" + I18N.DISPLAY.disabled());

        radioGrp = new RadioGroup();
        radioGrp.add(enable);
        radioGrp.add(disable);
    }

    private void initNotifyField() {
        chkEnableEmailNotifications = new CheckBox();
        chkEnableEmailNotifications.setId(ID_CHK_NOTIFY);
    }

    private void initRememberField() {
        chkRememberLastFileSelectorPath = new CheckBox();
        chkRememberLastFileSelectorPath.setId(ID_REM_LAST_PATH);
    }

    private void initDefaultOutputFolder() {
        defaultOutputFolder = new FolderSelector(new checkPermissions(), null);
        defaultOutputFolder.setId(ID_DEFAULT_OUTPUT_FOLDER);
    }

    private void setValues() {
        UserSettings us = UserSettings.getInstance();
        chkEnableEmailNotifications.setValue(us.isEnableEmailNotification());
        chkRememberLastFileSelectorPath.setValue(us.isRememberLastPath());
        if (us.isSaveSession()) {
            radioGrp.setValue(enable);
        } else {
            radioGrp.setValue(disable);
        }
        defaultOutputFolder.displayFolderName(us.getDefaultOutputFolder());
    }

    public void saveData() {
        UserSettings us = UserSettings.getInstance();
        us.setEnableEmailNotification(chkEnableEmailNotifications.getValue());
        us.setRememberLastPath(chkRememberLastFileSelectorPath.getValue());
        us.setSaveSession(radioGrp.getValue().getId().equals("id" + I18N.DISPLAY.enabled()) ? true
                : false);
        if (defaultOutputFolder.getSelectedFolderId() != null) {
            us.setDefaultOutputFolder(defaultOutputFolder.getSelectedFolderId());
        }
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.saveUserPreferences(us.toJson(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    private class checkPermissions implements Command {
        @Override
        public void execute() {
            if (!DataUtils.canUploadToThisFolder(defaultOutputFolder.getSelectedFolder())) {
                MessageBox.alert(I18N.DISPLAY.permissionErrorTitle(),
                        I18N.DISPLAY.permissionErrorMessage(), null);
                userPreferencesDialog.getButtonById(Dialog.OK).disable();
            } else {
                userPreferencesDialog.getButtonById(Dialog.OK).enable();
            }
        }
    }
}
