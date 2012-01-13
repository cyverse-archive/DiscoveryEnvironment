package org.iplantc.de.client.views;

import com.google.gwt.user.client.ui.Widget;

/**
 * Define basic functionality for a widget acting as a View.
 */
public interface View {
    /**
     * Retrieve widget to display.
     * 
     * @return display widget.
     */
    public Widget getDisplayWidget();

    /**
     * Display this view.
     */
    public void display();
}
