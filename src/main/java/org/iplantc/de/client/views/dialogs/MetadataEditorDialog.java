package org.iplantc.de.client.views.dialogs;

import org.iplantc.de.client.views.panels.DiskresourceMetadataEditorPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;

/**
 * 
 * A Dialog that display metadata
 * 
 * @author sriram
 * 
 */
public class MetadataEditorDialog extends Dialog {

    public MetadataEditorDialog(String title, DiskresourceMetadataEditorPanel mep,
            SelectionListener<ButtonEvent> OkBtnSelectionListener) {

        setButtons(Dialog.OKCANCEL);
        setHideOnButtonClick(true);
        Button ok_btn = (Button)getButtonBar().getItemByItemId(Dialog.OK);
        setHeading(title);
        ok_btn.addSelectionListener(OkBtnSelectionListener);
        add(mep);
    }

}
