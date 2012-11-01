package org.iplantc.de.client.desktop.widget;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantSubmittableDialog;
import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent.RequestBulkDownloadEventHandler;
import org.iplantc.core.uidiskresource.client.events.RequestBulkUploadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestBulkUploadEvent.RequestBulkUploadEventHandler;
import org.iplantc.core.uidiskresource.client.events.RequestImportFromUrlEvent;
import org.iplantc.core.uidiskresource.client.events.RequestImportFromUrlEvent.RequestImportFromUrlEventHandler;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent.RequestSimpleDownloadEventHandler;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent.RequestSimpleUploadEventHandler;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.File;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.dispatchers.IDropLiteWindowDispatcher;
import org.iplantc.de.client.dispatchers.SimpleDownloadWindowDispatcher;
import org.iplantc.de.client.events.AsyncUploadCompleteHandler;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.views.panels.FileUploadDialogPanel;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.common.collect.Lists;

public class DesktopFileTransferEventHandler implements RequestBulkDownloadEventHandler,
        RequestBulkUploadEventHandler, RequestImportFromUrlEventHandler,
        RequestSimpleDownloadEventHandler, RequestSimpleUploadEventHandler {

    private IPlantSubmittableDialog dlgUpload;

    @Override
    public void onRequestSimpleUpload(RequestSimpleUploadEvent event) {
        Folder uploadDest = event.getDestinationFolder();
        promptUploadImportForm(FileUploadDialogPanel.MODE.FILE_ONLY, uploadDest);
    }

    @Override
    public void onRequestUploadFromUrl(RequestImportFromUrlEvent event) {
        Folder uploadDest = event.getDestinationFolder();
        promptUploadImportForm(FileUploadDialogPanel.MODE.URL_ONLY, uploadDest);
    }

    @Override
    public void onRequestBulkUpload(RequestBulkUploadEvent event) {
        Folder uploadDest = event.getDestinationFolder();
        if (canUpload(uploadDest)) {
            IDropLiteWindowDispatcher dispatcher = new IDropLiteWindowDispatcher();
            dispatcher.launchUploadWindow(uploadDest.getId(), uploadDest.getId());
        }
    }

    @Override
    public void onRequestSimpleDownload(RequestSimpleDownloadEvent event) {
        List<DiskResource> resources = Lists.newArrayList(event.getRequestedResources());
        if (isDownloadable(resources)) {
            if (resources.size() == 1) {
                // Download now
                Services.DISK_RESOURCE_SERVICE.simpleDownload(resources.get(0).getId());
            } else {
                List<String> paths = new ArrayList<String>();

                for (DiskResource resource : resources) {
                    if (resource instanceof File) {
                        paths.add(resource.getId());
                    }
                }

                SimpleDownloadWindowDispatcher dispatcher = new SimpleDownloadWindowDispatcher();
                dispatcher.launchDownloadWindow(paths);
            }
        } else {
            showErrorMsg();
        }

    }

    @Override
    public void onRequestBulkDownload(RequestBulkDownloadEvent event) {
        List<DiskResource> resources = Lists.newArrayList(event.getRequestedResources());
        if (isDownloadable(resources)) {

            IDropLiteWindowDispatcher dispatcher = new IDropLiteWindowDispatcher();
            dispatcher.launchDownloadWindow(resources);
        } else {
            showErrorMsg();
        }
    }

    private boolean isDownloadable(List<DiskResource> resources) {
        if ((resources == null) || resources.isEmpty()) {
            return false;
        }

        for (DiskResource dr : resources) {
            if (!dr.getPermissions().isReadable()) {
                return false;
            }
        }
        return true;
    }

    private boolean canUpload(Folder uploadDest) {
        if (uploadDest != null && DataUtils.canUploadToThisFolder(uploadDest)) {
            return true;
        } else {
            showErrorMsg();
            return false;
        }
    }

    private void showErrorMsg() {
        // TODO JDS Use a GXT3 message window
        MessageBox.alert(I18N.DISPLAY.permissionErrorTitle(), I18N.DISPLAY.permissionErrorMessage(),
                null);
    }

    private void promptUploadImportForm(FileUploadDialogPanel.MODE mode, Folder uploadDest) {

        if (canUpload(uploadDest)) {
            String uploadDestId = uploadDest.getId();
            String username = UserInfo.getInstance().getUsername();

            // provide key/value pairs for hidden fields
            FastMap<String> hiddenFields = new FastMap<String>();
            hiddenFields.put(FileUploadDialogPanel.HDN_PARENT_ID_KEY, uploadDestId);
            hiddenFields.put(FileUploadDialogPanel.HDN_USER_ID_KEY, username);

            // define a handler for upload completion
            AsyncUploadCompleteHandler handler = new AsyncUploadCompleteHandler(uploadDestId) {
                @Override
                public void onAfterCompletion() {
                    if (dlgUpload != null) {
                        dlgUpload.hide();
                    }
                }
            };

            FileUploadDialogPanel pnlUpload = new FileUploadDialogPanel(hiddenFields,
                    Constants.CLIENT.fileUploadServlet(), handler, mode);

            dlgUpload = new IPlantSubmittableDialog(I18N.DISPLAY.upload(), 536, pnlUpload);
            dlgUpload.show();
        }
    }

}
