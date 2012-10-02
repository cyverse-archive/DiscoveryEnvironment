/**
 * 
 */
package org.iplantc.de.client.viewer.commands;

import org.iplantc.de.client.services.callbacks.FileEditorServiceFacade;
import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.de.client.viewer.views.ImageViewerImpl;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;

/**
 * @author sriram
 * 
 */
public class ImageDataViewCommand implements ViewCommand {

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.commands.RPCSuccessCommand#execute(java.lang.String)
     */
    @Override
    public FileViewer execute(FileIdentifier file) {

        FileViewer view = null;

        if (file != null && !file.getFileId().isEmpty()) {
            // we got the url of an image... lets add a tab
            FileEditorServiceFacade fesf = new FileEditorServiceFacade();
            view = new ImageViewerImpl(fesf.getServletDownloadUrl(file.getFileId()));
        }
        return view;

    }

}
