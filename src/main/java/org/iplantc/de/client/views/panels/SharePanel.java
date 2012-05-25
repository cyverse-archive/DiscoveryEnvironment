package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.Collaborator;
import org.iplantc.de.client.models.Sharing;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;

/**
 * A panel to display a list of sharee and their permissions
 * 
 * @author sriram
 * 
 */
public class SharePanel extends ContentPanel {
    private static final String ID_PERM_GROUP = "idPermGroup";
    private DiskResource resource;
    private Grid<Sharing> grid;
    private List<Sharing> unshareList;
    private CheckBoxSelectionModel<Sharing> sm;
    private ToolBar toolbar;
    private static final String ID_PERM_PANEL = "idPermPanel";
    private static final String ID_BTN_REMOVE = "idBtnRemove";

    public SharePanel(DiskResource dr) {
        unshareList = new ArrayList<Sharing>();
        this.setResource(dr);
        init();
    }

    private void init() {
        setSize(385, 225);
        setLayout(new FitLayout());
        ColumnModel cm = buildColumnModel();
        ListStore<Sharing> store = new ListStore<Sharing>();
        grid = new Grid<Sharing>(store, cm);
        store.setKeyProvider(new ModelKeyProvider<Sharing>() {
            @Override
            public String getKey(Sharing model) {
                return model.getUserName();
            }
        });
        grid.getView().setEmptyText(org.iplantc.de.client.I18N.DISPLAY.sharePanelEmptyText());
        grid.setSelectionModel(sm);
        grid.addPlugin(sm);
        grid.getSelectionModel().addListener(Events.SelectionChange, new GridSelectionChangeListener());
        add(grid);
        addToolBar();
        new SharingGridDropTarget(grid);
    }

    private void addToolBar() {
        toolbar = new ToolBar();
        Button removeBtn = buildUnshareButton();
        toolbar.add(removeBtn);
        toolbar.add(new FillToolItem());

        HorizontalPanel panel = new HorizontalPanel();
        panel.setId(ID_PERM_PANEL);

        final Radio read = buildRadio(org.iplantc.de.client.I18N.DISPLAY.read());
        read.addListener(Events.OnChange, new BulkPermissionsFieldListener());

        final Radio write = buildRadio(org.iplantc.de.client.I18N.DISPLAY.write());
        write.addListener(Events.OnChange, new BulkPermissionsFieldListener());

        final Radio own = buildRadio(org.iplantc.de.client.I18N.DISPLAY.own());
        own.addListener(Events.OnChange, new BulkPermissionsFieldListener());

        RadioGroup group = new RadioGroup();
        group.setId(ID_PERM_GROUP);
        group.add(read);
        group.add(write);
        group.add(own);

        panel.add(group);
        panel.disable();
        toolbar.add(panel);
        setTopComponent(toolbar);
    }

    private Button buildUnshareButton() {
        Button removeBtn = new Button(org.iplantc.de.client.I18N.DISPLAY.unshare(),
                AbstractImagePrototype.create(Resources.ICONS.deleteIcon()));
        removeBtn.setId(ID_BTN_REMOVE);
        removeBtn.addSelectionListener(new RemoveButtonSelectionListener());
        removeBtn.disable();
        return removeBtn;

    }

    public void setSharingInfo(List<Sharing> sharingInfoList) {
        grid.getStore().add(sharingInfoList);
    }

    /**
     * @return the unshareList
     */
    public List<Sharing> getUnshareList() {
        return unshareList;
    }

