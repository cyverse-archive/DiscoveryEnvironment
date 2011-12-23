package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.CommonUIErrorStrings;

/**
 * Constants used by the client to communicate system errors.
 * 
 * All values present here are subject to translation for internationalization.
 * 
 * @author psarando
 * 
 */
public interface ErrorStrings extends CommonUIErrorStrings {

    /**
     * Error message displayed when adding a Category fails.
     * 
     * @param name The name of the Category the user attempted to add.
     * @return localized error string.
     */
    String addCategoryError(String name);

    /**
     * Error message displayed when renaming a Category fails.
     * 
     * @param name The name of the Category the user attempted to rename.
     * @return localized error string.
     */
    String renameCategoryError(String name);

    /**
     * Error message displayed when moving a Category fails.
     * 
     * @param name The name of the Category the user attempted to move.
     * @return localized error string.
     */
    String moveCategoryError(String name);

    /**
     * Error message displayed when deleting a Category fails.
     * 
     * @param name The name of the Category the user attempted to delete.
     * @return localized error string.
     */
    String deleteCategoryError(String name);

    /**
     * Error message displayed when updating an Application fails.
     * 
     * @return localized error string.
     */
    String updateApplicationError();

    /**
     * Error message displayed when moving an Application fails.
     * 
     * @param name The name of the Application the user attempted to move.
     * @return localized error string.
     */
    String moveApplicationError(String name);

    /**
     * Error message displayed when deleting an Application fails.
     * 
     * @param name The name of the Application the user attempted to delete.
     * @return localized error string.
     */
    String deleteApplicationError(String name);

    /**
     * Error message displayed when adding a Category is not permitted.
     * 
     * @return localized error string.
     */
    String addCategoryPermissionError();
}
