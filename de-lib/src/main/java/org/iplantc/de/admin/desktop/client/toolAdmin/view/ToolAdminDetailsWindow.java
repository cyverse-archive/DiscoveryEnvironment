package org.iplantc.de.admin.desktop.client.toolAdmin.view;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Created by aramsey on 10/30/15.
 */
public class ToolAdminDetailsWindow extends Composite implements Editor<ToolContainer>{

//    interface EditorDriver extends SimpleBeanEditorDriver<ToolContainer, ToolAdminDetailsWindow>{}
//    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    private static ToolAdminDetailsWindowUiBinder uiBinder = GWT.create(ToolAdminDetailsWindowUiBinder.class);
    interface ToolAdminDetailsWindowUiBinder extends UiBinder<Widget, ToolAdminDetailsWindow> {

    }

    @UiField
    VerticalLayoutContainer layoutContainer;

    @UiField
    TextField descriptionEditor;

    @UiField
    TextField toolImportNameLabel;

    @UiField
    TextField toolImportTypeLabel;

    @UiField
    TextField toolImportIDLabel;

    @UiField
    TextField containerDevicesHostPathLabel;

    @UiField
    TextField containerDevicesContainerPathLabel;

    @UiField
    TextField containerNameLabel;

    @UiField
    TextField containerWorkingDirLabel;

    @UiField
    TextField containerEntryPointLabel;

    @UiField
    TextField containerMemoryLimitLabel;

    @UiField
    TextField containerCPUSharesLabel;

    @UiField
    TextField containerVolumesHostPathLabel;

    @UiField
    TextField containerVolumesContainerPathLabel;

    @UiField
    TextField containerNetworkModeLabel;

    @UiField
    TextField containerImageNameLabel;

    @UiField
    TextField containerImageTagLabel;

    @UiField
    TextField containerImageURLLabel;

    @UiField
    TextField containerVolumesFromNameLabel;

    @UiField
    TextField containerVolumesFromTagLabel;

    @UiField
    TextField containerVolumesFromURLLabel;

    @UiField
    TextField containerVolumesFromNamePrefixLabel;

    @UiField
    TextField containerVolumesFromReadyOnlyLabel;

    @UiField
    TextField toolImportAttributionLabel;

    @UiField
    TextField toolImportVersionLabel;

    @UiField
    TextField toolImportLocationLabel;

    @UiField
    TextField toolImplementationImplementorLabel;

    @UiField
    TextField toolImplementationImplementorEmailLabel;

    @UiField
    TextField testToolDataParamsLabel;

    @UiField
    TextField testToolDataInputFilesLabel;

    @UiField
    TextField testToolOutputFilesLabel;

    private ToolAdminDetailsWindow(final Tool tool){
        initWidget(uiBinder.createAndBindUi(this));
        //editorDriver.initialize(this);
    }
    private ToolAdminDetailsWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        //editorDriver.initialize(this);
    }

    public ToolAdminDetailsWindow editToolDetails(Tool tool){
        return new ToolAdminDetailsWindow(tool);
    }

    public static ToolAdminDetailsWindow addToolDetails(){ return new ToolAdminDetailsWindow();}

    public void edit(ToolContainer container){
       // editorDriver.edit(container);
        layoutContainer.forceLayout();
    }

}
