package org.iplantc.de.client.views.dialogs;

import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel;
import org.iplantc.de.client.views.panels.UserSettingPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

/**
 * A dialog to collect user general settings for the DE
 * 
 * 
 * @author sriram
 * 
 */
public class UserPreferencesDialog extends Dialog {

    private TabPanel tabPanel;

    public UserPreferencesDialog() {
        init();
    }

    private void init() {
        initDialog();
        buildTabPanel();
        buildSettingPanel();
        buildCollaboratorsPanel();

        addOkButtonListener();
    }

    private void initDialog() {
        setHeading(I18N.DISPLAY.preferences());
        setButtons(Dialog.OKCANCEL);
        setResizable(false);
        setHideOnButtonClick(true);
    }

    private void addOkButtonListener() {
        getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                UserSettings us = UserSettings.getInstance();
                // us.setEnableEmailNotification(chkEnableEmailNotifications.getValue());
                // us.setRememberLastPath(chkRememberLastFileSelectorPath.getValue());
            }
        });
    }

    private void buildTabPanel() {
        tabPanel = new TabPanel();
        tabPanel.setSize(450, 350);
        add(tabPanel);
    }

    private void buildSettingPanel() {
        TabItem ti = new TabItem(I18N.DISPLAY.settings());
        ti.add(new UserSettingPanel());
        tabPanel.add(ti);
    }

    private void buildCollaboratorsPanel() {
        TabItem ti = new TabItem(I18N.DISPLAY.collaborators());
        ti.add(new ManageCollaboratorsPanel());
        tabPanel.add(ti);
    }

}
