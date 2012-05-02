package org.iplantc.de.client.views.panels;

import java.util.Arrays;

import org.iplantc.de.client.I18N;
import org.iplantc.core.uicommons.client.images.Resources;
import org.iplantc.de.client.models.Collaborator;
import org.iplantc.de.client.services.UserSessionServiceFacade;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * 
 * A view class that enables the users to manage collaborators
 * 
 * @author sriram
 * 
 */

public class ManageCollaboratorsPanel extends LayoutContainer {

    private TextField<String> searchTerm;
    private Button search;
    private Status status;

    public static enum VIEW {
        MANAGE, SELECT
    };

    private Grid<Collaborator> grid;

    public ManageCollaboratorsPanel() {
        init();
    }

    private void init() {
        setStyleAttribute("padding-left", "5px");
        initSearch();
        grid = new Grid<Collaborator>(new ListStore<Collaborator>(), buildColumnModel());
        grid.setAutoExpandColumn(Collaborator.NAME);
        grid.setBorders(true);
        grid.getView().setEmptyText(I18N.DISPLAY.noCollaborators());
        grid.setSize(420, 260);
        add(grid);
    }

    private void initSearch() {
        buildSearchField();

        buildSearchButton();
        buildSearchStatus();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        hp.add(searchTerm);
        hp.add(search);
        hp.add(status);
        add(hp);

    }

    private void buildSearchButton() {
        search = new Button();
        search.setIcon(AbstractImagePrototype.create(Resources.ICONS.search()));
        search.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                UserSessionServiceFacade facade = new UserSessionServiceFacade();
                status.setBusy(I18N.DISPLAY.searching());
                facade.searchCollaborators(searchTerm.getValue(), new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        status.clearStatus("");

                    }

                    @Override
                    public void onSuccess(String result) {
                        status.clearStatus("");

                    }
                });

            }
        });
    }

    private void buildSearchField() {
        searchTerm = new TextField<String>();
        searchTerm.setWidth(250);
        searchTerm.setEmptyText(I18N.DISPLAY.collabSearchPrompt());
        searchTerm.setMinLength(3);
    }

    private void buildSearchStatus() {
        status = new Status();
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig name = new ColumnConfig(Collaborator.NAME, I18N.DISPLAY.name(), 150);

        ColumnConfig email = new ColumnConfig(Collaborator.EMAIL, I18N.DISPLAY.email(), 200);

        return new ColumnModel(Arrays.asList(name, email));

    }

}
