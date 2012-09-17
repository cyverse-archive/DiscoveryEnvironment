package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceFacade;
import org.iplantc.de.client.services.callbacks.FolderRenameCallback;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * Provides a user interface for prompting a user for renaming a folder.
 */
public class RenameFolderDialogPanel extends RenameDiskResourceDialogPanel {

    /**
     * Instantiate from an id, the folder's original name, and a component to unmask in the callback.
     * 
     * @param id unique id of folder to be re-named.
     * @param nameOrig original folder before re-naming.
     * @param maskingParent a component that will be unmasked in the service callback.
     */
    public RenameFolderDialogPanel(String id, String nameOrig, Component maskingParent) {
        super(I18N.DISPLAY.folderName(), id, nameOrig, maskingParent);
    }

    @Override
    protected void callRenameService(String srcName, String destName) {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade(maskingParent);
        facade.renameFolder(srcName, destName, new FolderRenameCallback(srcName, destName));
    }
}
