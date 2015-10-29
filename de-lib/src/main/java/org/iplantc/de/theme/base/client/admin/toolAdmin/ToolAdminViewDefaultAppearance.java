package org.iplantc.de.theme.base.client.admin.toolAdmin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.resources.client.IplantResources;

/**
 * Created by aramsey on 10/26/15.
 */
public class ToolAdminViewDefaultAppearance implements ToolAdminView.ToolAdminViewAppearance {

    private final ToolAdminDisplayStrings displayStrings;
    private final IplantResources iplantResources;

    public ToolAdminViewDefaultAppearance(){
        this (GWT.<ToolAdminDisplayStrings> create(ToolAdminDisplayStrings.class),
                GWT.<IplantResources> create(IplantResources.class));

    }

    ToolAdminViewDefaultAppearance(final ToolAdminDisplayStrings displayStrings,
                                   final IplantResources iplantResources) {
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
    }

    @Override
    public String add(){
        return displayStrings.add();
    }

    @Override
    public ImageResource addIcon(){
        return iplantResources.add();
    }

    @Override
    public String delete(){
        return displayStrings.delete();
    }

    @Override
    public ImageResource deleteIcon(){
        return iplantResources.delete();
    }

    @Override
    public String nameColumnLabel () {
        return displayStrings.nameColumnLabel();
    }

    @Override
    public int nameColumnWidth(){
        return 90;
    }

    @Override
    public String descriptionColumnLabel () {
        return displayStrings.descriptionColumnLabel();
    }

    @Override
    public int descriptionColumnWidth(){
        return 90;
    }

    @Override
    public String attributionColumnLabel () {
        return displayStrings.attributionColumnLabel();
    }

    @Override
    public int attributionColumnWidth(){
        return 90;
    }

    @Override
    public String locationColumnInfoLabel () {
        return displayStrings.locationColumnInfoLabel();
    }

    @Override
    public int locationColumnInfoWidth(){
        return 90;
    }

    @Override
    public String versionColumnInfoLabel () {
        return displayStrings.versionColumnInfoLabel();
    }

    @Override
    public int versionColumnInfoWidth(){
        return 90;
    }

    @Override
    public String typeColumnInfoLabel () {
        return displayStrings.typeColumnInfoLabel();
    }

    @Override
    public int typeColumnInfoWidth(){
        return 90;
    }

    @Override
    public String searchLabel () {
        return displayStrings.searchLabel();
    }

    @Override
    public String toolImportDescriptionLabel () {
        return displayStrings.toolImportDescriptionLabel();
    }

    @Override
    public String toolImportNameLabel () {
        return displayStrings.toolImportNameLabel();
    }

    @Override
    public String toolImportTypeLabel () {
        return displayStrings.toolImportTypeLabel();
    }

    @Override
    public String toolImportIDLabel () {
        return displayStrings.toolImportIDLabel();
    }

    @Override
    public String containerDevicesHostPathLabel () {
        return displayStrings.containerDevicesHostPathLabel();
    }

    @Override
    public String containerDevicesContainerPathLabel () {
        return displayStrings.containerDevicesContainerPathLabel();
    }

    @Override
    public String containerNameLabel () {
        return displayStrings.containerNameLabel();
    }

    @Override
    public String containerWorkingDirLabel () {
        return displayStrings.containerWorkingDirLabel();
    }

    @Override
    public String containerEntryPointLabel () {
        return displayStrings.containerEntryPointLabel();
    }

    @Override
    public String containerMemoryLimitLabel () {
        return displayStrings.containerMemoryLimitLabel();
    }

    @Override
    public String containerCPUSharesLabel () {
        return displayStrings.containerCPUSharesLabel();
    }

    @Override
    public String containerVolumesHostPathLabel () {
        return displayStrings.containerVolumesHostPathLabel();
    }

    @Override
    public String containerVolumesContainerPathLabel () {
        return displayStrings.containerVolumesContainerPathLabel();
    }

    @Override
    public String containerNetworkModeLabel () {
        return displayStrings.containerNetworkModeLabel();
    }

    @Override
    public String containerImageNameLabel () {
        return displayStrings.containerImageNameLabel();
    }

    @Override
    public String containerImageTagLabel () {
        return displayStrings.containerImageTagLabel();
    }

    @Override
    public String containerImageURLLabel () {
        return displayStrings.containerImageURLLabel();
    }

    @Override
    public String containerVolumesFromNameLabel () {
        return displayStrings.containerVolumesFromNameLabel();
    }

    @Override
    public String containerVolumesFromTagLabel () {
        return displayStrings.containerVolumesFromTagLabel();
    }

    @Override
    public String containerVolumesFromURLLabel () {
        return displayStrings.containerVolumesFromURLLabel();
    }

    @Override
    public String containerVolumesFromNamePrefixLabel () {
        return displayStrings.containerVolumesFromNamePrefixLabel();
    }

    @Override
    public String containerVolumesFromReadyOnlyLabel () {
        return displayStrings.containerVolumesFromReadyOnlyLabel();
    }

    @Override
    public String toolImportAttributionLabel () {
        return displayStrings.toolImportAttributionLabel();
    }

    @Override
    public String toolImportVersionLabel () {
        return displayStrings.toolImportVersionLabel();
    }

    @Override
    public String toolImportLocationLabel () {
        return displayStrings.toolImportLocationLabel();
    }

    @Override
    public String toolImplementationImplementorLabel () {
        return displayStrings.toolImplementationImplementorLabel();
    }

    @Override
    public String toolImplementationImplementorEmailLabel () {
        return displayStrings.toolImplementationImplementorEmailLabel();
    }

    @Override
    public String testToolDataParamsLabel () {
        return displayStrings.testToolDataParamsLabel();
    }

    @Override
    public String testToolDataInputFilesLabel () {
        return displayStrings.testToolDataInputFilesLabel();
    }

    @Override
    public String testToolOutputFilesLabel () {
        return displayStrings.testToolOutputFilesLabel();
    }

}
