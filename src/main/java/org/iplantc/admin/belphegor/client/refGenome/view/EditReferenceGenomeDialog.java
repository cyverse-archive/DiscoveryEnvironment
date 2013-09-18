package org.iplantc.admin.belphegor.client.refGenome.view;

import java.util.Date;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class EditReferenceGenomeDialog extends IPlantDialog implements Editor<ReferenceGenome> {

    private static EditReferenceGenomeDialogUiBinder uiBinder = GWT.create(EditReferenceGenomeDialogUiBinder.class);

    interface EditReferenceGenomeDialogUiBinder extends UiBinder<Widget, EditReferenceGenomeDialog> {}

    interface EditorDriver extends SimpleBeanEditorDriver<ReferenceGenome, EditReferenceGenomeDialog> {}

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @UiField
    FieldLabel nameLabel, pathLabel;
    @UiField
    TextField nameEditor, pathEditor, createdByEditor, lastModifiedByEditor;

    @UiField
    DateField createdDateEditor, lastModifiedDateEditor;

    @UiField
    CheckBox deletedEditor;

    private ReferenceGenome model;

    public static EditReferenceGenomeDialog addNewReferenceGenome() {
        ReferenceGenomeAutoBeanFactory factory = GWT.create(ReferenceGenomeAutoBeanFactory.class);
        ReferenceGenome refGenome = factory.referenceGenome().as();
        refGenome.setCreatedBy(UserInfo.getInstance().getUsername());
        refGenome.setLastModifiedBy(UserInfo.getInstance().getUsername());
        Date currDate = new Date();
        refGenome.setCreatedDate(currDate);
        refGenome.setLastModifiedDate(currDate);

        EditReferenceGenomeDialog dlg = new EditReferenceGenomeDialog(refGenome);
        dlg.setTitle(I18N.DISPLAY.addReferenceGenome());
        dlg.deletedEditor.setEnabled(false);
        return dlg;
    }

    public static EditReferenceGenomeDialog editReferenceGenome(ReferenceGenome refGenome) {
        EditReferenceGenomeDialog dlg = new EditReferenceGenomeDialog(refGenome);
        dlg.setTitle(refGenome.getName());
        dlg.deletedEditor.setEnabled(true);
        return dlg;
    }

    private EditReferenceGenomeDialog(ReferenceGenome refGenome) {
        add(uiBinder.createAndBindUi(this));
        getOkButton().setText("Save");

        nameLabel.setHTML(I18N.DISPLAY.requiredFieldLabel(I18N.DISPLAY.name()));
        pathLabel.setHTML(I18N.DISPLAY.requiredFieldLabel(I18N.DISPLAY.path()));

        editorDriver.initialize(this);
        editorDriver.edit(refGenome);
    }

    @Override
    protected void onButtonPressed(TextButton button) {
        if (button == getButtonBar().getItemByItemId(PredefinedButton.OK.name())) {
            model = editorDriver.flush();
            if (!editorDriver.hasErrors()) {
                super.onButtonPressed(button);
            }
        } else {
            super.onButtonPressed(button);
        }
    }

    public ReferenceGenome getReferenceGenome() {
        return model;
    }
}
