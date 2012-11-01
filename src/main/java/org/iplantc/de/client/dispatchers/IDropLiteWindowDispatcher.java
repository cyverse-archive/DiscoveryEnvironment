package org.iplantc.de.client.dispatchers;

import java.util.Collection;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.idroplite.util.IDropLiteUtil;
import org.iplantc.de.client.models.IDropLiteWindowConfig;

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
        IDropLiteWindowConfig configData = new IDropLiteWindowConfig();
        configData.setDisplayMode(IDropLiteUtil.DISPLAY_MODE_UPLOAD);
        configData.setUploadDest(uploadDest);
        configData.setCurrentPath(currentPath);
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.iDropLiteTag(),
                configData);

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
        IDropLiteWindowConfig configData = new IDropLiteWindowConfig();
        configData.setDisplayMode(IDropLiteUtil.DISPLAY_MODE_DOWNLOAD);
        configData.setDownloadPaths(resources);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.iDropLiteTag(),
                configData);

        // Dispatch window display action with this config
        setConfig(windowConfig);
        dispatchAction(Constants.CLIENT.iDropLiteTag());
    }

    public void launchDownloadWindow(
            Collection<org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource> resources) {
        // Build window config
        IDropLiteWindowConfig configData = new IDropLiteWindowConfig();
        configData.setDisplayMode(IDropLiteUtil.DISPLAY_MODE_DOWNLOAD);
        configData.setDownloadPaths(resources);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.iDropLiteTag(),
                configData);

        // Dispatch window display action with this config
        setConfig(windowConfig);
        dispatchAction(Constants.CLIENT.iDropLiteTag());

    }
}
