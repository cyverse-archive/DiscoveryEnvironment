package org.iplantc.de.client.commands;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Command for dispatching events
 * 
 * @author amuir
 * 
 */
public interface EventDispatchCommand {
    void execute(GwtEvent<?> event);
}
