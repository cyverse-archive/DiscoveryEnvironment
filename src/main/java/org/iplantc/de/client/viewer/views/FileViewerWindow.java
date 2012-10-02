/**
 * 
 */
package org.iplantc.de.client.viewer.views;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.events.FileEditorWindowClosedEvent;
import org.iplantc.de.client.viewer.presenter.FileViewerPresenter;
import org.iplantc.de.client.viewer.views.FileViewer.Presenter;
import org.iplantc.de.client.views.windows.Gxt3IplantWindow;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;

/**
 * @author sriram
 * 
 */
public class FileViewerWindow extends Gxt3IplantWindow {

    private PlainTabPanel tabPanel;
    protected JSONObject manifest;
    protected FileIdentifier file;

    public FileViewerWindow(String tag, FileIdentifier file, String manifest, boolean isMaximizable) {
        super(tag, true, true, isMaximizable, true);
        init(file, JsonUtil.getObject(manifest));
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

    private void init(FileIdentifier file, JSONObject manifest) {
        setSize("640px", "438px");
        this.manifest = manifest;
        this.file = file;
        tabPanel = new ViewerTabPanel();
        add(tabPanel);
        Presenter p = new FileViewerPresenter(file, manifest);
        p.go(this);
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

    @Override
    public void doHide() {
        super.doHide();
        doClose();
    }

    private void doClose() {
        EventBus eventbus = EventBus.getInstance();
        FileEditorWindowClosedEvent event = new FileEditorWindowClosedEvent(file.getFileId());
        eventbus.fireEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.views.windows.IPlantWindowInterface#getWindowState()
     */
    @Override
    public JSONObject getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Widget getWidget() {
        return tabPanel;
    }

    private class ViewerTabPanel extends PlainTabPanel {

        // we want to hide the whole window
        @Override
        public void hide() {
            FileViewerWindow.this.hide();
        }
    }

}
