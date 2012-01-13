package org.iplantc.de.client.commands;

/**
 * Basic interface for command pattern (when an RPC call returns success).
 * 
 * @author amuir
 * 
 */
public interface RPCSuccessCommand {
    /**
     * Execute command.
     */
    void execute(String result);
}
