package org.iplantc.de.client;

import org.iplantc.de.client.gxt3.presenter.DEPresenter;
import org.iplantc.de.client.gxt3.views.DEView;
import org.iplantc.de.client.gxt3.views.DEView.Presenter;
import org.iplantc.de.client.gxt3.views.DEViewImpl;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;

/**
 * Defines the web application entry point for the system.
 * 
 */
public class DiscoveryEnvironment implements EntryPoint {
    /**
     * Entry point for the application.
     */
    @Override
    public void onModuleLoad() {
        setEntryPointTitle();
        DEView view = new DEViewImpl();
        Presenter p = new DEPresenter(view);
        // HistoryManager mgrHistory = new HistoryManager();
        // String token = History.getToken();
        //
        // if (token == null || token.isEmpty()) {
        //            mgrHistory.handleToken("workspace"); //$NON-NLS-1$
        // } else {
        // mgrHistory.processCommand(token);
        // }
    }

    /**
     * Set the title element of the root page/entry point.
     * 
     * Enables i18n of the root page.
     */
    private void setEntryPointTitle() {
        Window.setTitle(I18N.DISPLAY.rootApplicationTitle());
    }
}
