package org.iplantc.de.client.views.dialogs;

import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.TableData;

/**
 * A dialog to collect user general settings for the DE
 * 
 * 
 * @author sriram
 * 
 */
public class UserSettingsDialog extends Dialog {

    private static final String ID_REM_LAST_PATH = "idRemLastPath";
    private static final String ID_CHK_NOTIFY = "idChkNotify";
    private CheckBox chkEnableEmailNotifications;
    private CheckBox chkRememberLastFileSelectorPath;

    public UserSettingsDialog() {
        init();
    }

    private void init() {
        setHeading(I18N.DISPLAY.preferences());
        setButtons(Dialog.OKCANCEL);
        setResizable(false);
        setHideOnButtonClick(true);
        add(buildNotifyField());
        add(buildRememberField());
        setValues();
        addOkButtonListener();
    }

    private void addOkButtonListener() {
        getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                UserSettings us = UserSettings.getInstance();
                us.setEnableEmailNotification(chkEnableEmailNotifications.getValue());
                us.setRememberLastPath(chkRememberLastFileSelectorPath.getValue());
            }
        });
    }

    private HorizontalPanel buildNotifyField() {

        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        initNotifyField();

        ret.add(chkEnableEmailNotifications, td);
        ret.add(new Html(I18N.DISPLAY.notifyemail()), td);

        return ret;
    }

    private HorizontalPanel buildRememberField() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(5);

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.LEFT);

        initRememberField();

        ret.add(chkRememberLastFileSelectorPath, td);
        ret.add(new Html(I18N.DISPLAY.rememberFileSectorPath()), td);

        return ret;
    }

    private void initNotifyField() {
        chkEnableEmailNotifications = new CheckBox();
        chkEnableEmailNotifications.setId(ID_CHK_NOTIFY);
    }

    private void initRememberField() {
        chkRememberLastFileSelectorPath = new CheckBox();
        chkRememberLastFileSelectorPath.setId(ID_REM_LAST_PATH);
    }

    private void setValues() {
        UserSettings us = UserSettings.getInstance();
        chkEnableEmailNotifications.setValue(us.isEnableEmailNotification());
        chkRememberLastFileSelectorPath.setValue(us.isRememberLastPath());
    }
}
