package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AsyncUploadCompleteHandler;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.services.FolderDeleteCallback;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.utils.PanelHelper;
import org.iplantc.de.client.views.dialogs.IPlantSubmittableDialog;
import org.iplantc.de.client.views.panels.DataNavigationPanel.Mode;
import org.iplantc.de.client.views.windows.IDropLiteAppletWindow;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A toolbar for the data navigation panel.
 * 
 * @author sriram
 * 
 */
public class DataNavToolBar extends ToolBar {

    private static final String ID_NEW_FOLDER_BTN = "idNewFolderBtn";
    private static final String ID_RENAME_FOLDER_BTN = "idRenameFolderBtn";
    private static final String ID_DELETE_FOLDER_BTN = "idDeleteFolderBtn";
    @SuppressWarnings("unused")
    private final String tag;
    private String parentFolderId;
    private TreePanelSelectionModel<Folder> selectionModel;
    private Component maskingParent;
    private Button addFolder;
    private Button deleteFolder;
    private Button renameFolder;
    private IPlantSubmittableDialog dlgUpload;

    /**
     * create a new instance of this tool bar
     * 
     * @param tag a tag for this widget
     */
    public DataNavToolBar(final String tag, Mode mode) {
        this.tag = tag;
        if (mode.equals(Mode.EDIT)) {
            add(buildImportButton());
            add(buildUrlImportButton());
        }
        add(buildAddFolderButton());
        add(buildDeleteFolderButton());
        add(buildRenameFolderButton());
    }

    /**
     * set parent folder id
     * 
     * @param id
     */
    public void setParentFolderId(String id) {
        this.parentFolderId = id;
    }

    /**
     * set selection model used by the tree panel
     * 
     * @param selectionModel
     */
    public void setSelectionModel(TreePanelSelectionModel<Folder> selModel) {
        this.selectionModel = selModel;
        if (selectionModel != null) {
            selectionModel.addSelectionChangedListener(new ActionsSelectionListener());
        }
    }

    public void setMaskingParent(Component maskingParent) {
        this.maskingParent = maskingParent;
    }

