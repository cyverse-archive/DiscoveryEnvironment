package org.iplantc.de.client;

import org.iplantc.core.uicommons.client.CommonUiConstants;

/**
 * Constants used by client code not visible to the user.
 */
public interface DEClientConstants extends CommonUiConstants {
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
     * The tag used by the window manager to identify the My Analysis window.
     * 
     * @return a string representing the handle for the My Analyis window
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
     * URL to redirect the browser to when the user logs out.
     * 
     * @return a string representing the URL.
     */
    String logoutUrl();

    /**
     * The unique tag used to identify a viewer window.
     * 
     * @return the tag.
     */
    String dataViewerTag();

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
