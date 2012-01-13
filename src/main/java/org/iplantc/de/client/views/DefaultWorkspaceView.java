package org.iplantc.de.client.views;

import java.util.Map;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.ApplicationLayout;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.models.BasicWindowConfig;
import org.iplantc.de.client.models.CatalogWindowConfig;
import org.iplantc.de.client.models.DEProperties;
import org.iplantc.de.client.services.DEServiceFacade;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.views.windows.DECatalogWindow;
import org.iplantc.de.shared.services.PropertyServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;
import org.iplantc.de.shared.services.SessionManagementServiceFacade;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Defines the default view of the workspace.
 */
public class DefaultWorkspaceView implements View {
    private final ApplicationLayout layout;

    /**
     * Constructs a default instance of the object.
     */
    public DefaultWorkspaceView() {
        layout = new ApplicationLayout();
    }

    private void doWorkspaceDisplay() {
        DesktopView view = new DesktopView();

        // show application if an appID is specified in the request string
        String appId = getAppId();
        if (appId != null) {
            JSONObject json = new JSONObject();
            json.put("appID", new JSONString(appId)); //$NON-NLS-1$
            BasicWindowConfig config = new CatalogWindowConfig(json);
            new DECatalogWindow(Constants.CLIENT.deCatalog(), config).show();
        }

        layout.replaceCenterPanel(view);
    }

    /**
     * Returns the application ID from the request string
     * 
     * @return a string containing a application ID, or null if the request does not contain one
     */
    private String getAppId() {
        String paramName = Constants.CLIENT.appIdParam();
        return Location.getParameter(paramName);
    }

    private String parseWorkspaceId(String json) {
        JSONObject obj = JsonUtil.getObject(json);
        // Bootstrap the user-info object with session data provided in JSON
        // format
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.init(obj.toString());
        return userInfo.getWorkspaceId();
    }

    private void initNotificationManager() {
        NotificationManager mgrNotification = NotificationManager.getInstance();
        mgrNotification.init();
    }

    /**
     * Initializes the username and email for a user.
     * 
     * Calls the session management service to get the attributes associated with a user.
     */
    private void initializeUserInfoAttributes() {
        SessionManagementServiceFacade.getInstance().getAttributes(
                new AsyncCallback<Map<String, String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.retrieveUserInfoFailed(), caught);
                    }

                    @Override
                    public void onSuccess(Map<String, String> attributes) {
                        UserInfo userInfo = UserInfo.getInstance();
                        userInfo.setEmail(attributes.get(UserInfo.ATTR_EMAIL));
                        userInfo.setUsername(attributes.get(UserInfo.ATTR_UID));
                        userInfo.setFullUsername(attributes.get(UserInfo.ATTR_USERNAME));
                        displayView();
                    }
                });
    }

    private void displayView() {
        layout.assembleLayout();
        doWorkspaceDisplay();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget getDisplayWidget() {
        return layout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void display() {
        initializeDEProperties();
    }

    /**
     * Initializes the discovery environment configuration properties object.
     */
    private void initializeDEProperties() {
        PropertyServiceFacade.getInstance().getProperties(new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.systemInitializationError(), caught);
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                DEProperties.getInstance().initialize(result);
                getUserInfo();
                setBrowserContextMenuEnabled(DEProperties.getInstance().isContextClickEnabled());
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

    /**
     * Retrieves the user information from the server.
     */
    private void getUserInfo() {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "bootstrap"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        DEServiceFacade.getInstance().getServiceData(wrapper, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveUserInfoFailed(), caught);
            }

            @Override
            public void onSuccess(String result) {
                parseWorkspaceId(result);
                initializeUserInfoAttributes();
                initNotificationManager();
            }
        });
    }
}
