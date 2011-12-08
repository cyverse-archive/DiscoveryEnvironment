package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.CommonUiConstants;

public interface BelphegorConstants extends CommonUiConstants {
    /**
     * The path to the iPlant logo used in heading of web application.
     *
     * @return a string representation of the path to iPlant logo
     */
    String iplantLogo();

    /**
     * The location of the login page.
     * 
     * @return the relative path to the login page.
     */
    String loginPage();

    /**
     * The path to the TED landing page.
     * 
     * @return the path to the landing page.
     */
    String landingPage();

    /**
     * URL to redirect the browser to when the user logs out.
     * 
     * @return a string representing the URL.
     */
    String logoutUrl();

    /**
     * URL to submit new tool request
     * 
     * @return a string representing the URL.
     */
    String newToolRequest();
    
    /**
     * URL to help wiki
     * 
     * @return a string representing the URL.
     */
    String titoHelpFile();

    /**
     * URL iplant logo
     * 
     * @return a string representing the URL.
     */
    String iplantAboutImage();

    /**
     * Link from Tito to the DE
     * 
     * @return a string representing the URL.
     */
    String deUrl();
}
