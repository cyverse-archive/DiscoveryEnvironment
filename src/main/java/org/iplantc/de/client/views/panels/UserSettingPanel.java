/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.TableData;

/**
 * @author sriram
 * 
 */
public class UserSettingPanel extends LayoutContainer {

    private static final String ID_REM_LAST_PATH = "idRemLastPath";
    private static final String ID_CHK_NOTIFY = "idChkNotify";
    private CheckBox chkEnableEmailNotifications;
    private CheckBox chkRememberLastFileSelectorPath;

    public UserSettingPanel() {
        add(buildNotifyField());
        add(buildRememberField());
        setValues();
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

    public void saveData() {
        UserSettings us = UserSettings.getInstance();
        us.setEnableEmailNotification(chkEnableEmailNotifications.getValue());
        us.setRememberLastPath(chkRememberLastFileSelectorPath.getValue());
    }

}
