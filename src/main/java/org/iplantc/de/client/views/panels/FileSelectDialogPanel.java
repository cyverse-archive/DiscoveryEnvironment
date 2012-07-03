package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Inner panel for the file selector dialog.
 * 
 * @author lenards
 * 
 */
public class FileSelectDialogPanel extends ResourceSelectDialogPanel {
    /**
     * Construct an instance a file selection panel.
     * 
     * @param file
     * @param currentFolderId
     */
    public FileSelectDialogPanel(File file, String currentFolderId, String tag) {
        super(file, currentFolderId, tag);

        mainSelectionChangeListener = new SelectionChangeListenerImpl();
    }

    /**
     * Initialize all components used by this widget.
     */
    @Override
    protected void initComponents() {
        initComponents(I18N.DISPLAY.selectedFile());
    }

    /**
     * Retrieve the file which the user selected.
     * 
     * @return file the user has selected.
     */
    public File getSelectedFile() {
        return (File)selectedResource;
    }

    private class SelectionChangeListenerImpl implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            setParentOkButton();

            List<DiskResource> selectedItems = pnlMain.getSelectedItems();
            if (selectedItems != null && selectedItems.size() > 0) {
                DiskResource resource = selectedItems.get(0);
                if (resource instanceof File && resource != null) {
                    txtResourceName.setValue(resource.getName());
                    selectedResource = resource;
                    if (!model.getCurrentPath().equals(model.getRootFolderId())) {
                        setCurrentFolderId(model.getCurrentPath());
                    } else {
                        setCurrentFolderId(null);
                    }
                    enableParentOkButton();

                    return;
                }
            }

            // disable OK button if a file is not selected
            txtResourceName.setValue(""); //$NON-NLS-1$
            disableParentOkButton();
        }
    }
}
