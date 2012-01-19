package org.iplantc.de.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Localized text for wizard widgets.
 * 
 * @author amuir
 * 
 */
public interface DEDisplayStaticText extends Constants {
    /**
     * 
     * Display text for TNRS column header.
     * 
     * @return a String representing the text.
     */
    String submittedName();

    /**
     * 
     * Display text for TNRS column header.
     * 
     * @return a String representing the text.
     */
    String selectedMatch();

    /**
     * 
     * Display text for TNRS column header.
     * 
     * @return a String representing the text.
     */
    String score();

    /**
     * 
     * Display text for TNRS column header.
     * 
     * @return a String representing the text.
     */
    String detailsHeader();

    /**
     * 
     * Display text for TNRS details hyper-link.
     * 
     * @return a String representing the text.
     */
    String detailsLink();

    /**
     * 
     * Display text for notification filter caption.
     * 
     * @return a String representing the text.
     */
    String filterBy();

    /**
     * 
     * Display text for category caption.
     * 
     * @return a String representing the text.
     */
    String category();

    /**
     * 
     * Display text for notification category "all".
     * 
     * @return a String representing the text.
     */
    String notificationCategoryAll();

    /**
     * 
     * Display text for notification category "analysis".
     * 
     * @return a String representing the text.
     */
    String notificationCategoryAnalysis();

    /**
     * 
     * Display text for notification category "system".
     * 
     * @return a String representing the text.
     */
    String notificationCategorySystem();

    /**
     * 
     * Display text when there is one notification.
     * 
     * @return a String representing the text.
     */
    String notificationCountOne();

    /**
     * 
     * Display text when there are multiple notifications.
     * 
     * @return a String representing the text.
     */
    String notificationCountMultiple();

    /**
     * 
     * Display text for the "all notifications" link.
     * 
     * @return a String representing the text.
     */
    String showAllNotifications();

    /**
     * 
     * Display text for the tool tip on the analysis notification icon.
     * 
     * @return a String representing the text.
     */
    String notificationToolTipAnalysis();

    /**
     * 
     * Display text for the word analysis. This could be used in multiple contexts.
     * 
     * @return a String representing the text.
     */
    String analysis();

    /**
     * 
     * Display text for job execution status.
     * 
     * @return a String representing the text.
     */
    String unknown();

    /**
     * 
     * Display text for job execution status.
     * 
     * @return a String representing the text.
     */
    String ready();

    /**
     * 
     * Display text for job execution status.
     * 
     * @return a String representing the text.
     */
    String running();

    /**
     * 
     * Display text for job execution status
     * 
     * @return a String representing the text.
     */
    String completed();

    /**
     * 
     * Display text for job execution status
     * 
     * @return a String representing the text.
     */
    String timeout();

    /**
     * 
     * Display text for job execution status
     * 
     * @return a String representing the text.
     */
    String failed();

    /**
     * 
     * Display text for job execution status
     * 
     * @return a String representing the text.
     */
    String stopped();

    /**
     * Display text for job execution status
     * 
     * @return a String representing the text.
     */
    String idle();

    /**
     * Display text for file selector caption.
     * 
     * @return a String representing the text.
     */
    String selectAFile();

    /**
     * Display text for file selector caption.
     * 
     * @return a String representing the text.
     */
    String selectAFolder();

    /**
     * Display text for the actions panel.
     * 
     * @return a String representing the text.
     */
    String actions();

    /**
     * Display text provenance display panel.
     * 
     * @return a String representing the text.
     */
    String provenance();

    /**
     * Display text for trees tab caption in file viewer.
     * 
     * @return a String representing the text.
     */
    String trees();
}
