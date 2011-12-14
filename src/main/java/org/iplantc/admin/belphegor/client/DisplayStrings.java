package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

/**
 * Internationalized strings for Belphegor
 */
public interface DisplayStrings extends CommonUIDisplayStrings {

    /**
     * Display string for admin app name
     * 
     * @return a text representing the admin app name
     */
    String adminApp();

    /**
     * Localized error message to show when information about the current user couldn't be loaded.
     * 
     * @return string representing the text
     */
    String cantLoadUserInfo();

    /**
     * Message provided when a user enters an invalid App Documentation Wiki URL.
     */

    public String notValidAppWikiUrl();

    /*
     * Localized error message to show when an app couldn't be deleted.
     * 
     * @return string representing the text
     */
    String cantDeleteApp();

    /**
     * Localized confirmation message to show when a user tries to delete an app.
     * 
     * @param appName the name of the app to be deleted
     * @return string representing the text
     */
    String confirmDeleteApp(String appName);

    /**
     * Localized title for the "confirm app deletion" message box.
     * 
     * @return string representing the text
     */
    String confirmDeleteAppTitle();

}
