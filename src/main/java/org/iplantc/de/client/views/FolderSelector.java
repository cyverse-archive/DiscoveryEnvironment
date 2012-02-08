package org.iplantc.de.client.views;

import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog.DialogOkClickHandler;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.views.dialogs.FolderSelectDialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.user.client.Command;

/**
 * Models a composite widget for selecting a folder from a user's workspace.
 * 
 * @author psarando
 */
public class FolderSelector extends DiskResourceSelector implements IFolderSelector {

    private FolderSelectDialog dlgFolderSelect;
    private String defaultFolderId;

    /**
     * @return the defaultFolderId
     */
    public String getDefaultFolderId() {
        return defaultFolderId;
    }

    /**
     * Default constructor.
     */
    public FolderSelector() {
        this(null);
    }

    /**
     * Instantiate with command for when value changes.
     * 
     * @param cmdChange command to fire.
     */
    public FolderSelector(Command cmdChange) {
        super(cmdChange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void launchBrowseDialog(String tag) {
        dlgFolderSelect = new FolderSelectDialog(I18N.CONSTANT.selectAFolder(),
                I18N.CONSTANT.selectAFolder(), getSelectedFolder(), getCurrentFolderId());
        dlgFolderSelect.addOkClickHandler(new DialogOkClickHandler() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                setSelectedFolder(dlgFolderSelect.getSelectedFolder());
                setCurrentFolderId(dlgFolderSelect.getCurrentFolder());
                txtResourceName.setValue(dlgFolderSelect.getSelectedFolder().getId());

                if (cmdChange != null) {
                    cmdChange.execute();
                }
            }
        });

        // init for default case
        if (dlgFolderSelect.getCurrentFolder() == null) {
            dlgFolderSelect.setDefaultFolderId(defaultFolderId);
            dlgFolderSelect.setCurrentFolder(defaultFolderId);
            setCurrentFolderId(defaultFolderId);
        }
        dlgFolderSelect.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSelectedFolder() {
        return super.hasSelectedResource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder getSelectedFolder() {
        return (Folder)super.getSelectedResource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedFolder(Folder folder) {
        super.setSelectedResource(folder);

        dlgFolderSelect.select(folder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectedFolderId() {
        return super.getSelectedResourceId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayFolderName(String name) {
        super.displayResourceName(name);
    }

    public void setDefaultFolderId(String folderId) {
        defaultFolderId = folderId;
    }

}
