package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.de.client.models.Collaborator;
import org.iplantc.de.client.models.Sharing;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Label;

/**
 * A panel to display a list of sharee and their permissions
 * 
 * @author sriram
 * 
 */
public class SharePanel extends ContentPanel {
    private DiskResource resource;
    private Grid<Sharing> grid;
    private List<Sharing> unshareList;

    public SharePanel(DiskResource dr) {
        unshareList = new ArrayList<Sharing>();
        this.setResource(dr);
        init();
    }

    private void init() {
        setSize(378, 225);
        setLayout(new FitLayout());
        ColumnModel cm = buildColumnModel();
        cm.addHeaderGroup(0, 1, new HeaderGroupConfig(org.iplantc.de.client.I18N.DISPLAY.permissions(),
                1, 3));
        grid = new Grid<Sharing>(new ListStore<Sharing>(), cm);
        add(grid);
        new SharingGridDropTarget(grid);

    }

    private ColumnModel buildColumnModel() {
        ColumnConfig sharee = new ColumnConfig(Sharing.NAME, I18N.DISPLAY.name(), 200);
        ColumnConfig read = new ColumnConfig(Sharing.READ, Sharing.READ, 50);
        ColumnConfig write = new ColumnConfig(Sharing.WRITE, Sharing.WRITE, 50);
        ColumnConfig own = new ColumnConfig(Sharing.OWN, Sharing.OWN, 50);

        sharee.setMenuDisabled(true);
        sharee.setSortable(true);
        sharee.setRenderer(new NameCellRenderer());

        read.setRenderer(new PermissionsReadCellRender());
        read.setMenuDisabled(true);
        read.setSortable(false);

        write.setRenderer(new PermissionsWriteCellRender());
        write.setMenuDisabled(true);
        write.setSortable(false);

        own.setRenderer(new PermissionsOwnCellRender());
        own.setMenuDisabled(true);
        own.setSortable(false);

        return new ColumnModel(Arrays.asList(sharee, read, write, own));
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

    /**
     * render radio for read permission
     * 
     * @author sriram
     * 
     */
    private class PermissionsReadCellRender implements GridCellRenderer<Sharing> {

        @Override
        public Object render(final Sharing model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Sharing> store, final Grid<Sharing> grid) {
            final Radio r = new Radio();
            if (!model.isOwner() && !model.isWritable() && model.isReadable()) {
                r.setValue(model.isReadable());
            }
            r.addListener(Events.OnChange, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    model.setReadable(r.getValue());
                    grid.getStore().update(model);
                }
            });
            return r;
        }
    }

    /**
     * render radio for write permission
     * 
     * @author sriram
     * 
     */
    private class PermissionsWriteCellRender implements GridCellRenderer<Sharing> {

        @Override
        public Object render(final Sharing model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Sharing> store, final Grid<Sharing> grid) {
            final Radio r = new Radio();
            if (!model.isOwner() && model.isWritable()) {
                r.setValue(model.isWritable());
            }
            r.addListener(Events.OnChange, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    model.setWritable(r.getValue());
                    grid.getStore().update(model);
                }
            });
            return r;
        }
    }

    /**
     * render radio for owner permission
     * 
     * @author sriram
     * 
     */
    private class PermissionsOwnCellRender implements GridCellRenderer<Sharing> {

        @Override
        public Object render(final Sharing model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Sharing> store, final Grid<Sharing> grid) {
            final Radio r = new Radio();
            r.setValue(model.isOwner());
            r.addListener(Events.OnChange, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    model.setOwner(r.getValue());
                    grid.getStore().update(model);
                }
            });
            return r;
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
        private static final String DELETE_BUTTON_STYLE = "delete_button";

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
                        unshareList.add(model);
                        return;
                    }

                }
            });

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
            for (ModelData md : list) {
                Collaborator c = (Collaborator)md;
                Sharing s = new Sharing(c, new Permissions(true, false, false));
                grid.getStore().add(s);
            }
        }

    }
}
