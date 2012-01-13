package org.iplantc.de.client.views;

import org.iplantc.de.client.Constants;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * The view associated with the login screen.
 * 
 * @author Dennis Roberts
 */
public class LoginView implements View {
    /**
     * Redirects the user to the login page.
     */
    @Override
    public void display() {
        Window.Location.assign(Window.Location.getPath() + Constants.CLIENT.loginPage());
    }

    /**
     * Returns the display widget associated with this view; this view has no display widget.
     * 
     * @return null
     */
    @Override
    public Widget getDisplayWidget() {
        return null;
    }
}
