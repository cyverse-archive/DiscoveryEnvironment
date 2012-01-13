package org.iplantc.de.client.views.dialogs;

import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;

/**
 * Dialog for submitting form panels when Ok is clicked.
 * 
 * @author amuir
 * 
 */
public class IPlantSubmittableDialog extends IPlantDialog {
    /**
     * Creates a new IPlantSubmittableDialog.
     * 
     * @param caption
     * @param width
     * @param panel
     */
    public IPlantSubmittableDialog(String caption, int width, IPlantDialogPanel panel) {
        super(caption, width, panel);
        setHideOnButtonClick(false);

        initCancelButton();
    }

    private void initCancelButton() {
        Button btnCancel = getButtonById(Dialog.CANCEL);

        btnCancel.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });
    }
}
