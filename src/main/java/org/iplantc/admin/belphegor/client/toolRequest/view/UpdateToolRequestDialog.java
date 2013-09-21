package org.iplantc.admin.belphegor.client.toolRequest.view;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestStatus;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class UpdateToolRequestDialog extends IPlantDialog {

    private static UpdateToolRequestPanelUiBinder uiBinder = GWT.create(UpdateToolRequestPanelUiBinder.class);

    @UiTemplate("UpdateToolRequestPanel.ui.xml")
    interface UpdateToolRequestPanelUiBinder extends UiBinder<Widget, UpdateToolRequestDialog> {
    }

    @UiField
    Label currentStatusLabel;

    @UiField()
    SimpleComboBox<ToolRequestStatus> statusCombo;

    private final ToolRequest toolRequest;

    private final ToolRequestAutoBeanFactory factory;

    public UpdateToolRequestDialog(ToolRequest toolRequest, ToolRequestAutoBeanFactory factory) {
        this.toolRequest = toolRequest;
        this.factory = factory;
        setHeadingText("Update Tool Request");
        getOkButton().setText("Submit");
        add(uiBinder.createAndBindUi(this));
        currentStatusLabel.setText(toolRequest.getStatus().name());
    }

    @UiFactory
    SimpleComboBox<ToolRequestStatus> createComboBox() {
        SimpleComboBox<ToolRequestStatus> cb = new SimpleComboBox<ToolRequestStatus>(new LabelProvider<ToolRequestStatus>() {

            @Override
            public String getLabel(ToolRequestStatus item) {
                return item.name();
            }
        });
        
        switch (toolRequest.getStatus()) {
            case Submitted:
                cb.add(ToolRequestStatus.Pending);
                cb.add(ToolRequestStatus.Evaluation);
                cb.add(ToolRequestStatus.Failed);
                break;
            case Pending:
                cb.add(ToolRequestStatus.Submitted);
                cb.add(ToolRequestStatus.Evaluation);
                cb.add(ToolRequestStatus.Installation);
                cb.add(ToolRequestStatus.Validation);
                break;
            case Evaluation:
                cb.add(ToolRequestStatus.Pending);
                cb.add(ToolRequestStatus.Installation);
                cb.add(ToolRequestStatus.Failed);
                break;
            case Installation:
                cb.add(ToolRequestStatus.Pending);
                cb.add(ToolRequestStatus.Installation);
                cb.add(ToolRequestStatus.Validation);
                cb.add(ToolRequestStatus.Failed);
                break;
            case Validation:
                cb.add(ToolRequestStatus.Pending);
                cb.add(ToolRequestStatus.Completion);
                cb.add(ToolRequestStatus.Failed);
                break;
            case Completion:
                cb.add(ToolRequestStatus.Validation);
                break;
            case Failed:
                break;

            default:
                break;
        }

        return cb;
    }

    public ToolRequestUpdate getToolRequestUpdate() {
        ToolRequestUpdate trUpdate = factory.update().as();
        trUpdate.setId(toolRequest.getId());
        trUpdate.setStatus(statusCombo.getCurrentValue());
        return trUpdate;
    }

}
