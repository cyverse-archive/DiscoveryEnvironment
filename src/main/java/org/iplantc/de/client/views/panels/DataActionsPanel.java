package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.IDropLiteWindowDispatcher;
import org.iplantc.de.client.dispatchers.SimpleDownloadWindowDispatcher;
import org.iplantc.de.client.services.FileDeleteCallback;
import org.iplantc.de.client.services.FolderDeleteCallback;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.utils.DataViewContextExecutor;
import org.iplantc.de.client.utils.TreeViewContextExecutor;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;
import org.iplantc.de.client.views.dialogs.MetadataEditorDialog;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;

public class DataActionsPanel extends ContentPanel {
    private List<DiskResource> resources;
    private Component maskingParent;

    public DataActionsPanel() {
        init();
    }

    private void init() {
        setHeaderVisible(false);
        setVisible(false);
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
    }

    public void update(final List<DiskResource> resources) {
        this.resources = resources;

        if (resources == null || resources.isEmpty()) {
            hide();
        } else {
            List<DataUtils.Action> actions = DataUtils.getSupportedActions(resources);

            if (actions.isEmpty()) {
                hide();
            } else {
                addLinks(actions);
                show();
            }
        }
    }

    public void setMaskingParent(ContentPanel maskingParent) {
        this.maskingParent = maskingParent;
    }

    private Hyperlink buildLink(final String text, final Listener<ComponentEvent> clickHandler) {
        Hyperlink ret = new Hyperlink(text, "de_hyperlink"); //$NON-NLS-1$

        if (clickHandler != null) {
            ret.addListener(Events.OnClick, clickHandler);
        }

        return ret;
    }

    private Listener<ComponentEvent> buildHandler(DataUtils.Action action) {
        Listener<ComponentEvent> ret = null; // assume failure

        switch (action) {
            case RenameFolder:
                ret = new FolderRenameListenerImpl();
                break;

            case RenameFile:
                ret = new FileRenameListenerImpl();
                break;

            case Delete:
                ret = new DeleteListenerImpl();
                break;

            case View:
                ret = new ViewListenerImpl();
                break;

            case ViewTree:
                ret = new ViewTreeListenerImpl();
                break;

            case SimpleDownload:
                ret = new SimpleDownloadListenerImpl();
                break;

            case BulkDownload:
                ret = new DownloadListenerImpl();
                break;

            case Metadata:
                ret = new MetadataListenerImpl();
                break;

            default:
                break;
        }

        return ret;
    }

    private void addLinks(final List<DataUtils.Action> actions) {
        removeAll();

        if (actions != null) {
            for (DataUtils.Action action : actions) {
                add(buildLink(action.toString(), buildHandler(action)));
            }

            layout();
        }
    }

    private class FolderRenameListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            DiskResource resource = resources.get(0);
            if (DataUtils.isRenamable(resource)) {
                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 340,
                        new RenameFolderDialogPanel(resource.getId(), resource.getName(), maskingParent));

