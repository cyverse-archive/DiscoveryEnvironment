/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceFacade;
import org.iplantc.de.client.utils.DEInfo;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.views.FolderSelector;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author sriram
 * 
 */
public class UserSettingPanel extends LayoutContainer {

    private static final String ID_REM_LAST_PATH = "idRemLastPath";
    private static final String ID_CHK_NOTIFY = "idChkNotify";
    private static final String ID_DEFAULT_OUTPUT_FOLDER = "idDefaultOutputFolder";
    private CheckBox chkEnableEmailNotifications;
    private CheckBox chkRememberLastFileSelectorPath;
    private RadioGroup radioGrp;
    private Radio enable;
    private Radio disable;
    private FolderSelector defaultOutputFolder;

    public UserSettingPanel() {
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
        ret.add(new Html("<div>" + I18N.DISPLAY.notifyemail() + "</div>"), td);

        IconButton ib = new IconButton("help");
        ToolTipConfig ttc = getToolTipConfig();
        ttc.setTitle(I18N.DISPLAY.help());
        ttc.setText(I18N.DISPLAY.notifyemailHelp());
        ib.setToolTip(ttc);
        ret.add(ib, td);

        return ret;
    }

    private HorizontalPanel buildRememberField() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        initRememberField();

        ret.add(chkRememberLastFileSelectorPath, td);
        ret.add(new Html("<div>" + I18N.DISPLAY.rememberFileSectorPath() + "</div>"), td);

        IconButton ib = new IconButton("help");
        ToolTipConfig ttc = getToolTipConfig();
        ttc.setTitle(I18N.DISPLAY.help());
        ttc.setText((I18N.DISPLAY.rememberFileSectorPathHelp()));
        ib.setToolTip(ttc);
        ret.add(ib, td);

        return ret;
    }

    private HorizontalPanel buildClearSessionPanel() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        initSaveOption();

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        ret.add(new Html("<div>" + I18N.DISPLAY.saveSession() + ": </div>"));
        ret.add(radioGrp, td);

        IconButton ib = new IconButton("help");
        ToolTipConfig ttc = getToolTipConfig();
        ttc.setTitle(I18N.DISPLAY.help());
        ttc.setText(I18N.DISPLAY.saveSessionHelp());
        ib.setToolTip(ttc);
        ret.add(ib, td);

        return ret;
    }

    private VerticalPanel buildDefaultOutputFolderPanel() {
        VerticalPanel ret = new VerticalPanel();
        ret.setSpacing(5);

        initDefaultOutputFolder();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        hp.add(new Html("<div>" + I18N.DISPLAY.defaultOutputFolder() + ": </div>"), td);
        IconButton ib = new IconButton("help");
        ToolTipConfig ttc = getToolTipConfig();
        ttc.setTitle(I18N.DISPLAY.help());
        ttc.setText(I18N.DISPLAY.defaultOutputFolderHelp());
        ib.setToolTip(ttc);
        hp.add(ib, td);

        ret.add(hp);
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
        defaultOutputFolder.setDefaultFolderId(us.getDefaultOutputFolder());
        defaultOutputFolder.displayFolderName(us.getDefaultOutputFolder());
    }

    public void setDefaultValues() {
        chkEnableEmailNotifications.setValue(true);
        chkRememberLastFileSelectorPath.setValue(true);
        radioGrp.setValue((Radio)radioGrp.get(0));
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        facade.putDefaultOutput(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.defaultPrefError(), caught);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JsonUtil.getObject(result);
                String opfolder = JsonUtil.getString(obj, "path");
                defaultOutputFolder.clearSelection();
                defaultOutputFolder.displayFolderName(opfolder);
                defaultOutputFolder.setSelectedFolder(new Folder(opfolder, DiskResourceUtil
                        .parseNameFromPath(opfolder), false, new Permissions(true, true, true)));
            }
        });
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
        Services.USER_SESSION_SERVICE.saveUserPreferences(us.toJson(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                DEInfo.display(I18N.DISPLAY.save(), I18N.DISPLAY.saveSettings());
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
                UserSettings us = UserSettings.getInstance();
                defaultOutputFolder.clearSelection();
                defaultOutputFolder.displayFolderName(us.getDefaultOutputFolder());
                defaultOutputFolder.setDefaultFolderId(us.getDefaultOutputFolder());
            }
        }
    }

    private ToolTipConfig getToolTipConfig() {
        ToolTipConfig config = new ToolTipConfig();
        config.setMouseOffset(new int[] {0, 0});
        config.setAnchor("left");
        return config;
    }

}
