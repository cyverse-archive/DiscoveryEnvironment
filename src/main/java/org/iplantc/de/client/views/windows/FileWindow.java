package org.iplantc.de.client.views.windows;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.events.FileEditorWindowClosedEvent;

import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * Displays the contents of a file.
 */
public abstract class FileWindow extends IPlantWindow {
    /**
     * The identifier of the file shown in the window.
     */
    protected FileIdentifier file;

    /**
     * The manifest of file contents
     */
    protected JSONObject manifest;

    /**
     * Collection of handlers associated with the window.
     */
    protected List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

    /**
     * Instantiate from tag, file identifier, manifest and maximizable flag.
     * 
     * @param tag unique window tag.
     * @param file information about file we are viewing.
     * @param manifest the manifest describing available viewers.
     * @param isMaximizable maximizable flag.
     */
    public FileWindow(String tag, FileIdentifier file, String manifest, boolean isMaximizable) {
        super(tag, true, true, isMaximizable, true);
        this.file = file;

        if (manifest != null) {
            this.manifest = JsonUtil.getObject(manifest);
        }

        init();
    }

    /**
     * Instantiate from tag, file identifier and manifest.
     * 
     * @param tag unique window tag.
     * @param file information about file we are viewing.
     * @param manifest manifest describing available viewers.
     */
    public FileWindow(String tag, FileIdentifier file, String manifest) {
        this(tag, file, manifest, true);
        registerEventHandlers();
    }

    /**
     * Initializes the window.
     */
    protected void init() {
        setHeading(file.getFilename());
        clearPanel();
        constructPanel();
        setSize(640, 438);
        setLayout(new FitLayout());
        initListeners();
    }

    /**
     * Applies a window configuration to the window.
     * 
     * @param config
     */
    @Override
    public void setWindowConfig(WindowConfig config) {
        this.config = config;
    }

    private void initListeners() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                // make sure we are really hiding (and not just minimizing)
                if (getData("minimize") == null) { //$NON-NLS-1$
                    doClose();
                }
            }
        });
    }

    /**
     * Returns an array from the manifest for a given key, or null if no array exists under that key.
     * 
     * @param key
     * @return
     */
    protected JSONValue getItems(String key) {
        return (key != null && manifest != null && manifest.containsKey(key)) ? manifest.get(key) : null;
    }

    private void doClose() {
        EventBus eventbus = EventBus.getInstance();
        FileEditorWindowClosedEvent event = new FileEditorWindowClosedEvent(file.getFileId());
        eventbus.fireEvent(event);
    }

    /**
     * Retrieve id of contained file.
     * 
     * @return a string representing the file identifier.
     */
    public String getFileId() {
        return file.getFileId();
    }

    /**
     * Retrieve the id of contained file's parent.
     * 
     * @return a string representing the file identifier associated with the panel.
     */
    public String getParentId() {
        return file.getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
        super.cleanup();

        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    /**
     * Register handlers with event bus.
     */
    protected void registerEventHandlers() {
        // by default, no event handlers are registered
    }

    protected abstract void clearPanel();

    protected abstract void constructPanel();
}
