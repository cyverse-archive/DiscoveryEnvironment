package org.iplantc.de.client.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.IDropLiteWindowDispatcher;
import org.iplantc.de.client.dispatchers.SimpleDownloadWindowDispatcher;
import org.iplantc.de.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.client.events.DiskResourceSelectionChangedEventHandler;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileDeleteCallback;
import org.iplantc.de.client.services.FolderDeleteCallback;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.utils.DataUtils.Action;
import org.iplantc.de.client.utils.DataViewContextExecutor;
import org.iplantc.de.client.utils.TreeViewContextExecutor;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;
import org.iplantc.de.client.views.dialogs.MetadataEditorDialog;
import org.iplantc.de.client.views.dialogs.SharingDialog;
import org.iplantc.de.client.views.panels.AddFolderDialogPanel;
import org.iplantc.de.client.views.panels.DiskresourceMetadataEditorPanel;
import org.iplantc.de.client.views.panels.MetadataEditorPanel;
import org.iplantc.de.client.views.panels.RenameFileDialogPanel;
import org.iplantc.de.client.views.panels.RenameFolderDialogPanel;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public final class DataActionsMenu extends Menu {
    private final ArrayList<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

    private final String tag;

    private List<DiskResource> resources;
    private Component maskingParent;

    private MenuItem itemAddFolder;
    private MenuItem itemRenameResource;
    private MenuItem itemViewResource;
    private MenuItem itemViewTree;
    private MenuItem itemSimpleDownloadResource;
    private MenuItem itemBulkDownloadResource;
    private MenuItem itemDeleteResource;
    private MenuItem itemMetaData;
    private MenuItem itemShareResource;

    public DataActionsMenu(final String tag) {
        this.tag = tag;
        resources = Collections.<DiskResource> emptyList();
        buildActions();
        registerHandlers();
    }

    /**
     * Builds an action menu with items for adding, renaming, viewing, downloading, and deleting disk
     * resources.
     */
    private void buildActions() {
        buildAddfolderMenuItem();

        buildRenameResourceMenuItem();

        buildViewResourceMenuItem();

        buildViewTreeMenuItem();

        buildSimpleDownloadMenuItem();

        buildBulDownloadMenuItem();

        buildDeleteResourceMenuItem();

        buildMetaDataMenuItem();

        buildShareResourceMenuItem();

        add(itemAddFolder);
        add(itemRenameResource);
        add(itemViewResource);
        add(itemViewTree);
        add(itemSimpleDownloadResource);
        add(itemBulkDownloadResource);
        add(itemDeleteResource);
        add(itemMetaData);
        add(itemShareResource);
    }

    private void buildShareResourceMenuItem() {
        itemShareResource = new MenuItem();
        itemShareResource.setText(I18N.DISPLAY.share());
        itemShareResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.share()));
        itemShareResource.addSelectionListener(new ShareResourceListenerImpl());
    }

    private void buildMetaDataMenuItem() {
        itemMetaData = new MenuItem();
        itemMetaData.setText(I18N.DISPLAY.metadata());
        itemMetaData.setIcon(AbstractImagePrototype.create(Resources.ICONS.metadata()));
        itemMetaData.addSelectionListener(new MetadataListenerImpl());
    }

    private void buildDeleteResourceMenuItem() {
        itemDeleteResource = new MenuItem();
        itemDeleteResource.setText(I18N.DISPLAY.delete());
        itemDeleteResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderDelete()));
        itemDeleteResource.addSelectionListener(new DeleteListenerImpl());
    }

    private void buildBulDownloadMenuItem() {
        itemBulkDownloadResource = new MenuItem();
        itemBulkDownloadResource.setText(I18N.DISPLAY.bulkDownload());
        itemBulkDownloadResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.download()));
        itemBulkDownloadResource.addSelectionListener(new BulkDownloadListenerImpl());
    }

    private void buildSimpleDownloadMenuItem() {
        itemSimpleDownloadResource = new MenuItem();
        itemSimpleDownloadResource.setText(I18N.DISPLAY.simpleDownload());
        itemSimpleDownloadResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.download()));
        itemSimpleDownloadResource.addSelectionListener(new SimpleDownloadListenerImpl());
    }

    private void buildViewTreeMenuItem() {
        itemViewTree = new MenuItem();
        itemViewTree.setText(I18N.DISPLAY.viewTreeViewer());
        itemViewTree.setIcon(AbstractImagePrototype.create(Resources.ICONS.fileView()));
        itemViewTree.addSelectionListener(new ViewTreeListenerImpl());
    }

    private void buildViewResourceMenuItem() {
        itemViewResource = new MenuItem();
        itemViewResource.setText(I18N.DISPLAY.view());
        itemViewResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.fileView()));
        itemViewResource.addSelectionListener(new ViewListenerImpl());
    }

    private void buildRenameResourceMenuItem() {
        itemRenameResource = new MenuItem();
        itemRenameResource.setText(I18N.DISPLAY.rename());
        itemRenameResource.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderRename()));
        itemRenameResource.addSelectionListener(new RenameListenerImpl());
    }

    private void buildAddfolderMenuItem() {
        itemAddFolder = new MenuItem();
        itemAddFolder.setText(I18N.DISPLAY.newFolder());
        itemAddFolder.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderAdd()));
        itemAddFolder.addSelectionListener(new NewFolderListenerImpl());
    }

    private void registerHandlers() {
        EventBus eventbus = EventBus.getInstance();

        handlers.add(eventbus.addHandler(DiskResourceSelectionChangedEvent.TYPE,
                new DiskResourceSelectionChangedEventHandler() {
                    @Override
                    public void onChange(final DiskResourceSelectionChangedEvent event) {
                        if (event.getTag().equals(tag)) {
                            update(event.getSelected());
                        }
                    }
                }));
    }

    public void cleanup() {
        // unregister
        for (HandlerRegistration reg : handlers) {
            reg.removeHandler();
        }

        // clear our list
        handlers.clear();
    }

    public void update(List<DiskResource> resources) {
        this.resources = resources == null ? Collections.<DiskResource> emptyList() : resources;
        prepareMenuItems(DataUtils.getSupportedActions(this.resources));
    }

    private void prepareMenuItems(final Iterable<Action> actions) {
        for (Component item : getItems()) {
            item.disable();
            item.hide();
        }

        boolean folderActionsEnabled = DataUtils.hasFolders(resources);

        if (folderActionsEnabled && resources.size() == 1) {
            // Enable the "Add Folder" item as well.
            itemAddFolder.enable();
            itemAddFolder.show();
        }

        for (DataUtils.Action action : actions) {
            switch (action) {
                case RenameFolder:
                    itemRenameResource.setIcon(AbstractImagePrototype.create(Resources.ICONS
                            .folderRename()));
                    itemRenameResource.enable();
                    itemRenameResource.show();
                    break;

                case RenameFile:
                    itemRenameResource.setIcon(AbstractImagePrototype.create(Resources.ICONS
                            .fileRename()));
                    itemRenameResource.enable();
                    itemRenameResource.show();
                    break;

                case View:
                    itemViewResource.enable();
                    itemViewResource.show();
                    break;

                case ViewTree:
                    itemViewTree.enable();
                    itemViewTree.show();
                    break;

                case SimpleDownload:
                    itemSimpleDownloadResource.enable();
                    itemSimpleDownloadResource.show();
                    break;

                case BulkDownload:
                    itemBulkDownloadResource.enable();
                    itemBulkDownloadResource.show();
                    break;

                case Delete:
                    ImageResource delIcon = folderActionsEnabled ? Resources.ICONS.folderDelete()
                            : Resources.ICONS.fileDelete();

                    itemDeleteResource.setIcon(AbstractImagePrototype.create(delIcon));
                    itemDeleteResource.enable();
                    itemDeleteResource.show();
                    break;
                case Metadata:
                    itemMetaData.enable();
                    itemMetaData.show();
                    break;
                case Share:
                    itemShareResource.enable();
                    itemShareResource.show();
                    break;
            }
        }
    }

    public void setMaskingParent(ContentPanel maskingParent) {
        this.maskingParent = maskingParent;
    }

    private class NewFolderListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            for (DiskResource resource : resources) {
                if (resource instanceof Folder) {
                    if (resource != null && resource.getId() != null) {
                        IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.newFolder(), 340,
                                new AddFolderDialogPanel(resource.getId(), maskingParent));
                        dlg.disableOkButton();
                        dlg.show();
                    }
                }
            }
        }
    }

    private class RenameListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            for (DiskResource resource : resources) {
                if (DataUtils.isRenamable(resource)) {
                    IPlantDialogPanel panel = null;
                    if (resource instanceof Folder) {
                        panel = new RenameFolderDialogPanel(resource.getId(), resource.getName(),
                                maskingParent);
                    } else if (resource instanceof File) {
                        panel = new RenameFileDialogPanel(resource.getId(), resource.getName(),
                                maskingParent);
                    }

                    IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 340, panel);

                    dlg.show();
                } else {
                    showErrorMsg();
                }
            }
        }
    }

    private class ShareResourceListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            if (DataUtils.isSharable(resources)) {
                SharingDialog sd = new SharingDialog(resources);
                sd.show();
            } else {
                showErrorMsg();
            }
        }

    }

    private class BulkDownloadListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            if (DataUtils.isDownloadable(resources)) {
                IDropLiteWindowDispatcher dispatcher = new IDropLiteWindowDispatcher();
                dispatcher.launchDownloadWindow(resources);
            } else {
                showErrorMsg();
            }
        }
    }

    private class MetadataListenerImpl extends SelectionListener<MenuEvent> {

        @Override
        public void componentSelected(MenuEvent ce) {
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

    private class DeleteListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            final Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();

                    // did the user click yes?
                    if (btn.getItemId().equals(Dialog.YES)) {
                        confirmDelete();
                    }
                }
            };

            // if folders are selected, display a "folder delete" confirmation
            if (DataUtils.hasFolders(resources)) {
                MessageBox.confirm(I18N.DISPLAY.warning(), I18N.DISPLAY.folderDeleteWarning(), callback);
            } else {
                confirmDelete();
            }
        }

        private void confirmDelete() {
            final Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();
                    if (btn.getItemId().equals(Dialog.YES)) {
                        doDelete();
                    }
                }
            };

            MessageBox.confirm(I18N.DISPLAY.deleteFilesTitle(), I18N.DISPLAY.deleteFilesMsg(), callback);
        }

        private void doDelete() {
            // first we need to fill our id lists
            List<String> idFolders = new ArrayList<String>();
            List<String> idFiles = new ArrayList<String>();

            if (DataUtils.isDeletable(resources)) {

                for (DiskResource resource : resources) {
                    if (resource instanceof Folder) {
                        idFolders.add(resource.getId());
                    } else if (resource instanceof File) {
                        idFiles.add(resource.getId());
                    }
                }

                // call the appropriate delete services
                DiskResourceServiceFacade facade = new DiskResourceServiceFacade(maskingParent);

                if (idFiles.size() > 0) {
                    facade.deleteFiles(JsonUtil.buildJsonArrayString(idFiles), new FileDeleteCallback(
                            idFiles));
                }

                if (idFolders.size() > 0) {
                    facade.deleteFolders(JsonUtil.buildJsonArrayString(idFolders),
                            new FolderDeleteCallback(idFolders));
                }
            } else {
                showErrorMsg();
            }
        }
    }

    private class ViewListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {

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

    private class ViewTreeListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
            if (DataUtils.isViewable(resources)) {
                DataContextBuilder builder = new DataContextBuilder();

                TreeViewContextExecutor executor = new TreeViewContextExecutor();

                for (DiskResource resource : resources) {
                    executor.execute(builder.build(resource.getId()));
                }
            } else {
                showErrorMsg();
            }
        }
    }

    private class SimpleDownloadListenerImpl extends SelectionListener<MenuEvent> {
        @Override
        public void componentSelected(MenuEvent ce) {
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
            DiskResourceServiceFacade service = new DiskResourceServiceFacade();
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
}
