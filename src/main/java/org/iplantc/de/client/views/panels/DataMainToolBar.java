package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.utils.PanelHelper;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DataMainToolBar extends ToolBar {
    private final String tag;
    private final DataContainer container;
    private TextField<String> filterField;
    private Button btnRefresh;

    public DataMainToolBar(final String tag, final DataContainer container) {
        this.tag = tag;
        this.container = container;
        setSpacing(4);
        addButtons();
    }

    private void addButtons() {
        btnRefresh = buildRefreshButton();
        add(btnRefresh);
        filterField = buildFilterField();
        add(filterField);
        add(new FillToolItem());
    }

    private Button buildRefreshButton() {
        Button refresh = PanelHelper.buildButton("idRefresh", I18N.DISPLAY.refresh(), //$NON-NLS-1$
                new RereshButtonListener());
        refresh.setIcon(AbstractImagePrototype.create(Resources.ICONS.refresh()));
        return refresh;
    }

    /**
     * Builds a text field for filtering items displayed in the data container.
     */
    private TextField<String> buildFilterField() {
        TextField<String> filter = new TextField<String>() {
            @Override
            public void onKeyUp(FieldEvent fe) {
                String filter = getValue();
                if (filter != null && !filter.isEmpty()) {
                    container.getDataStore().filter("name", filter); //$NON-NLS-1$
                } else {
                    container.getDataStore().clearFilters();
                }

            }
        };

        filter.setEmptyText(I18N.DISPLAY.filterDataList());

        return filter;
    }

    /**
     * Clears the toolbar's filtering text field.
     */
    public void clearFilters() {
        filterField.clear();
        container.getDataStore().clearFilters();
    }

    private class RereshButtonListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            ManageDataRefreshEvent event = new ManageDataRefreshEvent(tag, container.getCurrentPath(),
                    container.getSelectedItems());
            EventBus.getInstance().fireEvent(event);
        }

    }
}
