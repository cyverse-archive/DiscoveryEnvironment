package org.iplantc.de.client.views.dialogs;

import java.util.ArrayList;

import org.iplantc.core.client.widgets.dialogs.IFolderSelectDialog;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.events.ManageDataRefreshEventHandler;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.views.panels.FolderSelectDialogPanel;
import org.iplantc.de.client.views.panels.ResourceSelectDialogPanel;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Dialog launched from folder selector widget.
 * 
 * @author psarando
 * 
 */
public class FolderSelectDialog extends IPlantDialog implements IFolderSelectDialog {
    private ArrayList<HandlerRegistration> handlers;
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
    public FolderSelectDialog(String tag, String caption, Folder folder, String currentFolderId) {
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
    public FolderSelectDialog(String tag, String caption, int width, Folder folder,
            String currentFolderId) {
        super(caption, width, new FolderSelectDialogPanel(folder, currentFolderId, tag));
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
        setSize(480, 400);
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

        FolderServiceFacade facade = new FolderServiceFacade();
        facade.getHomeFolder(callback);
    }

    /**
     * Set the argument folder to be selected in the dialog.
     * 
     * @param folder the folder to be selected.
     */
    public void select(Folder folder) {
        ((FolderSelectDialogPanel)getUnderlyingPanel()).select(folder);
    }

    /**
     * Set the argument folder id to be selected in the dialog.
     * 
     * @param id id of the folder to be selected.
     */
    public Folder selectById(String folderId) {
        return ((FolderSelectDialogPanel)getUnderlyingPanel()).selectById(folderId);
    }

    /**
     * Retrieve selected folder.
     * 
     * @return folder the user has selected.
     */
    @Override
    public Folder getSelectedFolder() {
        return ((FolderSelectDialogPanel)getUnderlyingPanel()).getSelectedFolder();
    }

    /**
     * Set the argument folder id to which the folder belongs to.
     * 
     * @param id the id of the folder where the folder is present
     */

    public void setCurrentFolder(String id) {
        ((FolderSelectDialogPanel)getUnderlyingPanel()).setCurrentFolderId(id);
    }

    /**
     * Set the argument folder id to which the folder belongs to.
     */

    public String getCurrentFolder() {
        return ((FolderSelectDialogPanel)getUnderlyingPanel()).getCurrentFolderId();
    }

    private void initHandlers() {
        EventBus eventbus = EventBus.getInstance();

        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(eventbus.addHandler(ManageDataRefreshEvent.TYPE,
                new ManageDataRefreshEventHandlerImpl()));
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
            FolderSelectDialogPanel panel = (FolderSelectDialogPanel)getUnderlyingPanel();

            panel.seed(UserInfo.getInstance().getUsername(), result, maskingParent, false,
                    defaultFolderId);

            unmask();
        }
    }

    private final class RefreshRetrieveDataCallback implements AsyncCallback<String> {
        private final Component maskingParent;
        private final String folderId;

        private RefreshRetrieveDataCallback(Component maskingParent, String folderId) {
            this.maskingParent = maskingParent;
            this.folderId = folderId;
        }

        @Override
        public void onFailure(Throwable caught) {
            unmask();
            ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
        }

        @Override
        public void onSuccess(String result) {
            FolderSelectDialogPanel panel = (FolderSelectDialogPanel)getUnderlyingPanel();

            panel.seed(UserInfo.getInstance().getUsername(), result, maskingParent, true, folderId);

            unmask();
        }
    }

    private class ManageDataRefreshEventHandlerImpl implements ManageDataRefreshEventHandler {
        @Override
        public void onRefresh(ManageDataRefreshEvent mdre) {
            doRefresh(mdre);
        }

    }

    private void doRefresh(ManageDataRefreshEvent mdre) {
        if (tag.equals(mdre.getTag())) {
            retrieveData(new RefreshRetrieveDataCallback(this, mdre.getCurrentFolderId()));
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

    /**
     * @param defaultFolderId the defaultFolderId to set
     */
    public void setDefaultFolderId(String defaultFolderId) {
        this.defaultFolderId = defaultFolderId;
    }

    /**
     * @return the defaultFolderId
     */
    public String getDefaultFolderId() {
        return defaultFolderId;
    }
}
