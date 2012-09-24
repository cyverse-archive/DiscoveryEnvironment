package org.iplantc.de.client.desktop.views;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * DE Main view
 * 
 * 
 * @author sriram
 * 
 */
public interface DEView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

    }

    /**
     * set up DE main header logo and menus
     * 
     */
    public void drawHeader();

    /**
     * set up DE desktop view
     * 
     * @param view
     */
    public void replaceCenterPanel(IsWidget view);

    /**
     * Set the presenter for this view
     * 
     * @param presenter
     */
    public void setPresenter(final Presenter presenter);

}
