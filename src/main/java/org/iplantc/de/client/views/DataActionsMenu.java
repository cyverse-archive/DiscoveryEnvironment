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
import org.iplantc.de.client.services.callbacks.DiskResourceServiceFacade;
import org.iplantc.de.client.services.callbacks.FileDeleteCallback;
import org.iplantc.de.client.services.callbacks.FolderDeleteCallback;
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
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public final class DataActionsMenu extends Menu {
    private static final String MI_ADD_FOLDER_ID = "idDataActionsMenuAddFolder"; //$NON-NLS-1$
    private static final String MI_RENAME_RESOURCE_ID = "idDataActionsMenuRename"; //$NON-NLS-1$
    private static final String MI_VIEW_RESOURCE_ID = "idDataActionsMenuView"; //$NON-NLS-1$
    private static final String MI_VIEW_RAW_ID = "idDataActionsMenuViewRaw"; //$NON-NLS-1$
    private static final String MI_VIEW_TREE_ID = "idDataActionsMenuViewTree"; //$NON-NLS-1$
    private static final String MI_DOWNLOAD_RESOURCE_ID = "idDataActionsMenuDownload"; //$NON-NLS-1$
    private static final String MI_SIMPLE_DOWNLOAD_ID = "idDataActionsMenuSimpleDownload"; //$NON-NLS-1$
    private static final String MI_BULK_DOWNLOAD_ID = "idDataActionsMenuBulkDownload"; //$NON-NLS-1$
    private static final String MI_DELETE_RESOURCE_ID = "idDataActionsMenuDelete"; //$NON-NLS-1$
    private static final String MI_METADATA_ID = "idDataActionsMenuMetadata"; //$NON-NLS-1$
    private static final String MI_SHARE_RESOURCE_ID = "idDataActionsMenuShare"; //$NON-NLS-1$

    private final ArrayList<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

    private final String tag;

    private List<DiskResource> resources;
    private Component maskingParent;

    private MenuItem itemAddFolder;
    private MenuItem itemRenameResource;
    private MenuItem itemViewResource;
    private MenuItem itemViewRawResource;
    private MenuItem itemViewTree;
    private MenuItem itemDownloadResource;
    private MenuItem itemSimpleDownloadResource;
    private MenuItem itemBulkDownloadResource;
    private MenuItem itemDeleteResource;
    private MenuItem itemMetaData;
    private MenuItem itemShareResource;

    public DataActionsMenu(final String tag) {
        this.tag = tag;
        resources = Collections.<DiskResource> emptyList();
        initMenuItems();
        registerHandlers();
    }

    /**
     * Builds the menu items for adding, renaming, viewing, downloading, and deleting disk resources.
     */
    private void initMenuItems() {
        itemAddFolder = buildLeafMenuItem(MI_ADD_FOLDER_ID, I18N.DISPLAY.newFolder(),
                Resources.ICONS.folderAdd(), new NewFolderListenerImpl());
        itemRenameResource = buildLeafMenuItem(MI_RENAME_RESOURCE_ID, I18N.DISPLAY.rename(),
                Resources.ICONS.folderRename(), new RenameListenerImpl());

        itemViewRawResource = buildLeafMenuItem(MI_VIEW_RAW_ID, I18N.DISPLAY.viewRaw(),
                Resources.ICONS.fileView(), new ViewListenerImpl());
        itemViewTree = buildLeafMenuItem(MI_VIEW_TREE_ID, I18N.DISPLAY.viewTreeViewer(),
                Resources.ICONS.fileView(), new ViewTreeListenerImpl());
        itemViewResource = buildMenuMenuItem(MI_VIEW_RESOURCE_ID, I18N.DISPLAY.view(),
                Resources.ICONS.fileView(), itemViewRawResource, itemViewTree);

        itemSimpleDownloadResource = buildLeafMenuItem(MI_SIMPLE_DOWNLOAD_ID,
                I18N.DISPLAY.simpleDownload(),
                Resources.ICONS.download(), new SimpleDownloadListenerImpl());
        itemBulkDownloadResource = buildLeafMenuItem(MI_BULK_DOWNLOAD_ID, I18N.DISPLAY.bulkDownload(),
                Resources.ICONS.download(), new BulkDownloadListenerImpl());
        itemDownloadResource = buildMenuMenuItem(MI_DOWNLOAD_RESOURCE_ID, I18N.DISPLAY.download(),
                Resources.ICONS.download(), itemSimpleDownloadResource, itemBulkDownloadResource);

        itemDeleteResource = buildLeafMenuItem(MI_DELETE_RESOURCE_ID, I18N.DISPLAY.delete(),
                Resources.ICONS.folderDelete(), new DeleteListenerImpl());
        itemMetaData = buildLeafMenuItem(MI_METADATA_ID, I18N.DISPLAY.metadata(),
                Resources.ICONS.metadata(), new MetadataListenerImpl());
        itemShareResource = buildLeafMenuItem(MI_SHARE_RESOURCE_ID, I18N.DISPLAY.share(),
                Resources.ICONS.share(), new ShareResourceListenerImpl());

        add(itemAddFolder);
        add(itemRenameResource);
        add(itemViewResource);
        add(itemDownloadResource);
        add(itemDeleteResource);
        add(itemMetaData);
        // @TODO temp remove sharing action
        // add(itemShareResource);
    }

    private MenuItem buildLeafMenuItem(final String id, final String text,
            final ImageResource iconResource, final SelectionListener<? extends MenuEvent> listener) {
        final MenuItem res = new MenuItem(text, listener);
        res.setId(id);
        res.setIcon(AbstractImagePrototype.create(iconResource));
        return res;
    }

    private MenuItem buildMenuMenuItem(final String id, final String text,
            final ImageResource iconResource, final MenuItem... items) {
        final Menu submenu = new Menu();

        for (MenuItem item : items) {
            submenu.add(item);
        }

        final MenuItem res = new MenuItem(text);
        res.setId(id);
        res.setIcon(AbstractImagePrototype.create(iconResource));
        res.setSubMenu(submenu);
        return res;
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
        hideMenuItems(this);
        hideMenuItems(itemViewResource.getSubMenu());
        hideMenuItems(itemDownloadResource.getSubMenu());

        boolean folderActionsEnabled = DataUtils.hasFolders(resources);

        if (folderActionsEnabled && resources.size() == 1) {
            // Enable the "Add Folder" item as well.
            showMenuItem(itemAddFolder);
        }

        for (DataUtils.Action action : actions) {
            switch (action) {
                case RenameFolder:
                    itemRenameResource.setIcon(AbstractImagePrototype.create(Resources.ICONS
                            .folderRename()));
                    showMenuItem(itemRenameResource);
                    break;

                case RenameFile:
                    itemRenameResource.setIcon(AbstractImagePrototype.create(Resources.ICONS
                            .fileRename()));
                    showMenuItem(itemRenameResource);
                    break;

                case View:
                    showMenuItem(itemViewResource);
                    showMenuItem(itemViewRawResource);
                    break;

                case ViewTree:
                    showMenuItem(itemViewResource);
                    showMenuItem(itemViewTree);
                    break;

                case SimpleDownload:
                    showMenuItem(itemDownloadResource);
                    showMenuItem(itemSimpleDownloadResource);
                    break;

                case BulkDownload:
                    showMenuItem(itemDownloadResource);
                    showMenuItem(itemBulkDownloadResource);
                    break;

                case Delete:
                    ImageResource delIcon = folderActionsEnabled ? Resources.ICONS.folderDelete()
                            : Resources.ICONS.fileDelete();

                    itemDeleteResource.setIcon(AbstractImagePrototype.create(delIcon));
                    showMenuItem(itemDeleteResource);
                    break;
                case Metadata:
                    showMenuItem(itemMetaData);
                    break;
                case Share:
                    showMenuItem(itemShareResource);
                    break;
            }
        }
    }

    private void hideMenuItems(final Menu menu) {
        for (Component item : menu.getItems()) {
            item.disable();
            item.hide();
        }
    }

    private void showMenuItem(final MenuItem item) {
        item.enable();
        item.show();
    }

    public void setMaskingParent(final Component maskingParent) {
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
