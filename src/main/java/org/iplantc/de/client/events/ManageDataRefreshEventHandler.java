package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

public interface ManageDataRefreshEventHandler extends EventHandler {
    void onRefresh(ManageDataRefreshEvent mdre);
}
