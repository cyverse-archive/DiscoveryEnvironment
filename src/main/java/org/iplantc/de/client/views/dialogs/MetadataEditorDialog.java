package org.iplantc.de.client.views.dialogs;

import org.iplantc.de.client.views.panels.MetadataEditorPanel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Command;

/**
 * 
 * A Dialog that display metadata
 * 
 * @author sriram
 * 
 */
public class MetadataEditorDialog extends Dialog {

    private final MetadataEditorPanel editorPanel;
    private final Button ok_btn;

    private final class OkButtonListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            if (editorPanel != null) {
                editorPanel.UpdateMetadata();
            }

        }
    }

    public MetadataEditorDialog(String title, final MetadataEditorPanel mep) {
        editorPanel = mep;
        editorPanel.setEditCompleteCallback(new EditCompleteCallback());
        setHeading(title);
        ButtonBar buttonBar = getButtonBar();
        buttonBar.setAlignment(HorizontalAlignment.RIGHT);
        setButtons(Dialog.OKCANCEL);
        buttonBar.add(new FillToolItem());
        setHideOnButtonClick(true);
        ok_btn = (Button)buttonBar.getItemByItemId(Dialog.OK);
        ok_btn.addSelectionListener(new OkButtonListener());
        add(mep);

    }

    /**
     * A callback class whose execute method is called after editing is complete. The execute method will
     * then call MetadataEditorPanel's isError to decide if ok button should be enabled or disabled
     * 
     * @author sriram
     * 
     */
    private class EditCompleteCallback implements Command {
        @Override
        public void execute() {
            if (editorPanel.isError()) {
                ok_btn.disable();
            } else {
                ok_btn.enable();
            }

        }
    }

}
