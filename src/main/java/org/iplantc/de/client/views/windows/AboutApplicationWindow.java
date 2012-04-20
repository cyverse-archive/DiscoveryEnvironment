package org.iplantc.de.client.views.windows;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.AboutApplicationData;
import org.iplantc.de.shared.services.AboutApplicationServiceFacade;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * Models a user interface for "about" application information.
 * 
 * @author lenards
 */
public class AboutApplicationWindow extends IPlantWindow {
    private AboutApplicationData model;

    private Label lblNSFStatement;

    /**
     * Constructs an instance given a unique identifier.
     * 
     * @param tag string that serves as an identifier, or window handle.
     */
    public AboutApplicationWindow(String tag) {
        super(tag);

        setId(tag);
        setHeading(I18N.DISPLAY.aboutDiscoveryEnvironment());
        setResizable(false);
        setWidth(302);
        setHeight(250);
        initComponents();
        executeServiceCall();
    }

    private void initComponents() {
        lblNSFStatement = new Label(I18N.DISPLAY.nsfProjectText());
    }

    private String getAboutTemplate() {
        return "<p>Release: {0}</p><p> Build #: {1}</p><p>User Agent: {2}</p>"; //$NON-NLS-1$
    }

    private void executeServiceCall() {
        AboutApplicationServiceFacade.getInstance().getAboutInfo(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                model = new AboutApplicationData(result);
                compose();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    private void compose() {
        Image logo = new Image(Constants.CLIENT.iplantAboutImage());

        final VerticalPanel pnlInfo = new VerticalPanel();
        pnlInfo.setSpacing(7);
        pnlInfo.add(lblNSFStatement);

        VerticalPanel pnlRoot = new VerticalPanel();
        pnlRoot.add(logo);
        pnlRoot.add(pnlInfo);
        pnlRoot.add(buildDetailsContainer());
        add(pnlRoot);
        layout();
    }

    /**
     * Construct and configure the details container.
     * 
     * This is a panel containing details about the Discovery Environment like the release version, build
     * number, and the user's browser information
     * 
     * @return a configured panel containing details information.
     */
    private ContentPanel buildDetailsContainer() {
        ContentPanel pnlDetails = new ContentPanel();
        pnlDetails.setHeaderVisible(false);
        pnlDetails.setBodyStyleName("iplantc-about-pad-text"); //$NON-NLS-1$

        HTML txt = new HTML(Format.substitute(getAboutTemplate(), model.getReleaseVersion(),
                model.getBuildNumber(), model.getUserAgent()));
        pnlDetails.add(txt);

        return pnlDetails;
    }

    @Override
    public JSONObject getWindowState() {
        // Build window config
        JSONObject configData = config;
        if (configData == null) {
            configData = new JSONObject();
        }

        storeWindowViewState(configData);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.myAboutTag(), configData);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);

        return dispatcher.getDispatchJson(Constants.CLIENT.myAboutTag(), ActionType.DISPLAY_WINDOW);
    }
}
