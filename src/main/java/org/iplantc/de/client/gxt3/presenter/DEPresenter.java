package org.iplantc.de.client.gxt3.presenter;

import java.util.Map;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.requests.KeepaliveTimer;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.controllers.TitoController;
import org.iplantc.de.client.gxt3.desktop.widget.Desktop;
import org.iplantc.de.client.gxt3.views.DEView;
import org.iplantc.de.client.utils.MessagePoller;
import org.iplantc.de.shared.services.PropertyServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;
import org.iplantc.de.shared.services.SessionManagementServiceFacade;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Defines the default view of the workspace.
 * 
 * @author sriram
 */
public class DEPresenter implements DEView.Presenter {

    private final DEView view;

    /**
     * Constructs a default instance of the object.
     */
    public DEPresenter(DEView view) {
        this.view = view;
        initializeDEProperties();
        // Initialize TitoController
        TitoController.getInstance();

    }

    private void doWorkspaceDisplay() {
        Desktop widget = new Desktop();
        view.drawHeader();
        view.replaceCenterPanel(widget);
        RootPanel.get().add(view.asWidget());
        initMessagePoller();
    }

    private String parseWorkspaceId(String json) {
        JSONObject obj = JsonUtil.getObject(json);
        // Bootstrap the user-info object with session data provided in JSON
        // format
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.init(obj.toString());
        return userInfo.getWorkspaceId();
    }

    private void initMessagePoller() {
        MessagePoller poller = MessagePoller.getInstance();
        poller.start();
    }

    /**
     * Initializes the session keepalive timer.
     */
    private void initKeepaliveTimer() {
        String target = DEProperties.getInstance().getKeepaliveTarget();
        int interval = DEProperties.getInstance().getKeepaliveInterval();
        if (target != null && !target.equals("") && interval > 0) {
            KeepaliveTimer.getInstance().start(target, interval);
        }
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
                        userInfo.setFirstName(attributes.get(UserInfo.ATTR_FIRSTNAME));
                        userInfo.setLastName(attributes.get(UserInfo.ATTR_LASTNAME));

                        doWorkspaceDisplay();
                    }
                });
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
                initKeepaliveTimer();
            }
        });
    }

    @Override
    public void go(HasOneWidget container) {

    }
}
