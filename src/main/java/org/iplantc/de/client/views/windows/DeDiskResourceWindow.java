package org.iplantc.de.client.views.windows;

import java.util.Set;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.autobeans.Folder;
import org.iplantc.core.uidiskresource.client.presenters.DiskResourcePresenterImpl;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceViewImpl;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.DataWindowConfig;

import com.google.common.collect.Sets;
import com.google.gwt.json.client.JSONObject;
import com.sencha.gxt.data.shared.TreeStore;

public class DeDiskResourceWindow extends Gxt3IplantWindow {

    private final DiskResourceView.Presenter presenter;

    public DeDiskResourceWindow(String tag, DataWindowConfig config) {
        super(tag, config);

        final TreeStore<Folder> treeStore = new TreeStore<Folder>(new DiskResourceModelKeyProvider());
        DiskResourceView view = new DiskResourceViewImpl(treeStore);
        DiskResourceView.Proxy proxy = new FolderRpcProxy();
        presenter = new DiskResourcePresenterImpl(view, proxy, Services.DISK_RESOURCE_SERVICE,
                org.iplantc.core.uidiskresource.client.I18N.DISPLAY);

        setHeadingText(I18N.DISPLAY.data());
        setSize("800", "410");
        presenter.go(this);
        // presenter.setSelectedFolderById("/iplant/home/jstroot/analyses/analysis1-2012-10-15-14-44-02.028/logs");

        if (config != null) {
            if ((config.getFolderId() != null) && !config.getFolderId().isEmpty()) {
                presenter.setSelectedFolderById(config.getFolderId());
            }
            if ((config.getDiskResourceIds().isArray() != null)
                    && (config.getDiskResourceIds().isArray().size() > 0)) {
                Set<String> diskResourceIdList = Sets.newHashSet(JsonUtil.buildStringList(config
                        .getDiskResourceIds()));
                presenter.setSelectedDiskResourcesById(diskResourceIdList);
            }
        }

    }
    @Override
    public JSONObject getWindowState() {
        // Build config data
        DataWindowConfig configData = new DataWindowConfig(config);
        storeWindowViewState(configData);

        if (presenter.getSelectedFolder() != null) {
            configData.setFolderId(presenter.getSelectedFolder().getId());
        }

        configData.setDiskResourceIdsAlt(presenter.getSelectedDiskResources());

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.myDataTag(),
                configData);

        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.myDataTag(), ActionType.DISPLAY_WINDOW);
    }

}
