/**
 * 
 */
package org.iplantc.de.client.viewer.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.controllers.DataMonitor;
import org.iplantc.de.client.viewer.commands.ViewCommand;
import org.iplantc.de.client.viewer.factory.MimeTypeViewerResolverFactory;
import org.iplantc.de.client.viewer.model.MimeType;
import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.de.client.viewer.views.FileViewer.Presenter;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

/**
 * @author sriram
 * 
 */
public class FileViewerPresenter implements Presenter, DataMonitor {

    // A presenter can handle more than one view of the same data at a time
    private List<FileViewer> viewers;

    private PlainTabPanel panel;

    /**
     * The identifier of the file shown in the window.
     */
    private FileIdentifier file;

    /**
     * The manifest of file contents
     */
    private JSONObject manifest;

    public FileViewerPresenter(FileIdentifier file, JSONObject manifest) {
        this.manifest = manifest;
        viewers = new ArrayList<FileViewer>();
        this.file = file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.core.uicommons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget
     * )
     */
    @Override
    public void go(HasOneWidget container) {
        panel = (PlainTabPanel)container.getWidget();
        composeView(manifest);
    }

    @Override
    public void composeView(JSONObject manifest) {
        panel.mask(I18N.DISPLAY.loadingMask());
        String mimeType = JsonUtil.getString(manifest, "content-type");
        ViewCommand cmd = MimeTypeViewerResolverFactory.getViewerCommand(MimeType
                .fromTypeString(mimeType));
        FileViewer viewer = cmd.execute(file);
        if (viewer != null) {
            viewers.add(viewer);
            panel.add(viewer.asWidget(), file.getFilename());
            panel.unmask();
        } else {
            // no customers viewers available or data is viewed in browser
            panel.hide();
        }

    }

    @Override
    public void folderCreated(String idParentFolder, JSONObject jsonFolder) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addFile(String path, File info) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileSavedAs(String idOrig, String idParentFolder, File info) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileRename(String id, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void folderRename(String id, String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteResources(List<String> folders, List<String> files) {
        // TODO Auto-generated method stub

    }

    @Override
    public void folderMove(Map<String, String> folders) {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileMove(Map<String, String> files) {
        // TODO Auto-generated method stub

    }

}
