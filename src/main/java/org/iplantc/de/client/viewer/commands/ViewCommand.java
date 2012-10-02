package org.iplantc.de.client.viewer.commands;

import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;

/**
 * Basic interface for command pattern
 * 
 * @author sriram
 * 
 */
public interface ViewCommand {
    /**
     * Execute command.
     */
    FileViewer execute(FileIdentifier file);
}
