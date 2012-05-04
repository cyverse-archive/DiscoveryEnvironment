package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.Collaborator;
import org.iplantc.de.client.models.JsCollaborators;
import org.iplantc.de.client.services.UserSessionServiceFacade;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;

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
    private Button showList;

    private MODE mode;
    // list that holds user's collaborators
    private List<Collaborator> my_collaborators;

    public static enum MODE {
        MANAGE, SELECT, SEARCH
    };

    private Grid<Collaborator> grid;

    public ManageCollaboratorsPanel() {
        my_collaborators = new ArrayList<Collaborator>();
        init();
    }

    private void init() {
        initSearch();
        grid = new Grid<Collaborator>(new ListStore<Collaborator>(), buildColumnModel());
        grid.setAutoExpandColumn(Collaborator.NAME);
        grid.setBorders(true);
        grid.getView().setEmptyText(I18N.DISPLAY.noCollaborators());
        grid.setSize(435, 270);
        add(grid);
        getCurrentCollaborators();
    }

    private void initSearch() {
        buildSearchField();

        buildSearchButton();
        buildShowListButton();
        buildSearchStatus();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setBorders(true);
        hp.setWidth(440);
        hp.setSpacing(18);
        hp.add(searchTerm);
        hp.add(search);
        hp.add(showList);
        hp.add(status);
        add(hp);

    }

    private void buildShowListButton() {
        showList = new Button();
        showList.setIcon(AbstractImagePrototype.create(Resources.ICONS.viewCurrentCollabs()));
        showList.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                showList.setVisible(false);
                grid.getStore().removeAll();
                grid.getStore().add(my_collaborators);
            }
        });
        showList.setVisible(false);
        showList.setToolTip(I18N.DISPLAY.currentCollabList());
    }

    private void buildSearchButton() {
        search = new Button();
        search.setIcon(AbstractImagePrototype.create(Resources.ICONS.search()));
        search.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                doSearch();

            }
        });
        search.setToolTip(I18N.DISPLAY.search());
    }

    private void buildSearchField() {
        searchTerm = new TextField<String>() {
            @Override
            public void onKeyUp(FieldEvent fe) {
                if (fe.getKeyCode() == 13) {
                    doSearch();
                }
            }
        };
        searchTerm.setWidth(250);
        searchTerm.setEmptyText(I18N.DISPLAY.collabSearchPrompt());
        searchTerm.setMinLength(3);
    }

    private void buildSearchStatus() {
        status = new Status();
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig name = new ColumnConfig(Collaborator.NAME, I18N.DISPLAY.name(), 150);
        name.setRenderer(new NameCellRenderer());

        ColumnConfig email = new ColumnConfig(Collaborator.EMAIL, I18N.DISPLAY.email(), 200);

        return new ColumnModel(Arrays.asList(name, email));

    }

    private void loadResults(List<Collaborator> collaborators) {
        // clear results before adding. Sort alphabetically
        ListStore<Collaborator> store = grid.getStore();
        store.removeAll();
        store.add(collaborators);
        store.sort(Collaborator.NAME, SortDir.ASC);
    }

    private List<Collaborator> parseResults(String result) {
        JSONObject obj = JSONParser.parseStrict(result).isObject();
        String json = obj.get("users").toString();
        JsArray<JsCollaborators> collabs = JsonUtil.asArrayOf(json);
        List<Collaborator> collaborators = new ArrayList<Collaborator>();
        for (int i = 0; i < collabs.length(); i++) {
            Collaborator c = new Collaborator(collabs.get(i));
            collaborators.add(c);
        }
        return collaborators;
    }

    public void saveData() {
        // TODO Auto-generated method stub

    }

    private void doSearch() {
        String search = searchTerm.getValue();

        if (search == null || search.isEmpty() || search.length() < 3) {
            searchTerm.markInvalid(I18N.DISPLAY.collabSearchPrompt());
            return;
        }
        status.setBusy("");
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.searchCollaborators(search, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                status.clearStatus("");
                showList.setVisible(true);
            }

            @Override
            public void onSuccess(String result) {
                mode = MODE.SEARCH;
                List<Collaborator> collaborators = parseResults(result);
                loadResults(collaborators);
                status.clearStatus("");
                showList.setVisible(true);
            }
        });
    }

    private void getCurrentCollaborators() {
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        status.setBusy("");
        facade.getCollaborators(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                status.clearStatus("");
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                mode = MODE.MANAGE;
                my_collaborators = parseResults(result);
                loadResults(my_collaborators);
                status.clearStatus("");
            }

        });
    }

    /**
     * A custom renderer that renders with add / delete icon
     * 
     * @author sriram
     * 
     */
    private class NameCellRenderer implements GridCellRenderer<Collaborator> {

        @Override
        public Object render(Collaborator model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Collaborator> store, Grid<Collaborator> grid) {

            final HorizontalPanel hp = new HorizontalPanel();
            final IconButton addBtn = new IconButton("add_button",
                    new SelectionListener<IconButtonEvent>() {

                        @Override
                        public void componentSelected(IconButtonEvent ce) {
                        }
                    });
            final IconButton removeBtn = new IconButton("remove_button",
                    new SelectionListener<IconButtonEvent>() {

                        @Override
                        public void componentSelected(IconButtonEvent ce) {
                        }
                    });
            if (mode.equals(MODE.SEARCH)) {
                hp.add(addBtn);
            } else {
                hp.add(removeBtn);
            }
            hp.add(new Label(model.getName()));
            hp.setSpacing(3);
            return hp;
        }

    }
}
