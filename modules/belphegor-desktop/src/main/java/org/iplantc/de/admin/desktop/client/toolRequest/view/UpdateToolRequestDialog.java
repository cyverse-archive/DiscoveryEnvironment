package org.iplantc.de.admin.desktop.client.toolRequest.view;

import org.iplantc.de.client.models.toolRequest.ToolRequest;
import org.iplantc.de.client.models.toolRequest.ToolRequestAutoBeanFactory;
import org.iplantc.de.client.models.toolRequest.ToolRequestStatus;
import org.iplantc.de.client.models.toolRequest.ToolRequestUpdate;
import org.iplantc.de.commons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class UpdateToolRequestDialog extends IPlantDialog {

    private static UpdateToolRequestPanelUiBinder uiBinder = GWT.create(UpdateToolRequestPanelUiBinder.class);

    @UiTemplate("UpdateToolRequestPanel.ui.xml")
    interface UpdateToolRequestPanelUiBinder extends UiBinder<Widget, UpdateToolRequestDialog> {
    }

    @UiField
    Label currentStatusLabel;

    @UiField
    SimpleComboBox<ToolRequestStatus> statusCombo;

    @UiField
    TextArea commentsEditor;

    @UiField
    TextField statusField;

    private final ToolRequest toolRequest;

    private final ToolRequestAutoBeanFactory factory;

    public UpdateToolRequestDialog(ToolRequest toolRequest, ToolRequestAutoBeanFactory factory) {
        this.toolRequest = toolRequest;
        this.factory = factory;
        setHeadingText("Update Tool Request");
        getOkButton().setText("Submit");
        add(uiBinder.createAndBindUi(this));
        currentStatusLabel.setText(toolRequest.getStatus());
        statusField.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                statusCombo.reset();
            }
        });
        commentsEditor.setHeight(200);
    }

    @UiFactory
    SimpleComboBox<ToolRequestStatus> createComboBox() {
        SimpleComboBox<ToolRequestStatus> cb = new SimpleComboBox<ToolRequestStatus>(new LabelProvider<ToolRequestStatus>() {

            @Override
            public String getLabel(ToolRequestStatus item) {
                return item.name();
            }
        });
//        if (toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Completion.toString()) || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Evaluation.toString())
//                || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Failed.toString()) || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Installation.toString())
//                || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Pending.toString()) || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Submitted.toString())
//                || toolRequest.getStatus().equalsIgnoreCase(ToolRequestStatus.Validation.toString())) {
//
//            ToolRequestStatus status = ToolRequestStatus.fromTypeString(toolRequest.getStatus());
//            if (status != null) {
//                switch (status) {
//                    case Submitted:
//                        cb.add(ToolRequestStatus.Pending);
//                        cb.add(ToolRequestStatus.Evaluation);
//                        cb.add(ToolRequestStatus.Failed);
//                        break;
//                    case Pending:
//                        cb.add(ToolRequestStatus.Submitted);
//                        cb.add(ToolRequestStatus.Evaluation);
//                        cb.add(ToolRequestStatus.Installation);
//                        cb.add(ToolRequestStatus.Validation);
//                        break;
//                    case Evaluation:
//                        cb.add(ToolRequestStatus.Pending);
//                        cb.add(ToolRequestStatus.Installation);
//                        cb.add(ToolRequestStatus.Failed);
//                        break;
//                    case Installation:
//                        cb.add(ToolRequestStatus.Pending);
//                        cb.add(ToolRequestStatus.Installation);
//                        cb.add(ToolRequestStatus.Validation);
//                        cb.add(ToolRequestStatus.Failed);
//                        break;
//                    case Validation:
//                        cb.add(ToolRequestStatus.Pending);
//                        cb.add(ToolRequestStatus.Completion);
//                        cb.add(ToolRequestStatus.Failed);
//                        break;
//                    case Completion:
//                        cb.add(ToolRequestStatus.Validation);
//                        break;
//                    case Failed:
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        } else {
            cb.add(ToolRequestStatus.Submitted);
            cb.add(ToolRequestStatus.Pending);
            cb.add(ToolRequestStatus.Evaluation);
            cb.add(ToolRequestStatus.Installation);
            cb.add(ToolRequestStatus.Validation);
            cb.add(ToolRequestStatus.Completion);
            cb.add(ToolRequestStatus.Failed);
        // }

        cb.addSelectionHandler(new SelectionHandler<ToolRequestStatus>() {

            @Override
            public void onSelection(SelectionEvent<ToolRequestStatus> event) {
                if (statusField != null) {
                    statusField.clear();
                }

            }
        });
        return cb;
    }

    public ToolRequestUpdate getToolRequestUpdate() {
        ToolRequestUpdate trUpdate = factory.update().as();
        trUpdate.setStatus(statusCombo.getCurrentValue() != null ? (statusCombo.getCurrentValue().toString()) : statusField.getCurrentValue());
        trUpdate.setComments(commentsEditor.getCurrentValue());
        return trUpdate;
    }

}
