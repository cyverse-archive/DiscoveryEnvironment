package org.iplantc.de.client.dispatchers;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.IDropLiteWindowConfig;
import org.iplantc.de.client.utils.IDropLite;

import com.google.gwt.json.client.JSONObject;

/**
 * A WindowDispatcher that can launch an idrop-lite window in upload mode, given upload and refresh
 * paths, or in download mode, given a DiskResource list.
 * 
 * @author psarando
 * 
 */
public class IDropLiteWindowDispatcher extends WindowDispatcher {

    /**
     * Launches an idrop-lite window in upload mode with the given upload and refresh paths.
     * 
     * @param uploadDest
     * @param currentPath
     */
    public void launchUploadWindow(String uploadDest, String currentPath) {
        // Build window config
        IDropLiteWindowConfig config = new IDropLiteWindowConfig();
        config.setDisplayMode(IDropLite.DISPLAY_MODE_UPLOAD);
        config.setUploadDest(uploadDest);
        config.setCurrentPath(currentPath);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.iDropLiteTag(),
                config);

        // Dispatch window display action with this config
        setConfig(windowConfig);
        dispatchAction(Constants.CLIENT.iDropLiteTag());
    }

    /**
     * Launches an idrop-lite window in download mode for the given DiskResource list.
     * 
     * @param resources
     */
    public void launchDownloadWindow(List<DiskResource> resources) {
        // Build window config
        IDropLiteWindowConfig config = new IDropLiteWindowConfig();
        config.setDisplayMode(IDropLite.DISPLAY_MODE_DOWNLOAD);
        config.setDownloadPaths(resources);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.iDropLiteTag(),
                config);

        // Dispatch window display action with this config
        setConfig(windowConfig);
        dispatchAction(Constants.CLIENT.iDropLiteTag());
    }
}
