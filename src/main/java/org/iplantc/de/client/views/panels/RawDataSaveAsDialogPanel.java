package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.RawDataSaveAsCallback;
import org.iplantc.de.client.services.RawDataServices;

import com.extjs.gxt.ui.client.widget.MessageBox;

/**
 * Provides a user interface for "save as" operations with raw data.
 */
public class RawDataSaveAsDialogPanel extends IPlantPromptPanel {
    protected FileIdentifier file;
    protected String data;
    private final MessageBox wait;

    /**
     * Instantiate from a file identifier, data and wait message box.
     * 
     * @param file file associated with this panel.
     * @param data data to be saved.
     * @param wait message box to display while saving.
     */
    public RawDataSaveAsDialogPanel(FileIdentifier file, String data, MessageBox wait) {
        super(I18N.DISPLAY.fileName());

        this.file = file;
        this.data = data;
        this.wait = wait;
        field.setValue(file.getFilename());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        String name = field.getValue();

        if (name != null) {
            name = name.trim();

            if (name.length() > 0) {
                UserInfo uinfo = UserInfo.getInstance();
                // temp strings for readability
                String idParent = file.getParentId();
                String idFile = file.getFileId();
                String idWorkspace = uinfo.getWorkspaceId();
                RawDataServices.saveAsRawData(idWorkspace, idParent, idFile, field.getValue(), data,
                        new RawDataSaveAsCallback(idParent, idFile, wait));
            }
        }
    }
}
