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
public class HtmlDataViewCommand implements ViewCommand {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.viewer.commands.ViewCommand#execute(org.iplantc.core.uidiskresource.client
     * .models.FileIdentifier)
     */
    @Override
    public FileViewer execute(FileIdentifier file) {
        WindowUtil.open(Services.FILE_EDITOR_SERVICE.getServletDownloadUrl(file.getFileId())
                + "&attachment=0");
        return null;
    }

}