                dlg.show();
            } else {
                showErrorMsg();
            }
        }
    }

    private class FileRenameListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            DiskResource resource = resources.get(0);
            if (DataUtils.isRenamable(resource)) {
                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 320,
                        new RenameFileDialogPanel(resource.getId(), resource.getName(), maskingParent));

                dlg.show();
            } else {
                showErrorMsg();
            }

        }
    }

    private class MetadataListenerImpl implements Listener<ComponentEvent> {

        @Override
        public void handleEvent(ComponentEvent be) {
            DiskResource dr = resources.get(0);
            final MetadataEditorPanel mep = new DiskresourceMetadataEditorPanel(dr);

            MetadataEditorDialog d = new MetadataEditorDialog(
                    I18N.DISPLAY.metadata() + ":" + dr.getId(), mep); //$NON-NLS-1$

            d.setSize(500, 300);
            d.setResizable(false);
            d.show();
        }
    }

    private void showErrorMsg() {
        MessageBox.alert(I18N.DISPLAY.permissionErrorTitle(), I18N.DISPLAY.permissionErrorMessage(),
                null);
    }

    private class DeleteListenerImpl implements Listener<ComponentEvent> {
        private void doDelete() {
            final List<String> idFolders = new ArrayList<String>();
            final List<String> idFiles = new ArrayList<String>();

            // first we need to fill our id lists
            final Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();
                    if (btn.getItemId().equals(Dialog.YES)) {
                        for (DiskResource resource : resources) {
                            if (resource instanceof Folder) {
                                idFolders.add(resource.getId());
                            } else if (resource instanceof File) {
                                idFiles.add(resource.getId());
                            }
                        }

                        FolderServiceFacade facade = new FolderServiceFacade(maskingParent);

                        if (idFiles.size() > 0) {
                            facade.deleteFiles(JsonUtil.buildJsonArrayString(idFiles),
                                    new FileDeleteCallback(idFiles));
                        }

                        if (idFolders.size() > 0) {
                            facade.deleteFolders(JsonUtil.buildJsonArrayString(idFolders),
                                    new FolderDeleteCallback(idFolders));
                        }
                    }
                }
            };
            if (DataUtils.isDeletable(resources)) {
                MessageBox.confirm(I18N.DISPLAY.deleteFilesTitle(), I18N.DISPLAY.deleteFilesMsg(),
                        callback);
            } else {
                showErrorMsg();
            }
        }

        @Override
        public void handleEvent(ComponentEvent be) {
            final Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();

                    // did the user click yes?
                    if (btn.getItemId().equals(Dialog.YES)) {
                        doDelete();
                    }
                }
            };

            boolean folderselected = false;

            for (DiskResource resource : resources) {
                if (resource instanceof Folder) {
                    folderselected = true;
                    break;
                }
            }

            if (folderselected) {
                MessageBox.confirm(I18N.DISPLAY.warning(), I18N.DISPLAY.folderDeleteWarning(), callback);
            } else {
                doDelete();
            }
        }
    }

    private class ViewListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {

            if (DataUtils.isViewable(resources)) {
                List<String> contexts = new ArrayList<String>();

                DataContextBuilder builder = new DataContextBuilder();

                for (DiskResource resource : resources) {
                    contexts.add(builder.build(resource.getId()));
                }

                DataViewContextExecutor executor = new DataViewContextExecutor();
                executor.execute(contexts);
            } else {
                showErrorMsg();
            }
        }
    }

    private class ViewTreeListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            if (DataUtils.isViewable(resources)) {
                DataContextBuilder builder = new DataContextBuilder();

                TreeViewContextExecutor executor = new TreeViewContextExecutor();
                executor.execute(builder.build(resources.get(0).getId()));
            } else {
                showErrorMsg();
            }
        }
    }

    private class SimpleDownloadListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            if (DataUtils.isDownloadable(resources)) {
                if (resources.size() == 1) {
                    downloadNow(resources.get(0).getId());
                } else {
                    launchDownloadWindow();
                }
            } else {
                showErrorMsg();
            }
        }

        private void downloadNow(String path) {
            FolderServiceFacade service = new FolderServiceFacade();
            service.simpleDownload(path);
        }

        private void launchDownloadWindow() {
            List<String> paths = new ArrayList<String>();

            for (DiskResource resource : resources) {
                if (resource instanceof File) {
                    paths.add(resource.getId());
                }
            }

            SimpleDownloadWindowDispatcher dispatcher = new SimpleDownloadWindowDispatcher();
            dispatcher.launchDownloadWindow(paths);
        }
    }

    private class DownloadListenerImpl implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {

            if (DataUtils.isDownloadable(resources)) {
                IDropLiteWindowDispatcher dispatcher = new IDropLiteWindowDispatcher();
                dispatcher.launchDownloadWindow(resources);
            } else {
                showErrorMsg();
            }
        }
    }
}
