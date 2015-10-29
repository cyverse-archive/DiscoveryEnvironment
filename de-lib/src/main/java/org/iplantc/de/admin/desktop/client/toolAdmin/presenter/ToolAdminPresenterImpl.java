package org.iplantc.de.admin.desktop.client.toolAdmin.presenter;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.ErrorHandler;

import java.util.List;

/**
 * Created by aramsey on 10/27/15.
 */


public class ToolAdminPresenterImpl implements ToolAdminView.Presenter, SelectionChangedEvent.SelectionChangedHandler<Tool> {

    private final ToolAdminView view;
    private final ToolAdminServiceFacade toolAdminServiceFacade;

    @Inject
    public ToolAdminPresenterImpl(final ToolAdminView view, ToolAdminServiceFacade toolAdminServiceFacade) {
        this.view = view;
        this.toolAdminServiceFacade = toolAdminServiceFacade;
    }


    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        String searchTerm = "*";
        toolAdminServiceFacade.getTools(searchTerm, new AsyncCallback<List<Tool>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Tool> result) {
                view.setToolList(result);
            }
        });

    }

    @Override
    public void addTool(Tool tool) {

    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Tool> event) {

    }
}
