package org.iplantc.de.client;

import com.google.gwt.core.client.GWT;

/**
 * Provides static access to localized strings.
 * 
 * @author lenards
 * 
 */
public class I18N {
    /** Strings displayed in the UI */
    public static final DEDisplayStrings DISPLAY = (DEDisplayStrings)GWT.create(DEDisplayStrings.class);
    /** Strings displayed in the UI */
    public static final DEDisplayStaticText CONSTANT = (DEDisplayStaticText)GWT
            .create(DEDisplayStaticText.class);
    /** Error messages */
    public static final DEErrorStrings ERROR = (DEErrorStrings)GWT.create(DEErrorStrings.class);
}