    private Button buildImportButton() {
        Button ret = PanelHelper.buildButton("idDataImportBtn", null, //$NON-NLS-1$
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        promptUpload();
                    }
                });

        ret.setToolTip("Upload from desktop");
        ret.setIcon(AbstractImagePrototype.create(org.iplantc.de.client.images.Resources.ICONS
                .desktopUpload()));

        return ret;
    }

    private Button buildUrlImportButton() {
        Button ret = PanelHelper.buildButton("idUrlImportBtn", null, //$NON-NLS-1$
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        promptUrlImport();
                    }
                });

        ret.setToolTip("Import from URL");
        ret.setIcon(AbstractImagePrototype.create(org.iplantc.de.client.images.Resources.ICONS
                .urlImport()));

        return ret;

    }

    private void promptUrlImport() {
        String uploadDestId = selectionModel.getSelectedItem().getId();
        String username = UserInfo.getInstance().getUsername();

        // provide key/value pairs for hidden fields
        FastMap<String> hiddenFields = new FastMap<String>();
        hiddenFields.put(FileUploadDialogPanel.HDN_PARENT_ID_KEY, uploadDestId);
        hiddenFields.put(FileUploadDialogPanel.HDN_USER_ID_KEY, username);

        // define a handler for upload completion
        AsyncUploadCompleteHandler handler = new AsyncUploadCompleteHandler(uploadDestId) {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onAfterCompletion() {
                if (dlgUpload != null) {
                    dlgUpload.hide();
                }
            }
        };

        FileUploadDialogPanel pnlUpload = new FileUploadDialogPanel(hiddenFields,
                Constants.CLIENT.fileUploadServlet(), handler, FileUploadDialogPanel.MODE.URL_ONLY);

        dlgUpload = new IPlantSubmittableDialog(I18N.DISPLAY.upload(), 536, pnlUpload);
        dlgUpload.show();
    }

    private void promptUpload() {
        if (selectionModel != null && canUpload(selectionModel.getSelectedItem())) {
            IDropLiteAppletWindow.launchIDropLiteUploadWindow(getCurrentPath(), getCurrentPath());
        }
    }

    private boolean canUpload(Folder destination) {
        if (destination != null && DataUtils.canUploadToThisFolder(destination)) {
            return true;
        } else {
            showErrorMsg();
            return false;
        }
    }

    private String getCurrentPath() {
        if (selectionModel == null) {
            return null;
        } else {
            return selectionModel.getSelectedItem().getId();
        }
    }

    private void showErrorMsg() {
        MessageBox.alert(I18N.DISPLAY.permissionErrorTitle(), I18N.DISPLAY.permissionErrorMessage(),
                null);
    }

    private Button buildDeleteFolderButton() {
        deleteFolder = new Button();
        deleteFolder.setId(ID_DELETE_FOLDER_BTN);
        deleteFolder.setToolTip(I18N.DISPLAY.delete());
        deleteFolder.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderDelete()));
        deleteFolder.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @SuppressWarnings("unchecked")
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectionModel == null) {
                    return;
                }

                @SuppressWarnings("rawtypes")
                List resources = selectionModel.getSelectedItems();
                Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent ce) {
                        // did the user click yes?
                        if (ce.getButtonClicked().getItemId().equals(Dialog.YES)) {
                            List<String> idFolders = new ArrayList<String>();
                            for (Folder resource : selectionModel.getSelectedItems()) {
                                idFolders.add(resource.getId());
                            }

                            if (idFolders.size() > 0) {
                                FolderServiceFacade facade = new FolderServiceFacade(maskingParent);
                                facade.deleteFolders(JsonUtil.buildJsonArrayString(idFolders),
                                        new FolderDeleteCallback(idFolders));
                            }
                        }
                    }
                };

                if (!DataUtils.isDeletable(resources)) {
                    showErrorMsg();
                } else {
                    MessageBox.confirm(I18N.DISPLAY.warning(), I18N.DISPLAY.folderDeleteWarning(),
                            callback);
                }
            }
        });
        return deleteFolder;
    }

    private Button buildRenameFolderButton() {
        renameFolder = new Button();
        renameFolder.setId(ID_RENAME_FOLDER_BTN);
        renameFolder.setToolTip(I18N.DISPLAY.rename());
        renameFolder.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderRename()));
        renameFolder.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectionModel == null) {
                    return;
                }

                Folder resource = selectionModel.getSelectedItem();
                if (!DataUtils.isRenamable(resource)) {
                    showErrorMsg();
                } else {
                    IPlantDialogPanel panel = new RenameFolderDialogPanel(resource.getId(), resource
                            .getName(), maskingParent);
                    IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 340, panel);

                    dlg.show();
                }
            }
        });
        return renameFolder;
    }

    private Button buildAddFolderButton() {
        addFolder = new Button();
        addFolder.setToolTip(I18N.DISPLAY.newFolder());
        addFolder.setId(ID_NEW_FOLDER_BTN);
        addFolder.setIcon(AbstractImagePrototype.create(Resources.ICONS.folderAdd()));
        addFolder.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (selectionModel == null) {
                    return;
                }

                Folder dest = selectionModel.getSelectedItem();

                if (!DataUtils.canCreateFolderInThisFolder(dest)) {
                    showErrorMsg();

                } else {
                    IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.newFolder(), 340,
                            new AddFolderDialogPanel(dest.getId(), maskingParent));
                    dlg.disableOkButton();
                    dlg.show();

                }
            }
        });
        return addFolder;
    }

    private final class ActionsSelectionListener extends SelectionChangedListener<Folder> {

        @Override
        public void selectionChanged(SelectionChangedEvent<Folder> se) {
            // disable all actions by default
            boolean addMenuItemsEnabled = false;
            boolean editMenuItemsEnabled = false;

            if (selectionModel != null) {
                if (parentFolderId != null && !parentFolderId.isEmpty()) {
                    // check the selected folder
                    Folder selectedFolder = selectionModel.getSelectedItem();

                    if (selectedFolder != null) {
                        // we can at least add folders to a selected path under the home folder.
                        addMenuItemsEnabled = true;

                        // disable editing items for the home folder
                        editMenuItemsEnabled = !parentFolderId.equals(selectedFolder.getId());
                    }
                }
            }

            // enable or disable the correct actions
            addFolder.setEnabled(addMenuItemsEnabled);
            renameFolder.setEnabled(editMenuItemsEnabled);
            deleteFolder.setEnabled(editMenuItemsEnabled);

        }

    }
}
