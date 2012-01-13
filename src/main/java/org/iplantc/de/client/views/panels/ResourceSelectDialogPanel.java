package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.models.ClientDataModel;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;

/**
 * Inner panel for a resource selector dialog.
 * 
 * @author lenards
 * 
 */
public class ResourceSelectDialogPanel extends IPlantDialogPanel {
    private Button btnParentOk;
    private LayoutContainer container;
    protected DataNavigationPanel pnlNavigation;
    protected DataMainPanel pnlMain;
    protected final TextField<String> txtResourceName;
    protected DiskResource selectedResource;
    private String currentFolderId;
    protected ClientDataModel model;
    protected String tag;
    protected Listener<BaseEvent> navigationSelectionChangeListener = null;
    protected Listener<BaseEvent> mainSelectionChangeListener = null;

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
        if (!refresh) {
            pnlNavigation.seed(username, model, navigationSelectionChangeListener);
        }
        pnlMain.seed(model, tag, mainSelectionChangeListener);
        pnlMain.setSelectionMode(SelectionMode.SINGLE);
        pnlMain.setMaskingParent(maskingParent);

        if (selectedResource != null) {
            txtResourceName.setValue(selectedResource.getName());
            select(selectedResource);
        } else {
            selectFolder(folderId);
        }

    }

    private void selectFolder(String folderId) {
        if (pnlNavigation != null) {
            if (folderId != null) {
                currentFolderId = folderId;
                pnlNavigation.selectFolder(folderId);
            } else {
                pnlNavigation.selectFolder(model.getRootFolderId());
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
        pnlNavigation = new DataNavigationPanel(tag,DataNavigationPanel.Mode.VIEW);
        pnlNavigation.disableDragAndDrop();

        // initialize the main view panel
        pnlMain = new DataMainPanel(tag, model,selectedResource);
        pnlMain.disableDragAndDrop();

        // initialize the selected file field
        txtResourceName.setReadOnly(true);
        txtResourceName.setWidth(435); // I hate hard-coding this

        Label lblSelectedFile = new Label(lblStringSelectedResource);
        lblSelectedFile.setWidth(85);

        HorizontalPanel pnlSelectedInfo = new HorizontalPanel();
        pnlSelectedInfo.setSpacing(2);
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
        pnlNavigation.selectFolder(DataUtils.parseParent(resource.getId()));
        pnlMain.select(resource);
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
        return pnlMain;
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
        pnlNavigation.cleanup();
        pnlMain.cleanup();
    }

    /**
     * @param currentFolderId the currentFolderId to set
     */
    public void setCurrentFolderId(String currentFolderId) {
        this.currentFolderId = currentFolderId;
    }

    /**
     * @return the currentFolderId
     */
    public String getCurrentFolderId() {
        return currentFolderId;
    }

}
