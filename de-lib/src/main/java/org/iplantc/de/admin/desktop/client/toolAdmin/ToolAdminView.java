package org.iplantc.de.admin.desktop.client.toolAdmin;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;

import java.util.List;

/**
 * Created by aramsey on 10/26/15.
 */
public interface ToolAdminView extends IsWidget, IsMaskable{

    public interface ToolAdminViewAppearance {

        String add();

        ImageResource addIcon();

        String delete();

        ImageResource deleteIcon();

        String nameColumnLabel();

        int nameColumnWidth();

        String descriptionColumnLabel();

        int descriptionColumnWidth();

        String attributionColumnLabel();

        int attributionColumnWidth();

        String locationColumnInfoLabel();

        int locationColumnInfoWidth();

        String versionColumnInfoLabel();

        int versionColumnInfoWidth();

        String typeColumnInfoLabel();

        int typeColumnInfoWidth();

        String searchLabel();

        String toolImportDescriptionLabel();

        String toolImportNameLabel();

        String toolImportTypeLabel();

        String toolImportIDLabel();

        String containerDevicesHostPathLabel();

        String containerDevicesContainerPathLabel();

        String containerNameLabel();

        String containerWorkingDirLabel();

        String containerEntryPointLabel();

        String containerMemoryLimitLabel();

        String containerCPUSharesLabel();

        String containerVolumesHostPathLabel();

        String containerVolumesContainerPathLabel();

        String containerNetworkModeLabel();

        String containerImageNameLabel();

        String containerImageTagLabel();

        String containerImageURLLabel();

        String containerVolumesFromNameLabel();

        String containerVolumesFromTagLabel();

        String containerVolumesFromURLLabel();

        String containerVolumesFromNamePrefixLabel();

        String containerVolumesFromReadyOnlyLabel();

        String toolImportAttributionLabel();

        String toolImportVersionLabel();

        String toolImportLocationLabel();

        String toolImplementationImplementorLabel();

        String toolImplementationImplementorEmailLabel();

        String testToolDataParamsLabel();

        String testToolDataInputFilesLabel();

        String testToolOutputFilesLabel();

    }

    public interface Presenter {

        void go(HasOneWidget container);

        /**
         * Adds a tool by calling the
         * @param tool
         */
        void addTool(Tool tool);



    }

    void setPresenter (Presenter presenter);

    void setToolList(List<Tool> tools);


}
