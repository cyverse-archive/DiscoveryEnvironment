package org.iplantc.de.client.views.dialogs;

import java.util.ArrayList;

import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.events.ManageDataRefreshEventHandler;
import org.iplantc.de.client.views.panels.FileSelectDialogPanel;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Dialog launched from file selector widget.
 * 
 * @author lenards
 * 
 */
public class FileSelectDialog extends IPlantDialog implements IFileSelectDialog {

    private String tag;
    private ArrayList<HandlerRegistration> handlers;

    /**
     * Instantiate from a tag and caption.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param file the selected file
     * @param currentFolderId id of the folder where the selected file resides
     */
    public FileSelectDialog(String tag, String caption, File file, String currentFolderId) {
        this(tag, caption, 565, file, currentFolderId);
        this.tag = tag;
        setResizable(false);
    }

    /**
     * Instantiate from tag, caption and width.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param file the selected file
     * @param currentFolderId id of the folder where the selected file resides
     */
    public FileSelectDialog(String tag, String caption, int width, File file, String currentFolderId) {
        super(caption, width, new FileSelectDialogPanel(file, currentFolderId, tag));
        this.tag = tag;
        disableOkOnStartup();
        initHandlers();

        addListener(Events.BeforeHide, new Listener<WindowEvent>() {
            @Override
            public void handleEvent(WindowEvent be) {
                cleanup();
                ((FileSelectDialogPanel)getUnderlyingPanel()).cleanup();

                // persist last path
                if ((be.getButtonClicked() != null) && be.getButtonClicked().getItemId().equals("ok")) {

                    UserSettings.getInstance().setDefaultFileSelectorPath(
                            ((FileSelectDialogPanel)getUnderlyingPanel()).getCurrentNavPath());
                }

            }
        });

        setResizable(true);
        setSize(640, 480);
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
     * Set the argument file to be selected in the dialog.
     * 
     * @param file the file to be selected.
     */
    public void select(File file) {
        ((FileSelectDialogPanel)getUnderlyingPanel()).select(file);
    }

    /**
     * Retrieve selected file.
     * 
     * @return file the user has selected.
     */
    @Override
    public File getSelectedFile() {
        return ((FileSelectDialogPanel)getUnderlyingPanel()).getSelectedFile();
    }

    /**
     * Set the argument folder id to which the file belongs to.
     * 
     * @param id the id of the folder where the file is present
     */

    public void setCurrentFolder(String id) {
        ((FileSelectDialogPanel)getUnderlyingPanel()).setCurrentFolderId(id);
    }

    /**
     * Set the argument folder id to which the file belongs to.
     */

    public String getCurrentFolder() {
        return ((FileSelectDialogPanel)getUnderlyingPanel()).getCurrentFolderId();
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
            FileSelectDialogPanel panel = (FileSelectDialogPanel)getUnderlyingPanel();

            panel.seed(UserInfo.getInstance().getUsername(), result, maskingParent, false, null);

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
            FileSelectDialogPanel panel = (FileSelectDialogPanel)getUnderlyingPanel();

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
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

}
