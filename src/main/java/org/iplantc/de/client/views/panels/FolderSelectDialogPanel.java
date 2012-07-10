package org.iplantc.de.client.views.panels;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.ui.HTML;

/**
 * Inner panel for the file selector dialog.
 * 
 * @author psarando
 * 
 */
public class FolderSelectDialogPanel extends ResourceSelectDialogPanel {
    /**
     * Construct an instance a folder selection panel.
     * 
     * @param folder
     * @param currentFolderId
     */
    public FolderSelectDialogPanel(Folder folder, String currentFolderId, String tag) {
        super(folder, currentFolderId, tag);
        navigationSelectionChangeListener = new SelectionChangeListenerImpl();
    }

    /**
     * Initialize all components used by this widget.
     */
    @Override
    protected void initComponents() {
        initComponents(I18N.DISPLAY.selectedFolder());
    }

    @Override
    public void select(DiskResource resource) {
        if (resource instanceof Folder) {
            select((Folder)resource);
        }

    }

    public void select(Folder folder) {
        if (pnlNavigation != null) {
            selectFolder(folder.getId());
        }
    }

    /**
     * Retrieve the folder which the user selected.
     * 
     * @return folder the user has selected.
     */
    public Folder getSelectedFolder() {
        return (Folder)selectedResource;
    }

    private class SelectionChangeListenerImpl implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            setParentOkButton();
            DiskResource resource = pnlNavigation.getSelectedItem();
            if (resource instanceof Folder && resource != null) {
                txtResourceName.setValue(resource.getId());
                enableParentOkButton();
            } else {
                // disable OK button if a folder is not selected
                txtResourceName.setValue(""); //$NON-NLS-1$
                disableParentOkButton();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        DiskResource resource = pnlNavigation.getSelectedItem();
        if (resource instanceof Folder && resource != null) {
            txtResourceName.setValue(resource.getId());
            selectedResource = resource;
            setCurrentFolderId(resource.getId());
        }
    }

    /**
     * Tells the panel which DiskResource should be shown as "selected."
     * 
     * @param idDiskresource id of the DiskResource in the data browser grid to select
     */
    public Folder selectById(String idDiskresource) {
        if (pnlNavigation != null) {
            if (pnlNavigation.selectFolder(idDiskresource)) {
                return pnlNavigation.getSelectedItem();
            }

        }
        return null;
    }

    /**
     * Initialize all components used by this widget.
     */
    @Override
    protected void initComponents(String lblStringSelectedResource) {
        // initialize the navigation panel
        pnlNavigation = new DataNavigationPanel(tag, DataNavigationPanel.Mode.VIEW);
        pnlNavigation.disableDragAndDrop();
        pnlNavigation.setHeight(275);

        // initialize the selected file field
        txtResourceName.setReadOnly(true);
        txtResourceName.setWidth(300); // I hate hard-coding this

        Label lblSelectedFile = new Label(lblStringSelectedResource);
        lblSelectedFile.setWidth(85);

        HorizontalPanel pnlSelectedInfo = new HorizontalPanel();
        pnlSelectedInfo.setSpacing(2);
        pnlSelectedInfo.add(lblSelectedFile);
        pnlSelectedInfo.add(txtResourceName);
        // initialize and add these fields to the border layout container
        container = new LayoutContainer(new FlowLayout());
        container.setSize(400, 300);
        container.add(pnlNavigation);
        container.add(new HTML("<br/>"));
        container.add(pnlSelectedInfo);

        pnlNavigation.setMaskingParent(container);
    }
}
