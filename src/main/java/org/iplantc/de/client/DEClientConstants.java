package org.iplantc.de.client;

import org.iplantc.core.uicommons.client.CommonUiConstants;

/**
 * Constants used by client code not visible to the user.
 */
public interface DEClientConstants extends CommonUiConstants {
    /**
     * Interval between status updates.
     * 
     * @return the requested interval.
     */
    int statusInterval();

    /**
     * The Servlet path (relative URL) for file upload.
     * 
     * @return the requested URL.
     */
    String fileUploadServlet();

    /**
     * The Servlet path (relative URL) for file downloads.
     * 
     * @return the requested URL.
     */
    String fileDownloadServlet();

    /**
     * The path to the iPlant logo used in heading of web application.
     * 
     * @return a string representation of the path to iPlant logo
     */
    String iplantLogo();

    /**
     * The path to the iPlant image used in the "About Discovery Environment" window.
     * 
     * @return a string representation of the path to iPlant image used in "About."
     */
    String iplantAboutImage();

    /**
     * The tag used by the window manager to identify the My Data window.
     * 
     * @return a string representing the handle for the My Data window
     */
    String myDataTag();

    /**
     * The tag used by the window manager to identify the Results window.
     * 
     * @return a string representing the handle for the Results window
     */
    String myAnalysisTag();

    /**
     * The tag used by the window manager to identify the My Notifications window.
     * 
     * @return a string representing the handle for My Notifications window
     */
    String myNotifyTag();

    /**
     * The tag use by the window manager to identify the Help - User Manual window.
     * 
     * @return a string representing the handle for the Help - User Manual window
     */
    String myHelpTag();

    /**
     * The tag use by the window manager to identify the About Discovery Environment window.
     * 
     * @return a string representing the handle for the About Discovery Environment window
     */
    String myAboutTag();

    /**
     * The tag used by the window manager to identify the File Editor window.
     * 
     * @return a string representing the handle for the File Editor window
     */
    String fileEditorTag();

    /**
     * The tag used by the window manager to identify the wizard window.
     * 
     * @return a string representing the handle for the wizard window
     */
    String wizardTag();

    /**
     * The default tag prefix used by the window manager when creating a "handle" for a window.
     * 
     * @return a string representing the default prefix used for a window
     */
    String windowTag();

    /**
     * The default tag prefix used to identify "actions", operations performed by the window manager that
     * are not necessarily visible to the user.
     * 
     * @return a string representing the default prefix for an action
     */
    String actionTag();

    /**
     * Defines action for action/tag implementation.
     * 
     * @return a string representing the action.
     */
    String action();

    /**
     * Defines tag for action/tag implementation.
     * 
     * @return a string representing the tag.
     */
    String tag();

    /**
     * The history token for the logout operation.
     * 
     * @return a string representing the history token for logout
     */
    String logoutTag();

    /**
     * Width for desktop shortcut width
     * 
     * @return the shortcut width.
     */
    int shortcutWidth();

    /**
     * Height for desktop shortcut width
     * 
     * @return the shortcut height.
     */
    int shortcutHeight();

    /**
     * The location of the login page.
     * 
     * @return the relative path to the login page.
     */
    String loginPage();

    /**
     * The path to the discovery environment support information page.
     * 
     * @return the relative path to the page.
     */
    String supportPage();

    String releaseNotesPage();

    /**
     * The path to the discovery environment landing page.
     * 
     * @return the path to the landing page.
     */
    String landingPage();

    /**
     * Width of edit box fields which contain string values.
     * 
     * @return a string representing the URL.
     */
    int editBoxWidthString();

    /**
     * Width of edit box fields which contain integer or float values.
     * 
     * @return a string representing the URL.
     */
    int editBoxWidthNumber();

    /**
     * URL to redirect the browser to when the user logs out.
     * 
     * @return a string representing the URL.
     */
    String logoutUrl();

    /**
     * The unique tag used to identify the job execution wizard.
     * 
     * @return the tag.
     */
    String executeJobsTag();

    /**
     * The unique tag used to identify a viewer window.
     * 
     * @return the tag.
     */
    String dataViewerTag();

    /**
     * The path to the NSF logo used in footer of web application.
     * 
     * @return path to logo
     */
    String nsfLogo();

    /**
     * The unique tag to identify file selection dialog
     * 
     * @return
     */
    String fileSelectionDialog();

    /**
     * The path to DE help file
     * 
     * @return path to help file;
     */
    String deHelpFile();

    /**
     * The tag for DE catalog
     * 
     * @return path to help file
     */
    String deCatalog();

    /**
     * The unique tag used to identify a viewer window.
     * 
     * @return the tag.
     */
    String pipelineEditorTag();

    /**
     * The unique tag used to identify the iDrop Lite applet window.
     * 
     * @return the tag.
     */
    String iDropLiteTag();

    /**
     * Applet Main Class
     * 
     * @return Main class name of the iDrop Lite Applet
     */
    String iDropLiteMainClass();

    /**
     * get relative path of Applet jar
     * 
     * @return the relative path
     */
    String iDropLiteArchivePath();

    /**
     * The unique tag used to identify the Tito window.
     * 
     * @return the tag.
     */
    String titoTag();

    /**
     * The unique tag used to identify the Simple Download links window.
     * 
     * @return the tag.
     */
    String simpleDownloadTag();
}
