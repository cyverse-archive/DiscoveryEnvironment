package org.iplantc.de.admin.desktop.client.toolAdmin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolContainer;

import java.util.List;

/**
 * Created by aramsey on 10/28/15.
 */
public interface ToolAdminServiceFacade {

    void getTools(String searchTerm, AsyncCallback<List<Tool>> callback);

    void getToolDetails(Tool tool, AsyncCallback<ToolContainer> callback);

    void addTool(Tool tool, AsyncCallback<Tool> callback);

    void updateTool(Tool tool, AsyncCallback<Tool> callback);

}
