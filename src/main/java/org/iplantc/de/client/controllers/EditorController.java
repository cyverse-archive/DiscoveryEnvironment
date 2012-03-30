package org.iplantc.de.client.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.events.FileEditorWindowClosedEvent;
import org.iplantc.de.client.events.FileEditorWindowClosedEventHandler;
import org.iplantc.de.client.events.WindowPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEventHandler;
import org.iplantc.de.client.services.DiskResourceServiceCallback;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.utils.DEWindowManager;
import org.iplantc.de.client.views.windows.FileEditorWindow;
import org.iplantc.de.client.views.windows.FileWindow;
import org.iplantc.de.client.views.windows.IPlantWindow;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * Defines a controller for editor windows.
 */
public class EditorController implements DataMonitor {
    private final DEWindowManager mgrWindow;
    private final List<FileWindow> fileWindows = new ArrayList<FileWindow>();

    /**
     * Instantiate from WindowManager.
     * 
     * @param mgrWindow manager for window objects.
     */
    public EditorController(DEWindowManager mgrWindow) {
        this.mgrWindow = mgrWindow;

        initEventListeners();
    }

    private void initEventListeners() {
        EventBus eventbus = EventBus.getInstance();

        // handle window close
        eventbus.addHandler(FileEditorWindowClosedEvent.TYPE, new FileEditorWindowClosedEventHandler() {
            @Override
            public void onClosed(FileEditorWindowClosedEvent event) {
                removeFileWindow(event.getId());
            }
        });

        eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandler() {
            @Override
            public void onFire(DataPayloadEvent event) {
                DataController controller = DataController.getInstance();
                controller.handleEvent(EditorController.this, event.getPayload());
            }
        });

        // window payload events
        eventbus.addHandler(WindowPayloadEvent.TYPE, new WindowPayloadEventHandler() {
            private boolean payloadContainsAction(String action, final JSONObject payload) {
                return JsonUtil.getString(payload, "action").equals(action); //$NON-NLS-1$
            }

            private boolean isViewerPayload(final JSONObject objPayload) {
                return payloadContainsAction("display_viewer", objPayload); //$NON-NLS-1$
            }

            private boolean isTreeViewerPayload(final JSONObject objPayload) {
                return payloadContainsAction("display_viewer_add_treetab", objPayload); //$NON-NLS-1$
            }

            @Override
            public void onFire(WindowPayloadEvent event) {
                JSONObject objPayload = event.getPayload();

                if (isViewerPayload(objPayload)) {
                    addFileWindows(objPayload, false);
                } else if (isTreeViewerPayload(objPayload)) {
                    addFileWindows(objPayload, true);
                }
            }
        });
    }

    private FileIdentifier buildFileIdentifier(final JSONObject objData) {
        FileIdentifier ret = null; // assume failure

        if (objData != null) {
            String id = JsonUtil.getString(objData, "id"); //$NON-NLS-1$
            String name = JsonUtil.getString(objData, "name"); //$NON-NLS-1$
            String idParent = JsonUtil.getString(objData, "idParent"); //$NON-NLS-1$

            ret = new FileIdentifier(name, idParent, id);
        }

        return ret;
    }

    private void addFileWindows(final JSONObject objPayload, boolean addTreeTab) {
        if (objPayload != null) {
            JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

            if (objData != null) {
                JSONValue valFiles = objData.get("files"); //$NON-NLS-1$

                if (!JsonUtil.isEmpty(valFiles)) {
                    String tag;
                    JSONArray files = valFiles.isArray();

                    // loop through our sub-folders and recursively add them
                    for (int i = 0,len = files.size(); i < len; i++) {
                        JSONObject file = JsonUtil.getObjectAt(files, i);

                        FileIdentifier identifier = buildFileIdentifier(file);

                        if (identifier != null) {
                            tag = Constants.CLIENT.fileEditorTag() + identifier.getFileId();

                            addFileWindow(tag, identifier, addTreeTab);
                        }
                    }
                }
            }
        }
    }

    private void removeFileWindow(FileWindow in) {
        if (in != null) {
            // since minimized is really hidden... we need to check
            // or else the hide event is never picked up the result
            // is that the user can delete a 'minimized' provenance
            // window and it will not be removed from the toolbar.
            // The solution is to show the window first and then
            // hide. If this causes visual anomalies on slower
            // machines, we will likely have to fire an event to
            // have the perspective remove the item from the toolbar.
            if (in.getData("minimize") != null) { //$NON-NLS-1$
                in.show();
            }

            // make sure our window cleans up after itself
            in.hide();

            // remove from our list for file portlets
            fileWindows.remove(in);

            mgrWindow.remove(in.getTag());
        }
    }

    private void removeFileWindow(String idFile) {
        if (idFile != null) {
            List<FileWindow> remove = new ArrayList<FileWindow>();

            // fill our delete list
            for (FileWindow window : fileWindows) {
                if (window.getFileId().equals(idFile)) {
                    remove.add(window);
                }
            }

            // perform remove
            for (FileWindow window : remove) {
                removeFileWindow(window);
            }
        }
    }

    private void removeFileWindowAfterParentDelete(String idParent) {
        if (idParent != null) {
            List<FileWindow> remove = new ArrayList<FileWindow>();

            // fill our delete list
            for (FileWindow window : fileWindows) {
                if (window.getParentId().equals(idParent)) {
                    remove.add(window);
                }
            }

            // perform remove
            for (FileWindow window : remove) {
                removeFileWindow(window);
            }
        }
    }

    private void buildNewWindow(final String tag, final FileIdentifier file, final String json,
            boolean addTreeTab) {
        if (json != null) {
            FileWindow window;
            FileEditorWindow editorWindow = new FileEditorWindow(tag, file, json);

            if (addTreeTab) {
                editorWindow.loadTreeTab();
            }

            window = editorWindow;

            fileWindows.add(window);
            mgrWindow.add(window);
            mgrWindow.show(tag);
        }
    }

    private void retrieveFileManifest(final String tag, final FileIdentifier file,
            final boolean addTreeTab) {
        FileEditorServiceFacade facade = new FileEditorServiceFacade();

        facade.getManifest(file.getFileId(), new DiskResourceServiceCallback() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    buildNewWindow(tag, file, result, addTreeTab);
                } else {
                    onFailure(null);
                }
            }

            @Override
            protected String getErrorMessageDefault() {
                return I18N.ERROR.unableToRetrieveFileManifest(file.getFilename());
            }

            @Override
            protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
                return getErrorMessageForFiles(code, file.getFilename());
            }
        });
    }

    private void addFileWindow(final String tag, final FileIdentifier file, boolean addTreeTab) {
        IPlantWindow window = mgrWindow.getWindow(tag);

        // do we already have a window for this file... let's bring it to the front
        if (window != null) {
            window.show();
            window.toFront();
            window.refresh();
        } else {
            retrieveFileManifest(tag, file, addTreeTab);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFile(String idParentFolder, File info) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteResources(List<String> folders, List<String> files) {
        for (String id : files) {
            removeFileWindow(id);
        }

        for (String id : folders) {
            removeFileWindowAfterParentDelete(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileRename(String id, String name) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileSavedAs(String idOrig, String idParent, File info) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void folderCreated(String idParentFolder, JSONObject jsonFolder) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void folderRename(String id, String name) {
        // intentionally do nothing
    }

    @Override
    public void fileMove(Map<String, String> files) {
        // intentionally do nothing... for now
    }

    @Override
    public void folderMove(Map<String, String> folders) {
        // intentionally do nothing
    }
}
