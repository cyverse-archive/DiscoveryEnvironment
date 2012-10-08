/**
 * 
 */
package org.iplantc.de.client.viewer.commands;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.viewer.views.FileViewer;

/**
 * @author sriram
 * 
 */
public class PdfDataViewCommand implements ViewCommand {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.viewer.commands.DataCommand#execute(org.iplantc.core.uidiskresource.client
     * .models.FileIdentifier)
     */
    @Override
    public FileViewer execute(FileIdentifier file) {
        String fileId = file.getFileId();
        if (fileId != null && !fileId.isEmpty()) {
            // // we got the url of the PDF file, so open it in a new window
            WindowUtil
                    .open(Services.FILE_EDITOR_SERVICE.getServletDownloadUrl(fileId) + "&attachment=0");
        }

        return null;
    }
}
