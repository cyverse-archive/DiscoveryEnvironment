/**
 * 
 */
package org.iplantc.de.client.views.dialogs;

import java.util.ArrayList;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.views.panels.FolderSelectDialogPanel;
import org.iplantc.de.client.views.panels.ResourceSelectDialogPanel;
import org.iplantc.de.client.views.panels.SaveAsFolderSelectDialogPanel;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A dialog to get new name for the resource being saved and its destination folder
 * 
 * @author sriram
 * 
 */
public class SaveAsDialog extends IPlantDialog {

    private ArrayList<HandlerRegistration> handlers;
    @SuppressWarnings("unused")
    private final String tag;
    private String defaultFolderId;

    /**
     * Instantiate from a tag and caption.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param folder the selected folder
     * @param currentFolderId id of the folder where the selected folder resides
     */
    public SaveAsDialog(String tag, String caption, Folder folder, String currentFolderId) {
        this(tag, caption, 565, folder, currentFolderId);
        setResizable(false);
    }

    /**
     * Instantiate from tag, caption and width.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param folder the selected folder
     * @param currentFolderId id of the folder where the selected folder resides
     */
    public SaveAsDialog(String tag, String caption, int width, Folder folder, String currentFolderId) {
        super(caption, width, new SaveAsFolderSelectDialogPanel(folder, currentFolderId, tag));
        disableOkOnStartup();
        this.tag = tag;
        initHandlers();
        addListener(Events.BeforeHide, new Listener<WindowEvent>() {
            @Override
            public void handleEvent(WindowEvent be) {
                ((FolderSelectDialogPanel)getUnderlyingPanel()).cleanup();
                cleanup();
            }
        });

        setResizable(false);
        setSize(480, 425);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        layout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        retrieveData(new RetrieveDataCallback(this));
    }

    /**
     * 
     * Get user workspace's files and folder
     * 
     */
    protected void retrieveData(final AsyncCallback<String> callback) {
        mask(I18N.DISPLAY.loadingMask());

        Services.DISK_RESOURCE_SERVICE.getHomeFolder(callback);
    }

    /**
     * Set the argument folder to be selected in the dialog.
     * 
     * @param folder the folder to be selected.
     */
    public void select(Folder folder) {
        ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).select(folder);
    }

    /**
     * Set the argument folder id to be selected in the dialog.
     * 
     * @param id id of the folder to be selected.
     */
    public Folder selectById(String folderId) {
        return ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).selectById(folderId);
    }

    /**
     * Retrieve selected folder.
     * 
     * @return folder the user has selected.
     */
    public Folder getSelectedFolder() {
        return ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).getSelectedFolder();
    }

    /**
     * Set the argument folder id to which the folder belongs to.
     * 
     * @param id the id of the folder where the folder is present
     */

    public void setCurrentFolder(String id) {
        ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).setCurrentFolderId(id);
    }

    /**
     * Set the argument folder id to which the folder belongs to.
     */

    public String getCurrentFolder() {
        return ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).getCurrentFolderId();
    }

    /**
     * 
     * get the new name for the resource
     * 
     * @return
     */
    public String getNewName() {
        return ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).getNewName();
    }

    /**
     * set the new name for the resource
     * 
     * @param newName
     */
    public void setNewName(String newName) {
        ((SaveAsFolderSelectDialogPanel)getUnderlyingPanel()).setNewName(newName);
    }

    private void initHandlers() {
        @SuppressWarnings("unused")
        EventBus eventbus = EventBus.getInstance();

        handlers = new ArrayList<HandlerRegistration>();
    }

    private final class RetrieveDataCallback implements AsyncCallback<String> {
        private final Component maskingParent;

        private RetrieveDataCallback(Component maskingParent) {
            this.maskingParent = maskingParent;
        }

        @Override
        public void onFailure(Throwable caught) {
            unmask();
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
        }

        @Override
        public void onSuccess(String result) {
            SaveAsFolderSelectDialogPanel panel = (SaveAsFolderSelectDialogPanel)getUnderlyingPanel();

            panel.seed(UserInfo.getInstance().getUsername(), result, maskingParent, false,
                    defaultFolderId);

            unmask();
        }
    }

    private void cleanup() {

        ((ResourceSelectDialogPanel)getUnderlyingPanel()).cleanup();

        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }
}
