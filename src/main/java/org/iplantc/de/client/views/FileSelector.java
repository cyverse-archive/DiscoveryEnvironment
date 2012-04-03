package org.iplantc.de.client.views;

import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog.DialogOkClickHandler;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.dialogs.FileSelectDialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.user.client.Command;

/**
 * Models a composite widget for selecting a file from a user's workspace.
 * 
 * @author lenards
 * 
 */
public class FileSelector extends DiskResourceSelector implements IFileSelector {

    private FileSelectDialog dlgFileSelect;

    /**
     * Default constructor.
     */
    public FileSelector(String tag) {
        super(null, tag);
    }

    /**
     * Instantiate with command for when value changes.
     * 
     * @param cmdChange command to fire.
     * @param tag tag to this widget
     */
    public FileSelector(Command cmdChange, String tag) {
        super(cmdChange, tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void launchBrowseDialog(String tag) {
        dlgFileSelect = new FileSelectDialog(I18N.CONSTANT.selectAFile(), I18N.CONSTANT.selectAFile(),
                getSelectedFile(), getCurrentFolderId());
        dlgFileSelect.addOkClickHandler(new DialogOkClickHandler() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                setSelectedFile(dlgFileSelect.getSelectedFile());
                setCurrentFolderId(dlgFileSelect.getCurrentFolder());
                txtResourceName.setValue(getSelectedResourceName());

                if (cmdChange != null) {
                    cmdChange.execute();
                }
            }
        });

        dlgFileSelect.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSelectedFile() {
        return super.hasSelectedResource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getSelectedFile() {
        return (File)super.getSelectedResource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedFile(File file) {
        super.setSelectedResource(file);

        if (dlgFileSelect != null) {
            dlgFileSelect.select(file);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectedFileId() {
        return super.getSelectedResourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayFilename(String name) {
        super.displayResourceName(name);
    }

}