    private ColumnModel buildColumnModel() {
        sm = new CheckBoxSelectionModel<Sharing>();
        ColumnConfig sharee = new ColumnConfig(Sharing.NAME, I18N.DISPLAY.name(), 170);
        ColumnConfig premissions = new ColumnConfig("",
                org.iplantc.de.client.I18N.DISPLAY.permissions(), 170);
        premissions.setRenderer(new PermissionsReadCellRender());
        sharee.setMenuDisabled(true);
        sharee.setSortable(true);
        sharee.setRenderer(new NameCellRenderer());

        return new ColumnModel(Arrays.asList(sm.getColumn(), sharee, premissions));
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(DiskResource resource) {
        this.resource = resource;
    }

    /**
     * @return the resource
     */
    public DiskResource getResource() {
        return resource;
    }

    public List<Sharing> getSharingList() {
        return grid.getStore().getModels();
    }

    private final class GridSelectionChangeListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            if (grid.getSelectionModel().getSelectedItems().size() > 0) {
                toolbar.getItemByItemId(ID_BTN_REMOVE).enable();
                HorizontalPanel hp = (HorizontalPanel)toolbar.getItemByItemId(ID_PERM_PANEL);
                RadioGroup rg = (RadioGroup)hp.getItemByItemId(ID_PERM_GROUP);
                rg.clear();
                hp.enable();
            } else {
                toolbar.getItemByItemId(ID_BTN_REMOVE).disable();
                toolbar.getItemByItemId(ID_PERM_PANEL).disable();
            }
        }
    }

    /**
     * render radio for permissions
     * 
     * @author sriram
     * 
     */
    private class PermissionsReadCellRender implements GridCellRenderer<Sharing> {

        @Override
        public Object render(final Sharing model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Sharing> store, final Grid<Sharing> grid) {
            HorizontalPanel panel = new HorizontalPanel();

            final Radio read = buildRadio(org.iplantc.de.client.I18N.DISPLAY.read());
            read.addListener(Events.OnChange, new PermissionsFieldListener(model));

            final Radio write = buildRadio(org.iplantc.de.client.I18N.DISPLAY.write());
            write.addListener(Events.OnChange, new PermissionsFieldListener(model));

            final Radio own = buildRadio(org.iplantc.de.client.I18N.DISPLAY.own());
            own.addListener(Events.OnChange, new PermissionsFieldListener(model));

            if (!model.isOwner() && !model.isWritable() && model.isReadable()) {
                read.setValue(model.isReadable());
            } else if (!model.isOwner() && model.isWritable()) {
                write.setValue(model.isWritable());
            } else {
                own.setValue(model.isOwner());
            }

            RadioGroup group = new RadioGroup();
            group.add(read);
            group.add(write);
            group.add(own);

            panel.add(group);
            return panel;
        }
    }

    private Radio buildRadio(String label) {
        final Radio r = new Radio();
        r.setBoxLabel(label);
        r.setId("id" + label);
        return r;
    }

    private void updatePermissions(Radio r, Sharing model) {
        if (r.getBoxLabel().equals(org.iplantc.de.client.I18N.DISPLAY.read())) {
            model.setReadable(r.getValue());
        } else if (r.getBoxLabel().equals(org.iplantc.de.client.I18N.DISPLAY.write())) {
            model.setWritable(r.getValue());
        } else {
            model.setOwner(r.getValue());
        }
        grid.getStore().update(model);
    }

    private class BulkPermissionsFieldListener implements Listener<FieldEvent> {
        public void handleEvent(FieldEvent be) {
            Radio r = (Radio)be.getField();
            List<Sharing> models = grid.getSelectionModel().getSelectedItems();
            for (Sharing model : models) {
                updatePermissions(r, model);
            }
        }
    }

    private class RemoveButtonSelectionListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            MessageBox.confirm(org.iplantc.de.client.I18N.DISPLAY.unshare(),
                    org.iplantc.de.client.I18N.DISPLAY.unsharePrompt(resource.getName()),
                    new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            Button btn = be.getButtonClicked();
                            if (btn.getText().equals("Yes")) {
                                List<Sharing> models = grid.getSelectionModel().getSelectedItems();
                                unshareList.addAll(models);
                                removeModels(models);
                            }

                        }
                    });

        }
    }

    private void removeModels(List<Sharing> models) {
        for (Sharing model : models) {
            grid.getStore().remove(model);
        }
    }

    private class PermissionsFieldListener implements Listener<FieldEvent> {

        private Sharing model;

        public PermissionsFieldListener(Sharing model) {
            this.model = model;
        }

        @Override
        public void handleEvent(FieldEvent be) {
            Radio r = (Radio)be.getField();
            updatePermissions(r, model);
        }

    }

    /**
     * A custom renderer that renders with add / delete icon
     * 
     * @author sriram
     * 
     */
    private class NameCellRenderer implements GridCellRenderer<Sharing> {

        private static final String REMOVE_BUTTON_STYLE = "remove_button";
        private static final String DELETE_BUTTON_STYLE = "unshare_button";

        @Override
        public Object render(Sharing model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Sharing> store, Grid<Sharing> grid) {

            final HorizontalPanel hp = new HorizontalPanel();
            IconButton ib = buildButton(REMOVE_BUTTON_STYLE, model);
            hp.add(ib);
            hp.add(new Label(model.getName()));
            hp.setSpacing(3);
            return hp;
        }

        private IconButton buildButton(final String style, final Sharing model) {
            final IconButton btn = new IconButton(style, new SelectionListener<IconButtonEvent>() {

                @Override
                public void componentSelected(IconButtonEvent ce) {
                    IconButton src = (IconButton)ce.getSource();
                    String existing_style = src.getStyleName();

                    if (existing_style.contains(REMOVE_BUTTON_STYLE)) {
                        src.changeStyle(DELETE_BUTTON_STYLE);
                        return;
                    }

                    if (existing_style.contains(DELETE_BUTTON_STYLE)) {
                        grid.getStore().remove(model);
                        unshareList.add(model);
                        return;
                    }

                }
            });
            btn.setToolTip(org.iplantc.de.client.I18N.DISPLAY.unshare());
            return btn;
        }
    }

    /**
     * 
     * Enable drag n drop
     * 
     * @author sriram
     * 
     */
    private class SharingGridDropTarget extends GridDropTarget {

        public SharingGridDropTarget(Grid<Sharing> grid) {
            super(grid);
        }

        @Override
        protected void onDragDrop(DNDEvent e) {
            List<ModelData> list = e.getData();
            ListStore<ModelData> store = grid.getStore();
            for (ModelData md : list) {
                Collaborator c = (Collaborator)md;
                Sharing s = new Sharing(c, new Permissions(true, false, false));
                if (!store.contains(s)) {
                    store.add(s);
                }
            }
        }

    }
}
