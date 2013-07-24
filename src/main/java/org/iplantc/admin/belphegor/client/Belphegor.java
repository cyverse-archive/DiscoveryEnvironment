package org.iplantc.admin.belphegor.client;

import java.util.Map;

import org.iplantc.admin.belphegor.client.controllers.ApplicationController;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.admin.belphegor.client.views.ApplicationLayout;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.requests.KeepaliveTimer;
import org.iplantc.de.shared.services.PropertyServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Belphegor implements EntryPoint {
    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        setEntryPointTitle();
        initProperties();
    }

    private void initApp() {
        ApplicationLayout layoutApplication = new ApplicationLayout();

        ApplicationController controller = ApplicationController.getInstance();
        controller.init(layoutApplication);

        RootPanel.get().add(layoutApplication);
        setBrowserContextMenuEnabled(ToolIntegrationAdminProperties.getInstance()
                .isContextClickEnabled());

        String keepaliveTarget = ToolIntegrationAdminProperties.getInstance().getKeepaliveTarget();
        int keepaliveInterval = ToolIntegrationAdminProperties.getInstance().getKeepaliveInterval();
        KeepaliveTimer.getInstance().start(keepaliveTarget, keepaliveInterval);
    }

    private void setEntryPointTitle() {
        Window.setTitle(I18N.DISPLAY.adminApp());
    }

    private void initUserInfo() {
        String address = ToolIntegrationAdminProperties.getInstance().getBootStrapUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper,
                new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.retrieveUserInfoFailed(), caught);

                    }

                    @Override
                    public void onSuccess(String result) {
                        parseWorkspaceInfo(result);
                        initApp();
                    }
                });
    }

    private void parseWorkspaceInfo(String json) {
        // Bootstrap the user-info object with workspace info provided in JSON format.
        UserInfo.getInstance().init(json);
    }

    /**
     * Initializes the Tito configuration properties object.
     */
    private void initProperties() {
        PropertyServiceFacade.getInstance().getProperties(new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.DISPLAY.cantLoadUserInfo(), caught);
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                ToolIntegrationAdminProperties.getInstance().initialize(result);
                initUserInfo();
            }
        });
    }

    /**
     * Disable the context menu of the browser using native JavaScript.
     * 
     * This disables the user's ability to right-click on this widget and get the browser's context menu
     */
    private native void setBrowserContextMenuEnabled(boolean enabled)
    /*-{
		$doc.oncontextmenu = function() {
			return enabled;
		};
    }-*/;

}
