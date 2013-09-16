package org.iplantc.admin.belphegor.client.refGenome.view;

import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class EditReferenceGenomeDialog extends IPlantDialog implements Editor<ReferenceGenome> {

    private static EditReferenceGenomeDialogUiBinder uiBinder = GWT.create(EditReferenceGenomeDialogUiBinder.class);

    interface EditReferenceGenomeDialogUiBinder extends UiBinder<Widget, EditReferenceGenomeDialog> {}

    @UiField
    TextField nameEditor, pathEditor, createdByEditor, lastModifiedByEditor;

    @UiField
    DateField createdDateEditor, lastModifiedDateEditor;

    @UiField
    CheckBox deletedEditor;

    public EditReferenceGenomeDialog() {
        add(uiBinder.createAndBindUi(this));
    }

}
