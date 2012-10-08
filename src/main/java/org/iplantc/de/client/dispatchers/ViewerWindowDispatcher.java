/**
 * 
 */
package org.iplantc.de.client.dispatchers;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.ViewerWindowConfig;

import com.google.gwt.json.client.JSONObject;

/**
 * @author sriram
 * 
 */
public class ViewerWindowDispatcher extends WindowDispatcher {

    public void launchViewerWindow(List<String> ids, boolean viewTree) {
        for (String id : ids) {
            ViewerWindowConfig config = new ViewerWindowConfig();
            config.setFileIdentifier(buildFileIdentifier(id));
            config.setShowTreeTab(viewTree);
            WindowConfigFactory configFactory = new WindowConfigFactory();
            JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.dataViewerTag(),
                    config);

            // Dispatch window display action with this config
            setConfig(windowConfig);

            dispatchAction(Constants.CLIENT.dataViewerTag());
        }
    }

    private FileIdentifier buildFileIdentifier(String id) {
        return new FileIdentifier(DiskResourceUtil.parseNameFromPath(id),
                DiskResourceUtil.parseParent(id), id);
    }

}
