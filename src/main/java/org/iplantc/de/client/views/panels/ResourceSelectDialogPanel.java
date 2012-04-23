package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.controllers.DataController;
import org.iplantc.de.client.controllers.DataMonitor;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEvent;
import org.iplantc.de.client.models.ClientDataModel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Inner panel for a resource selector dialog.
 * 
 * @author lenards
 * 
 */
public class ResourceSelectDialogPanel extends IPlantDialogPanel implements DataMonitor {
    private Button btnParentOk;
    protected LayoutContainer container;
    protected DataNavigationPanel pnlNavigation;
    protected DataMainPanel pnlMain;
    protected final TextField<String> txtResourceName;
    protected DiskResource selectedResource;
    private String currentFolderId;
    protected ClientDataModel model;
    protected String tag;
    protected Listener<BaseEvent> navigationSelectionChangeListener = null;
    protected Listener<BaseEvent> mainSelectionChangeListener = null;
    private ArrayList<HandlerRegistration> handlers;

    /**
     * Construct an instance a file selection panel.
     * 
     * @param resource
     * @param currentFolderId
     */
    public ResourceSelectDialogPanel(DiskResource resource, String currentFolderId, String tag) {
        txtResourceName = new TextField<String>();
        selectedResource = resource;
        this.currentFolderId = currentFolderId;
        model = new ClientDataModel();
        this.tag = tag;
        initComponents();
        initHandlers();
    }

    /**
     * Sets the parent "OK" button.
     * 
     * This is the "OK" button contained in the parent of this panel.
     */
    public void setParentOkButton() {
        if (btnParentOk == null && parentButtons != null) {
            btnParentOk = (Button)parentButtons.getItemByItemId(Dialog.OK);
        }
    }

    /**
     * Enable the "OK" button of the parent.
     */
    protected void enableParentOkButton() {
        if (btnParentOk != null) {
            btnParentOk.enable();
        }
    }

    /**
     * Disable the "OK" button of the parent.
     */
    protected void disableParentOkButton() {
        if (btnParentOk != null) {
            btnParentOk.disable();
        }
    }

    public void seed(final String username, final String json, final Component maskingParent,
            boolean refresh, String folderId) {
        model.seed(json);
        if (!refresh && pnlNavigation != null) {
            pnlNavigation.seed(username, model, navigationSelectionChangeListener);
        }
        if (pnlMain != null) {
            pnlMain.seed(model, tag, mainSelectionChangeListener);
            pnlMain.setSelectionMode(SelectionMode.SINGLE);
            pnlMain.setMaskingParent(maskingParent);
        }

        if (selectedResource != null) {
            txtResourceName.setValue(selectedResource.getName());
            select(selectedResource);
        } else {
            if (folderId != null) {
                selectFolder(folderId);
            } else {
                // if not refresh and currently nothing was selected and remember path is enabled, the go
                // back to last back
                UserSettings instance = UserSettings.getInstance();
                String id = instance.getDefaultFileSelectorPath();
                boolean remember = instance.isRememberLastPath();
                if (remember && id != null && !id.isEmpty()) {
                    selectFolder(id);
                }
            }
        }

    }

