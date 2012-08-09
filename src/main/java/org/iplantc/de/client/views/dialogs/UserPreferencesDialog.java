package org.iplantc.de.client.views.dialogs;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.SettingsUpdatedEvent;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel.MODE;
import org.iplantc.de.client.views.panels.UserSettingPanel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;

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
        // @TODO temp remove collaborators panel
        // buildCollaboratorsPanel();
        layout();
        addListener(Events.Hide, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {

                settingPanel.saveData();
                // @TODO temp remove collaborators panel
                // collabPanel.saveData();
                EventBus.getInstance().fireEvent(new SettingsUpdatedEvent());
            }
        });
    }

    private void initDialog() {
        setHeading(I18N.DISPLAY.preferences());
        setButtons();
        setResizable(false);
        setHideOnButtonClick(true);

    }

    private void setButtons() {
        ButtonBar buttonBar = getButtonBar();
        // buttonBar.setAlignment(HorizontalAlignment.RIGHT);
        buttonBar.removeAll();
        setDefaultsButton();
        buttonBar.add(new SeparatorToolItem());
        setOkButton();

    }

    private void setOkButton() {
        Button ok = new Button(I18N.DISPLAY.done());
        ok.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();

            }
        });
        ok.setId(Dialog.OK);
        getButtonBar().add(ok);
    }

    private void setDefaultsButton() {
        Button def = new Button("Restore Defaults");
        def.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                settingPanel.setDefaultValues();
            }
        });
        def.setId("btn_default");
        getButtonBar().add(def);
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
