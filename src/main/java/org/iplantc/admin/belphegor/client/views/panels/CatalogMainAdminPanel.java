package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.views.panels.BaseCatalogMainPanel;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A panel that displays apps in a grid.
 */
public class CatalogMainAdminPanel extends BaseCatalogMainPanel {

    /**
     * Creates a new CatalogMainAdminPanel.
     * 
     * @param templateService
     */
    public CatalogMainAdminPanel() {
        initToolBar();
    }

    private void initToolBar() {
        addToToolBar(buildDeleteButton());
    }

    private Button buildDeleteButton() {
        Button btn = new Button(I18N.DISPLAY.delete());

        btn.setId("idDelete"); //$NON-NLS-1$
        btn.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));

        return btn;
    }

    /**
     * Overridden to render app names as hyperlinks to edit the app, and display average user rating
     */
    @Override
    protected ColumnModel buildColumnModel() {
        ColumnModel model = super.buildColumnModel();
        ColumnConfig cc = model.getColumnById(Analysis.RATING);
        cc.setHeader("Average User Rating");
        cc.setAlignment(HorizontalAlignment.CENTER);
        cc.setRenderer(new VotingCellRenderer());
        model.getColumnById(Analysis.NAME).setRenderer(new AppNameCellRenderer());
        return model;
    }

    /**
     * Displays app names as hyperlinks; clicking a link edit the app.
     */
    public class AppNameCellRenderer implements GridCellRenderer<Analysis> {

        @Override
        public Object render(final Analysis model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Analysis> store, Grid<Analysis> grid) {
            Hyperlink link = new Hyperlink(model.getName(), "analysis_name"); //$NON-NLS-1$
            link.addListener(Events.OnClick, new AppNameClickHandler(model));
            link.setWidth(model.getName().length());
            return link;
        }
    }

    /**
     * 
     * Show average user rating
     * 
     * @author sriram
     * 
     */
    private class VotingCellRenderer implements GridCellRenderer<Analysis> {
        @Override
        public Object render(final Analysis model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Analysis> store, Grid<Analysis> grid) {
            return model.getFeedback().getAverage_score();
        }

    }

    private class EditCompleteCallback implements AsyncCallback<String> {

        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess(String result) {
            // TODO Auto-generated method stub

        }

    }

    private final class AppNameClickHandler implements Listener<BaseEvent> {
        private final Analysis model;

        private AppNameClickHandler(Analysis model) {
            this.model = model;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            EditAppDetailsPanel editPanel = new EditAppDetailsPanel(model, new EditCompleteCallback());
            Dialog d = new Dialog();
            d.setHeading(model.getName());
            d.setButtons(Dialog.OKCANCEL);
            d.setHideOnButtonClick(true);
            d.setSize(595, 440);
            d.add(editPanel);
            d.show();
        }
    }

}
