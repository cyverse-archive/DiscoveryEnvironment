package org.iplantc.de.theme.base.client.fileViewers;

import com.google.gwt.i18n.client.Messages;

/**
 * @author jstroot
 */
public interface FileViewerStrings extends Messages {

    @Key("addPathsButtonTooltip")
    String addPathsButtonTooltip();

    @Key("deleteSelectedPathsButtonTooltip")
    String deleteSelectedPathsButtonTooltip();

    @Key("initializingFileViewer")
    String initializingFileViewer();

    /**
     * Translated "Select a valid page".
     *
     * @return translated "Select a valid page"
     */
    @DefaultMessage("Select a valid page")
    @Key("invalidPage")
    String invalidPage();

    /**
     * @param totalPages the total number of pages to display
     * @return translated "of " + total pages
     */
    @Key("ofTotalPages")
    String ofTotalPages(int totalPages);

    /**
     * Translated "page size".
     *
     * @return translated "page size"
     */
    @DefaultMessage("Page Size (KB)")
    @Key("pageSize")
    String pageSize();

    @Key("editingStatusText")
    String editingStatusText();

    @Key("notEditingStatusText")
    String notEditingStatusText();

    @Key("lineNumberCheckboxLabel")
    String lineNumberCheckboxLabel();

    @Key("addRowButtonTooltip")
    String addRowButtonTooltip();

    @Key("deleteRowButtonTooltip")
    String deleteRowButtonTooltip();

    @Key("headerRowsLabel")
    String headerRowsLabel();

    @Key("pathListColumnHeaderText")
    String pathListColumnHeaderText();

    @Key("pathListViewName")
    String pathListViewName(String fileName);

    @Key("preventPathListDrop")
    String preventPathListDrop();

    @Key("retrieveFileManifestMask")
    String retrieveFileManifestMask();

    @Key("retrieveTreeUrlsMask")
    String retrieveTreeUrlsMask();

    @Key("retrievingFileContentsMask")
    String retrievingFileContentsMask();

    @Key("skipLinesLabel")
    String skipLinesLabel();

    @Key("previewMarkdownLabel")
    String previewMarkdownLabel();

    /**
     * Translated "Wrap Text".
     *
     * @return translated "Wrap Text"
     */
    @DefaultMessage("Wrap Text")
    @Key("wrap")
    String wrap();

    @Key("sampleColumnText")
    String sampleColumnText(int i);

    @Key("defaultViewName")
    String defaultViewName();

    @Key("viewName")
    String viewName(String fileName);

    @Key("gridToolTip")
    String gridToolTip();

    @Key("fileOpenMsg")
    @DefaultMessage("Your file was opened in separate browser tab / window. You can close this window.")
    String fileOpenMsg();

    /**
     * Translated "Unable to retrieve details.".
     *
     * @return translated "Unable to retrieve details."
     */
    @DefaultMessage("Unable to retrieve details.")
    @Key("retrieveManifestFailed")
    String retrieveManifestFailed();

    /**
     * Translated "Saving...".
     *
     * @return translated "Saving..."
     */
    @DefaultMessage("Saving...")
    @Key("savingMask")
    String savingMask();

    /**
     * Translated "Unable to retrieve contents for file {0}.".
     *
     * @return translated "Unable to retrieve contents for file {0}."
     */
    @DefaultMessage("Unable to retrieve contents for file {0}.")
    @Key("unableToRetrieveFileData")
    String unableToRetrieveFileData(String arg0);

}