    private void initHandlers() {
        EventBus eventbus = EventBus.getInstance();
        handlers = new ArrayList<HandlerRegistration>();
        handlers.add(eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandlerImpl()));
    }

    public void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    private void selectFolder(String folderId) {
        if (pnlNavigation != null) {
            if (folderId != null) {
                currentFolderId = folderId;
                pnlNavigation.selectFolder(folderId);
            }
        }
    }

    /**
     * Initialize all components used by this widget.
     */
    protected void initComponents() {
        initComponents(I18N.DISPLAY.selectedResource() + "  "); //$NON-NLS-1$
    }

    /**
     * Initialize all components used by this widget.
     */
    protected void initComponents(String lblStringSelectedResource) {
        // initialize the navigation panel
        pnlNavigation = new DataNavigationPanel(tag, DataNavigationPanel.Mode.VIEW);
        pnlNavigation.disableDragAndDrop();

        // initialize the main view panel
        if (selectedResource != null) {
            pnlMain = new DataMainPanel(tag, model, Arrays.asList(selectedResource.getId()));
        } else {
            pnlMain = new DataMainPanel(tag, model, null);
        }
        pnlMain.disableDragAndDrop();

        // initialize the selected file field
        txtResourceName.setReadOnly(true);
        txtResourceName.setWidth(435); // I hate hard-coding this

        Label lblSelectedFile = new Label(lblStringSelectedResource);
        lblSelectedFile.setWidth(85);

        HorizontalPanel pnlSelectedInfo = new HorizontalPanel();
        pnlSelectedInfo.setSpacing(2);
        pnlSelectedInfo.add(new Html("<br/>"));
        pnlSelectedInfo.add(lblSelectedFile);
        pnlSelectedInfo.add(txtResourceName);

        // initialize the border layout data
        BorderLayoutData west = new BorderLayoutData(LayoutRegion.WEST, 140);
        west.setCollapsible(true);
        west.setSplit(true);

        BorderLayoutData center = new BorderLayoutData(LayoutRegion.CENTER, 480);
        center.setCollapsible(true);
        center.setSplit(true);

        BorderLayoutData south = new BorderLayoutData(LayoutRegion.SOUTH, 40);

        // initialize and add these fields to the border layout container
        container = new LayoutContainer(new BorderLayout());
        container.add(pnlNavigation, west);
        container.add(pnlMain, center);
        container.add(pnlSelectedInfo, south);

        pnlNavigation.setMaskingParent(container);
    }

    /**
     * Tells the panel which DiskResource should be shown as "selected."
     * 
     * @param resource the DiskResource in the data browser grid to select
     */
    public void select(DiskResource resource) {
        if (pnlNavigation != null) {
            pnlNavigation.selectFolder(DiskResourceUtil.parseParent(resource.getId()));
        }
        if (pnlMain != null) {
            pnlMain.select(resource.getId(), false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getDisplayWidget() {
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getFocusWidget() {
        if (pnlMain != null) {
            return pnlMain;
        } else {
            return pnlNavigation;
        }
    }

    /**
     * Retrieve the file which the user selected.
     * 
     * @return file the user has selected.
     */
    public DiskResource getSelectedResource() {
        return selectedResource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        // intentionally do nothing.
    }

    /**
     * Release unneeded resources.
     */
    public void cleanup() {
        if (pnlNavigation != null) {
            pnlNavigation.cleanup();
        }
        if (pnlMain != null) {
            pnlMain.cleanup();
        }
    }

    /**
     * @param currentFolderId the currentFolderId to set
     */
    public void setCurrentFolderId(String currentFolderId) {
        this.currentFolderId = currentFolderId;
    }

    /**
     * Get the current path to which user has naivated in the nav panel
     * 
     * @return String id of the path to which user has navigated
     */
    public String getCurrentNavPath() {
        if (pnlNavigation != null && pnlNavigation.getSelectedItem() != null) {
            return pnlNavigation.getSelectedItem().getId();
        } else {
            return null;
        }
    }

    /**
     * @return the currentFolderId
     */
    public String getCurrentFolderId() {
        return currentFolderId;
    }

    @Override
    public void folderCreated(String idParentFolder, JSONObject jsonFolder) {
        Folder folder = model.createFolder(idParentFolder, jsonFolder);
        if (pnlMain != null) {
            pnlMain.addDiskResource(idParentFolder, folder);
        }

    }

    @Override
    public void addFile(String path, File info) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileSavedAs(String idOrig, String idParentFolder, File info) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileRename(String id, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void folderRename(String id, String name) {
        Folder folder = model.renameFolder(id, name);

        if (folder != null && pnlMain != null) {
            pnlMain.renameFolder(id, name);
        }

    }

    @Override
    public void deleteResources(List<String> folders, List<String> files) {
        deleteFolders(folders);
        deleteFiles(files);

    }

    private void deleteFolders(final List<String> folders) {
        if (folders != null) {
            if (pnlMain != null) {
                pnlMain.deleteDiskResources(folders);
            }

            for (String path : folders) {
                model.deleteDiskResource(path);

                if (model.isCurrentPage(path)) {
                    Folder folder = model.getFolder(DiskResourceUtil.parseParent(path));

                    if (folder != null) {
                        EventBus.getInstance().fireEvent(new DiskResourceSelectedEvent(folder, tag));
                    }
                }
            }
        }
    }

    private void deleteFiles(final List<String> files) {
        model.deleteDiskResources(files);
        if (pnlMain != null) {
            pnlMain.deleteDiskResources(files);
        }
    }

    @Override
    public void folderMove(Map<String, String> folders) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileMove(Map<String, String> files) {
        // TODO Auto-generated method stub

    }

    private class DataPayloadEventHandlerImpl implements DataPayloadEventHandler {
        @Override
        public void onFire(DataPayloadEvent event) {
            DataController controller = DataController.getInstance();
            controller.handleEvent(ResourceSelectDialogPanel.this, event.getPayload());
        }
    }

}
