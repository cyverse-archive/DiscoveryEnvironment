package org.iplantc.de.client.viewer.commands;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.viewer.views.FileViewer;

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
