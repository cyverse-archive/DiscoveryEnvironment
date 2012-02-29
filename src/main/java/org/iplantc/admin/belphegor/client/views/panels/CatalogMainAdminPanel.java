package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.views.panels.BaseCatalogMainPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A panel that displays apps in a grid and lets the user delete or modify them.
 */
public class CatalogMainAdminPanel extends BaseCatalogMainPanel {
    private Button deleteButton;
    private Button restoreButton;

    /**
     * Creates a new CatalogMainAdminPanel.
     * 
     * @param templateService
     */
    public CatalogMainAdminPanel(String tag) {
        super(tag, new AppTemplateAdminServiceFacade());

        addToolBarButtons();

        new CatalogMainAdminPanelDragSource(analysisGrid);
    }

    private AppTemplateAdminServiceFacade getTemplateService() {
        return (AppTemplateAdminServiceFacade)templateService;
    }

    private void addToolBarButtons() {
        buildDeleteButton();
        buildRestoreButton();
        addToToolBar(deleteButton);
        addToToolBar(restoreButton);
    }

    private void buildDeleteButton() {
        deleteButton = new Button(I18N.DISPLAY.delete());

        deleteButton.disable();
        deleteButton.setId("idDelete"); //$NON-NLS-1$
        deleteButton.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));
        deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                deleteSelectedApp();
            }
        });

        addGridSelectionChangeListener(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                deleteButton.setEnabled(getSelectedApp() != null
                        && !(current_category.getId().equals(ToolIntegrationAdminProperties
                                .getInstance().getDefaultTrashAnalysisGroupId())));
            }
        });

    }

    private void buildRestoreButton() {
        restoreButton = new Button(I18N.DISPLAY.restoreApp());

        restoreButton.disable();
        restoreButton.setId("idRestore"); //$NON-NLS-1$
        restoreButton.setIcon(AbstractImagePrototype.create(Resources.ICONS.restore()));
        restoreButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                restoreSelectedApp();
            }
        });

        addGridSelectionChangeListener(new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                restoreButton.setEnabled(getSelectedApp() != null
                        && current_category.getId().equals(
                                ToolIntegrationAdminProperties.getInstance()
                                        .getDefaultTrashAnalysisGroupId()));
            }
        });
    }

    private void deleteSelectedApp() {
        final Analysis app = getSelectedApp();

        Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent ce) {
                Button btn = ce.getButtonClicked();

                // did the user click yes?
                if (btn.getItemId().equals(Dialog.YES)) {
                    confirmDeleteSelectedApp(app);
                }
            }
        };

        MessageBox.confirm(I18N.DISPLAY.confirmDeleteAppTitle(),
                I18N.DISPLAY.confirmDeleteApp(app.getName()), callback);
    }

    private void restoreSelectedApp() {
        final Analysis app = getSelectedApp();
        getTemplateService().restoreApplication(app.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSONParser.parseStrict(result).isObject();
                JSONArray arr = obj.get("categories").isArray();
                if (arr != null && arr.size() > 0) {
                    StringBuilder names_display = new StringBuilder("");
                    for (int i = 0; i < arr.size(); i++) {
                        names_display.append(JsonUtil.trim(arr.get(0).isObject().get("name").toString()));
                        if (i != arr.size() - 1) {
                            names_display.append(",");
                        }
                    }

                    MessageBox.info(I18N.DISPLAY.restoreAppSucessMsgTitle(),
                            I18N.DISPLAY.restoreAppSucessMsg(app.getName(), names_display.toString()),
                            null);
                }
                EventBus.getInstance().fireEvent(new CatalogCategoryRefreshEvent());
            }

            @Override
            public void onFailure(Throwable caught) {
                JSONObject obj = JSONParser.parseStrict(caught.getMessage()).isObject();
                String reason = JsonUtil.trim(obj.get("reason").toString());
                if (reason.contains("orphaned")) {
                    MessageBox.alert(I18N.DISPLAY.restoreAppFailureMsgTitle(),
                            I18N.DISPLAY.restoreAppFailureMsg(app.getName()), null);
                } else {
                    ErrorHandler.post(reason);
                }
            }
        });
    }

    private void confirmDeleteSelectedApp(final Analysis app) {
        getTemplateService().deleteApplication(app.getId(), new AdminServiceCallback() {
            @Override
            protected void onSuccess(JSONObject jsonResult) {
                EventBus.getInstance().fireEvent(new CatalogCategoryRefreshEvent());
            }

            @Override
            protected String getErrorMessage() {
                return I18N.ERROR.deleteApplicationError(app.getName());
            }
        });
    }

    @Override
    protected String buildAppDetailsTemplate() {
        StringBuilder tmpl = new StringBuilder(super.buildAppDetailsTemplate());

        // Deployed Components
        tmpl.append("<tpl if=\"suggested_categories\"><p>"); //$NON-NLS-1$

        tmpl.append("<u>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.categorySelect());
        tmpl.append("</u>"); //$NON-NLS-1$

        tmpl.append("<tpl for=\"suggested_categories\">"); //$NON-NLS-1$
        tmpl.append("<br/>{name}"); //$NON-NLS-1$
        tmpl.append("</tpl>"); //$NON-NLS-1$

        tmpl.append("</p></tpl>"); //$NON-NLS-1$

        return tmpl.toString();
    }

    /**
     * Overridden to render app names as hyperlinks to edit the app, and display average user rating
     */
    @Override
    protected ColumnModel buildColumnModel() {
        ColumnModel model = super.buildColumnModel();
        ColumnConfig cc = model.getColumnById(Analysis.RATING);
        cc.setHeader(I18N.DISPLAY.avgUserRating());
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
            String name = model.getName();
            if (model.isDisabled()) {
                name = "<img title ='"
                        + org.iplantc.core.uiapplications.client.I18N.DISPLAY.appUnavailable()
                        + "' src='./images/exclamation.png'/>&nbsp;" + name;
            }
            Hyperlink link = new Hyperlink(name, "analysis_name"); //$NON-NLS-1$
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
            return NumberFormat.getFormat("0.00").format(model.getFeedback().getAverage_score()); //$NON-NLS-1$
        }

    }

    private class EditCompleteCallback extends AdminServiceCallback {
        Dialog dialog;

        public EditCompleteCallback(Dialog d) {
            dialog = d;
        }

        @Override
        protected void onSuccess(JSONObject jsonResult) {
            dialog.hide();

            updateApp(JsonUtil.getObject(jsonResult, "application")); //$NON-NLS-1$
        }

        @Override
        protected String getErrorMessage() {
            return I18N.ERROR.updateApplicationError();
        }

    }

    private void updateApp(JSONObject jsonApp) {
        Analysis app = analysisGrid.getStore().findModel(Analysis.ID,
                JsonUtil.getString(jsonApp, Analysis.ID));

        if (app != null) {
            app.setName(JsonUtil.getString(jsonApp, Analysis.NAME));
            app.setIntegratorEmail(JsonUtil.getString(jsonApp, Analysis.INTEGRATOR_EMAIL));
            app.setIntegratorName(JsonUtil.getString(jsonApp, Analysis.INTEGRATOR_NAME));
            app.setWikiUrl(JsonUtil.getString(jsonApp, Analysis.WIKI_URL));
            app.setDescription(JsonUtil.getString(jsonApp, Analysis.DESCRIPTION));

            analysisGrid.getStore().update(app);
        }
    }

    private final class AppNameClickHandler implements Listener<BaseEvent> {
        private final Analysis model;

        private AppNameClickHandler(Analysis model) {
            this.model = model;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            final Dialog d = new Dialog();

            EditAppDetailsPanel editPanel = new EditAppDetailsPanel(model, new EditCompleteCallback(d));
            editPanel.addCancelButtonSelectionListener(new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(ButtonEvent ce) {
                    d.hide();
                }
            });

            d.setHeading(model.getName());
            d.getButtonBar().removeAll();
            d.setSize(595, 435);
            d.add(editPanel);
            d.show();
        }
    }

    /**
     * GridDragSource for re-categorizing Apps.
     * 
     * @author psarando
     * 
     */
    private class CatalogMainAdminPanelDragSource extends GridDragSource {
        public CatalogMainAdminPanelDragSource(Grid<Analysis> grid) {
            super(grid);
        }

        @Override
        public void onDragStart(DNDEvent event) {
            // Check if a valid row is selected.
            Element dragStartElement = (Element)event.getDragEvent().getStartElement();
            Element targetRow = analysisGrid.getView().findRow(dragStartElement).cast();
            if (targetRow == null) {
                event.setCancelled(true);
                return;
            }

            // Set the drag source in the event
            Analysis source = analysisGrid.getSelectionModel().getSelectedItem();

            if (source != null) {
                event.setData(source);
                event.getStatus().update(source.getName());
            } else {
                event.setCancelled(true);
                event.getStatus().setStatus(false);
            }
        }

        @Override
        public void onDragDrop(DNDEvent e) {
            // do nothing intentionally
        }
    }
}
