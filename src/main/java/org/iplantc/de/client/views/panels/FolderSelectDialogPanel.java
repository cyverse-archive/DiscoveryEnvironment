package org.iplantc.de.client.views.panels;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;

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
    protected void initComponents() {
        initComponents(I18N.DISPLAY.selectedFolder());
    }

    /**
     * Retrieve the folder which the user selected.
     * 
     * @return folder the user has selected.
     */
    public Folder getSelectedFolder() {
        return (Folder)selectedResource;
    }

    /**
     * Tells the panel which DiskResource should be shown as "selected."
     * 
     * @param resource the DiskResource in the data browser grid to select
     */
    @Override
    public void select(DiskResource resource) {
        pnlNavigation.selectFolder(resource.getId());
        pnlMain.select(resource);
    }

    private class SelectionChangeListenerImpl implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            setParentOkButton();

            DiskResource resource = pnlNavigation.getSelectedItem();
            if (resource instanceof Folder && resource != null) {
                txtResourceName.setValue(resource.getName());
                selectedResource = (Folder)resource;

                String currentPath = model.getCurrentPath();
                if (currentPath != null && currentPath.equals(model.getRootFolderId())) {
                    currentPath = null;
                }

                setCurrentFolderId(currentPath);

                enableParentOkButton();
            } else {
                // disable OK button if a folder is not selected
                txtResourceName.setValue(""); //$NON-NLS-1$
                disableParentOkButton();
            }
        }
    }
}
