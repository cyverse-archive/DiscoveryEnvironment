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
     * Display text for notification category "data".
     * 
     * @return a String representing the text.
     */
    String notificationCategoryData();

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
     * Display text for the word analysis. This could be used in multiple contexts.
     * 
     * @return a String representing the text.
     */
    String analysis();

    /**
     * 
     * Display text for analysis execution status.
     * 
     * @return a String representing the text.
     */
    String submitted();

    /**
     * 
     * Display text for analysis execution status.
     * 
     * @return a String representing the text.
     */
    String unknown();

    /**
     * 
     * Display text for analysis execution status.
     * 
     * @return a String representing the text.
     */
    String running();

    /**
     * 
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String completed();

    /**
     * 
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String removed();

    /**
     * 
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String failed();

    /**
     * 
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String held();

    /**
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String idle();

    /**
     * Display text for analysis execution status
     * 
     * @return a String representing the text.
     */
    String subErr();

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
     * Display text for trees tab caption in file viewer.
     * 
     * @return a String representing the text.
     */
    String trees();

    /**
     * Display text for unseen notifications filter
     * 
     * @return a String representing the text.
     */
    String notificationCategoryUnseen();
}
