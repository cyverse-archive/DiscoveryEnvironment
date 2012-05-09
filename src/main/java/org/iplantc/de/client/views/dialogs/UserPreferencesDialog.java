package org.iplantc.de.client.views.dialogs;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel.MODE;
import org.iplantc.de.client.views.panels.UserSettingPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;

/**
 * A dialog to collect user general settings for the DE
 * 
 * 
 * @author sriram
 * 
 */
public class UserPreferencesDialog extends Dialog {

    private TabPanel tabPanel;
    private ManageCollaboratorsPanel collabPanel;
    private UserSettingPanel settingPanel;

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
        setButtons(Dialog.OK);
        setResizable(false);
        setHideOnButtonClick(true);
    }

    private void addOkButtonListener() {
        Button ok = getButtonById(Dialog.OK);
        ok.setText(I18N.DISPLAY.done());
        ok.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                settingPanel.saveData();
                collabPanel.saveData();
            }
        });
    }

    private void buildTabPanel() {
        tabPanel = new TabPanel();
        tabPanel.setSize(450, 380);
        add(tabPanel);
    }

    private void buildSettingPanel() {
        TabItem ti = new TabItem(I18N.DISPLAY.settings());
        settingPanel = new UserSettingPanel();
        ti.add(settingPanel);
        tabPanel.add(ti);
    }

    private void buildCollaboratorsPanel() {
        TabItem ti = new TabItem(I18N.DISPLAY.collaborators());
        collabPanel = new ManageCollaboratorsPanel(MODE.MANAGE, 435);
        ti.add(collabPanel);
        tabPanel.add(ti);
    }

}
