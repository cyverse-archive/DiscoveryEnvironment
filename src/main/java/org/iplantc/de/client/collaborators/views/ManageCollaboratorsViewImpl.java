package org.iplantc.de.client.collaborators.views;

import java.util.List;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.collaborators.models.Collaborator;
import org.iplantc.de.client.collaborators.presenter.ManageCollaboratorsPresenter;
import org.iplantc.de.client.collaborators.presenter.ManageCollaboratorsPresenter.MODE;
import org.iplantc.de.client.collaborators.util.CollaboratorsUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class ManageCollaboratorsViewImpl extends Composite implements ManageCollaboratorsView {

    Presenter presenter;

    @UiField(provided = true)
    final ListStore<Collaborator> listStore;

    @UiField(provided = true)
    final ColumnModel<Collaborator> cm;

    @UiField
    Grid<Collaborator> grid;

    @UiField
    TextButton searchBtn;

    @UiField
    TextButton showCollabsBtn;

    @UiField
    TextButton addBtn;

    @UiField
    TextButton deleteBtn;

    @UiField
    TextField searchField;

    @UiField
    FramedPanel collaboratorListPnl;

    private final Widget widget;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private ManageCollaboratorsPresenter.MODE mode;

    private final class SearchFieldKeyPressDownImpl implements KeyDownHandler {
        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == 13) {
                submitSearch(null);
            }
        }
    }

    @UiTemplate("ManageCollaboratorsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ManageCollaboratorsViewImpl> {
    }

    public ManageCollaboratorsViewImpl(CheckBoxSelectionModel<Collaborator> checkBoxModel,
            ColumnModel<Collaborator> cm, final ListStore<Collaborator> store) {
        this.cm = cm;
        this.listStore = store;
        this.mode = MODE.MANAGE;
        widget = uiBinder.createAndBindUi(this);
        grid.setSelectionModel(checkBoxModel);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        grid.getView().setEmptyText(I18N.DISPLAY.noCollaborators());
        init();
    }

    private void init() {
        searchField.addKeyDownHandler(new SearchFieldKeyPressDownImpl());
        searchField.setAutoValidate(true);
        collaboratorListPnl.setHeadingText(I18N.DISPLAY.currentCollaborators());
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Collaborator>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Collaborator> event) {
                if (event.getSelection() != null && event.getSelection().size() > 0) {
                    if (mode.equals(MODE.MANAGE)) {
                        deleteBtn.enable();
                    } else {
                        deleteBtn.disable();
                    }

                    if (mode.equals(MODE.SEARCH)) {
                        addBtn.enable();
                    } else {
                        addBtn.disable();
                    }
                } else {
                    deleteBtn.disable();
                    addBtn.disable();
                }

            }
        });
    }

    @UiHandler("searchBtn")
    void submitSearch(SelectEvent event) {
        String searchTerm = searchField.getCurrentValue();
        searchField.clearInvalid();
        if (searchTerm != null && !searchTerm.isEmpty() && searchTerm.length() > 2) {
            collaboratorListPnl.setHeadingText(I18N.DISPLAY.search() + ": " + searchTerm);
            presenter.searchUsers(searchTerm);
        } else {
            searchField.forceInvalid(I18N.DISPLAY.searchMinChars());

        }
    }

    @UiHandler("addBtn")
    void addCollaborator(SelectEvent event) {
        presenter.addAsCollaborators(grid.getSelectionModel().getSelectedItems());
    }

    @UiHandler("deleteBtn")
    void deleteCollaborator(SelectEvent event) {
        presenter.removeFromCollaborators(grid.getSelectionModel().getSelectedItems());
    }

    @UiHandler("showCollabsBtn")
    void showCurrentCollaborators(SelectEvent event) {
        loadData(CollaboratorsUtil.getCurrentCollaborators());
        setMode(MODE.MANAGE);
        collaboratorListPnl.setHeadingText(I18N.DISPLAY.currentCollaborators());
        showCollabsBtn.setVisible(false);
    }

    @Override
    public void setMode(ManageCollaboratorsPresenter.MODE mode) {
        this.mode = mode;
        if (mode.equals(MODE.MANAGE)) {
            grid.getView().setEmptyText(I18N.DISPLAY.noCollaborators());
            showCollabsBtn.setVisible(false);
        } else if (mode.equals(MODE.SEARCH)) {
            grid.getView().setEmptyText(I18N.DISPLAY.noCollaboratorsSearchResult());
            showCollabsBtn.setVisible(true);
        }
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void loadData(List<Collaborator> models) {
        listStore.clear();
        listStore.addAll(models);
    }

    @Override
    public void mask(String maskText) {
        if (maskText == null || maskText.isEmpty()) {
            collaboratorListPnl.mask(I18N.DISPLAY.loadingMask());
        } else {
            collaboratorListPnl.mask(maskText);
        }
    }

    @Override
    public void unmask() {
        grid.unmask();
    }

    @Override
    public void removeCollaborators(List<Collaborator> models) {
        if (models != null && !models.isEmpty()) {
            for (Collaborator c : models) {
                if (listStore.findModel(c) != null) {
                    listStore.remove(c);
                }
            }
        }
    }

}
