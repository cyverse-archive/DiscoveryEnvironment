package org.iplantc.de.client.apps.views;

import org.iplantc.core.uiapplications.client.models.CatalogWindowConfig;
import org.iplantc.core.uiapplications.client.presenter.AppsViewPresenter;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.AppsViewImpl;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.views.windows.Gxt3IplantWindow;

import com.google.gwt.json.client.JSONObject;

public class DEAppsWindow extends Gxt3IplantWindow {

    private final AppsView.Presenter presenter;

    public DEAppsWindow(String tag, WindowConfig config) {
        super(tag, config);

        AppsView view = new AppsViewImpl();
        presenter = new AppsViewPresenter(view, (CatalogWindowConfig)config);

        setSize("800", "410");
        presenter.go(this);
        setHeadingText(I18N.DISPLAY.applications());
    }

    @Override
    public JSONObject getWindowState() {
        CatalogWindowConfig configData = new CatalogWindowConfig(config);
        storeWindowViewState(configData);

        if (presenter.getSelectedApp() != null) {
            configData.setAppId(presenter.getSelectedApp().getId());
        }

        if (presenter.getSelectedAppGroup() != null) {
            configData.setCategoryId(presenter.getSelectedAppGroup().getId());
        }

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.deCatalog(),
                configData);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.deCatalog(), ActionType.DISPLAY_WINDOW);
        // return config;
    }

    @Override
    public void setWindowConfig(WindowConfig config) {
    }

    @Override
    public void setMinimized(boolean min) {
    }

    @Override
    public boolean isMinimized() {
        return false;
    }

    @Override
    public int getHeaderOffSetHeight() {
        return 0;
    }

}
