/**
 * 
 */
package org.iplantc.de.client.viewer.commands;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.services.callbacks.FileEditorServiceFacade;
import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.de.client.util.WindowUtil;

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
        FileEditorServiceFacade fesf = new FileEditorServiceFacade();
        WindowUtil.open(fesf.getServletDownloadUrl(file.getFileId()) + "&attachment=0");
        return null;
    }

}
