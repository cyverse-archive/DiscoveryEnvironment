package org.iplantc.de.client.controllers;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.commands.data.DataCommand;
import org.iplantc.de.client.commands.data.DiskResourceDeletedCommand;
import org.iplantc.de.client.commands.data.FileImportedCommand;
import org.iplantc.de.client.commands.data.FileMoveCommand;
import org.iplantc.de.client.commands.data.FileRenamedCommand;
import org.iplantc.de.client.commands.data.FileSaveAsCommand;
import org.iplantc.de.client.commands.data.FolderCreatedCommand;
import org.iplantc.de.client.commands.data.FolderMoveCommand;
import org.iplantc.de.client.commands.data.FolderRenamedCommand;

import com.google.gwt.json.client.JSONObject;

/**
 * Generic controller for handling data payload events.
 * 
 * @author amuir
 * 
 */
public class DataController {
    private static DataController instance;

    private DataController() {
    }

    private DataCommand getCommand(String action) {
        DataCommand ret = null; // assume failure

        if (action.equals("file_uploaded")) { //$NON-NLS-1$
            ret = new FileImportedCommand();
        } else if (action.equals("folder_created")) { //$NON-NLS-1$
            ret = new FolderCreatedCommand();
        } else if (action.equals("save_as")) { //$NON-NLS-1$
            ret = new FileSaveAsCommand();
        } else if (action.equals("file_renamed")) { //$NON-NLS-1$
            ret = new FileRenamedCommand();
        } else if (action.equals("folder_renamed")) { //$NON-NLS-1$
            ret = new FolderRenamedCommand();
        } else if (action.equals("file_move")) { //$NON-NLS-1$
            ret = new FileMoveCommand();
        } else if (action.equals("folder_move")) { //$NON-NLS-1$
            ret = new FolderMoveCommand();
        } else if (action.equals("delete")) { //$NON-NLS-1$
            ret = new DiskResourceDeletedCommand();
        }
        return ret;
    }

    /**
     * Retrieve singleton instance.
     * 
     * @return the singleton instance.
     */
    public static DataController getInstance() {
        if (instance == null) {
            instance = new DataController();
        }

        return instance;
    }

    /**
     * Handle an event based on the payload returned by a DataPayloadEvent
     * 
     * @param objPayload payload from a DataPayloadEvent
     */
    public void handleEvent(final DataMonitor monitor, final JSONObject objPayload) {
        if (objPayload != null) {
            String action = JsonUtil.getString(objPayload, "action"); //$NON-NLS-1$

            if (action != null) {
                DataCommand cmd = getCommand(action);

                if (cmd != null) {
                    // drill down into data
                    JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

                    cmd.execute(monitor, objData);
                }
            }
        }
    }
}
