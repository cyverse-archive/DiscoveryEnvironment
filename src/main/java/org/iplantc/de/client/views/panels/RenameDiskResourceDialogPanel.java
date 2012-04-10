package org.iplantc.de.client.views.panels;

import org.iplantc.core.client.widgets.validator.JobNameValidator;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.extjs.gxt.ui.client.widget.Component;

public abstract class RenameDiskResourceDialogPanel extends IPlantPromptPanel {
    private final String id;
    private final String nameOrig;
    protected Component maskingParent;

    /**
     * Instantiate from an id and the folder's original name.
     * 
     * @param id unique id of folder to be re-named.
     * @param nameOrig original folder before re-naming.
     */
    public RenameDiskResourceDialogPanel(String caption, String id, String nameOrig,
            Component maskingParent) {
        super(caption, -1, new JobNameValidator());

        this.nameOrig = nameOrig;
        this.id = id;
        this.maskingParent = maskingParent;

        field.setValue(nameOrig);
    }

    private void doRename() {
        String name = field.getValue();

        if (name != null) {
            name = name.trim();

            if (name.length() > 0) {
                if ((nameOrig == null) || (!name.equals(nameOrig.trim()))) {
                    String srcName = id;
                    String destName = DiskResourceUtil.parseParent(id) + "/" + name; //$NON-NLS-1$

                    callRenameService(srcName, destName);
                }
            }
        }
    }

    protected abstract void callRenameService(String srcName, String destName);

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleOkClick() {
        doRename();
    }
}
