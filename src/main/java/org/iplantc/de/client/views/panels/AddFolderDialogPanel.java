package org.iplantc.de.client.views.panels;

import org.iplantc.core.client.widgets.validator.AnalysisNameValidator;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceFacade;
import org.iplantc.de.client.services.callbacks.FolderCreateCallback;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * Simple dialog to allow the user to set a folder name.
 * 
 * @author amuir
 * 
 */
public class AddFolderDialogPanel extends IPlantPromptPanel {
    private final String idParent;
    private final Component maskingParent;

    /**
     * Construct an instance using an identifier of the parent folder.
     * 
     * @param idParent the parent folder's identifier
     */
    public AddFolderDialogPanel(String idParent, Component maskingParent) {
        super(I18N.DISPLAY.folderName(), -1, new AnalysisNameValidator());

        this.idParent = idParent;
        this.maskingParent = maskingParent;
    }

    private void doFolderAdd() {
        String name = field.getValue();

        if (name != null) {
            name = name.trim();

            if (name.length() > 0) {
                DiskResourceServiceFacade facade = new DiskResourceServiceFacade(maskingParent);
                facade.createFolder(idParent + "/" + name, new FolderCreateCallback(idParent, name)); //$NON-NLS-1$
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        doFolderAdd();
    }
}
