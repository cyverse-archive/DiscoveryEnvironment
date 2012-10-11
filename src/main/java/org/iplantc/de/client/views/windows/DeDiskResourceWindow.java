package org.iplantc.de.client.views.windows;

import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.presenters.DiskResourcePresenter;
import org.iplantc.core.uidiskresource.client.presenters.proxy.FolderRpcProxy;
import org.iplantc.core.uidiskresource.client.views.DiskResourceView;
import org.iplantc.core.uidiskresource.client.views.DiskResourceViewImpl;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbar;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceViewToolbarImpl;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.DataWindowConfig;

import com.google.gwt.json.client.JSONObject;

public class DeDiskResourceWindow extends Gxt3IplantWindow {

    private final DiskResourceView.Presenter presenter;

    public DeDiskResourceWindow(String tag, WindowConfig config) {
        super(tag, config);

        DiskResourceView view = new DiskResourceViewImpl();
        DiskResourceView.Proxy proxy = new FolderRpcProxy();
        DiskResourceViewToolbar toolbar = new DiskResourceViewToolbarImpl();
        presenter = new DiskResourcePresenter(view, toolbar, proxy);

        setSize("800", "410");
        presenter.go(this);
        setHeadingText(I18N.DISPLAY.data());
    }
    @Override
    public JSONObject getWindowState() {
        // Build config data
        DataWindowConfig configData = new DataWindowConfig(config);
        storeWindowViewState(configData);

        if (presenter.getSelectedFolder() != null && presenter.getSelectedFolder().getId() != null) {
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
