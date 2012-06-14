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

    /**
     * Localized confirmation message to show when a user tries to delete a category.
     * 
     * @param appName the name of the category to be deleted
     * @return string representing the text
     */
    String confirmDeleteCategory(String name);

    /**
     * Localized display text for an "Average User Rating" label.
     * 
     * @return a string representing the localized text.
     */
    String avgUserRating();

    /**
     * Info for admin user
     * 
     * @return a string representing the localized text.
     */
    String adminInfo();

    /**
     * rename category prompt
     * 
     * @return a string representing the localized text.
     */
    String renamePrompt();

    /**
     * add category prompt
     * 
     * @return a string representing the localized text.
     */
    String addCategoryPrompt();

    /**
     * Disable app prompt
     * 
     * @return
     */
    String appDisabled();

    /**
     * Disable app text
     * 
     * @return
     */
    String tempDisable();

    /**
     * Restore app button text
     * 
     * @return
     */
    String restoreApp();

    /**
     * Restore App Msg title
     * 
     * @return
     */
    String restoreAppSucessMsgTitle();

    /**
     * Restore App msg
     * 
     * @return
     */
    String restoreAppSucessMsg(String appName, String restore_categories);

    /**
     * Restore App failure msg title
     * 
     * @return
     */
    String restoreAppFailureMsgTitle();

    /**
     * Restore App failure msg
     * 
     * @return
     */
    String restoreAppFailureMsg(String appName);

    /**
     * Ref. Genome Name
     * 
     * @return
     */
    String refGenName();

    /**
     * Ref. Genome Path
     * 
     * @return
     */
    String refGenPath();

    /**
     * Created by
     * 
     * @return
     */
    String createdBy();

    /**
     * Created On
     * 
     * @return
     */
    String createdOn();

    /**
     * 
     * Last Mod By
     * 
     * @return
     */
    String lastModBy();

    /**
     * Last Mod On
     * 
     * @return
     */
    String lastModOn();

    /**
     * deleted ?
     * 
     * @return
     */
    String deleted();

    /**
     * 
     * @return
     */
    String refDeletePrompt();

    /**
     * 
     * @return
     */
    String referenceGenomes();

    /**
     * 
     * @return
     */
    String addRefGenome();

    /**
     * 
     * @return
     */
    String updateRefGenome();

    /**
     * Text for invalid Ref. genome path
     * 
     * @return
     */
    String invalidPath();
}
