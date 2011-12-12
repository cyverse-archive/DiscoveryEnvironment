package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

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

}
