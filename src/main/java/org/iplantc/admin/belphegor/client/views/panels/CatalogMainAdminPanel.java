package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.views.panels.BaseCatalogMainPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A panel that displays apps in a grid and lets the user delete or modify them. TODO: disable delete
 * button when nothing selected
 */
public class CatalogMainAdminPanel extends BaseCatalogMainPanel {
    private AppTemplateAdminServiceFacade service;
    private Button deleteButton;

    /**
     * Creates a new CatalogMainAdminPanel.
     * 
     * @param templateService
     */
    public CatalogMainAdminPanel() {
        service = new AppTemplateAdminServiceFacade();
        initToolBar();
    }

    private void initToolBar() {
        deleteButton = buildDeleteButton();
        addToToolBar(deleteButton);
    }

    private Button buildDeleteButton() {
        deleteButton = new Button(I18N.DISPLAY.delete());

        deleteButton.disable();
        deleteButton.setId("idDelete"); //$NON-NLS-1$
        deleteButton.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));
        deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                deleteSelectedApp();
            }
        });

        addGridSelectionChangeListener(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                deleteButton.setEnabled(getSelectedApp() != null);
            }
        });

        return deleteButton;
    }

    private void deleteSelectedApp() {
        Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent ce) {
                Button btn = ce.getButtonClicked();

                // did the user click yes?
                if (btn.getItemId().equals(Dialog.YES)) {
                    confirmDeleteSelectedApp();
                }
            }
        };

        String appName = getSelectedApp().getName();
        MessageBox.confirm(I18N.DISPLAY.confirmDeleteAppTitle(), I18N.DISPLAY.confirmDeleteApp(appName),
                callback);
    }

    private void confirmDeleteSelectedApp() {
        String appId = getSelectedApp().getId();
        service.deleteApplication(appId, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantDeleteApp(), caught);
            }
        });
    }
}
