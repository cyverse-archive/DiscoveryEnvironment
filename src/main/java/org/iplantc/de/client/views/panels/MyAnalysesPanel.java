package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.gxt3.AnalysisExecution;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.utils.NotificationHelper;
import org.iplantc.de.client.utils.NotifyInfo;
import org.iplantc.de.client.views.MyAnalysesGrid;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PushButton;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.LiveToolItem;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * A container panel of MyAanalysesGrid
 * 
 * @author sriram
 * 
 */
public class MyAnalysesPanel extends VerticalLayoutContainer {

    private final String DELETE_ITEM_ID = "idDeleteBtn"; //$NON-NLS-1$
    private final String CANCEL_ANALYSIS_ITEM_ID = "idCancelAnalysisBtn"; //$NON-NLS-1$
    private static final String VIEW_PARAMETER_ITEM_ID = "idViewParameter";

    private MyAnalysesGrid analysisGrid;

    private final HashMap<String, PushButton> analyses_buttons;
    private final HashMap<String, Menu> menus;

    private ToolBar topComponentMenu;

    private ArrayList<HandlerRegistration> handlers;

    private String idWorkspace;

    private String idCurrentSelection;

    private final AnalysisServiceFacade facadeAnalysisService;

    protected static CheckBoxSelectionModel<AnalysisExecution> sm;
    private TextField filter;

    /**
     * Indicates the status of an analysis.
     */
    public static enum EXECUTION_STATUS {
        /** analysis status unknown */
        UNKNOWN(I18N.CONSTANT.unknown()),
        /** analysis is ready */
        SUBMITTED(I18N.CONSTANT.submitted()),
        /** analysis is running */
        RUNNING(I18N.CONSTANT.running()),
        /** analysis is complete */
        COMPLETED(I18N.CONSTANT.completed()),
        /** analysis timed out */
        HELD(I18N.CONSTANT.held()),
        /** analysis failed */
        FAILED(I18N.CONSTANT.failed()),
        /** analysis was stopped */
        SUBMISSION_ERR(I18N.CONSTANT.subErr()),
        /** analysis is idle */
        IDLE(I18N.CONSTANT.idle()),
        /** analysis is removed */
        REMOVED(I18N.CONSTANT.removed());

        private String displayText;

        private EXECUTION_STATUS(String displaytext) {
            this.displayText = displaytext;
        }

        /**
         * Returns a string that identifies the EXECUTION_STATUS.
         * 
         * @return
         */
        public String getTypeString() {
            return toString().toLowerCase();
        }

        /**
         * Null-safe and case insensitive variant of valueOf(String)
         * 
         * @param typeString name of an EXECUTION_STATUS constant
         * @return
         */
        public static EXECUTION_STATUS fromTypeString(String typeString) {
            if (typeString == null || typeString.isEmpty()) {
                return null;
            }

            return valueOf(typeString.toUpperCase());
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    /**
     * Create a new MyAnalysisPanel
     * 
     * @param caption text to be displayed as caption
     * @param idCurrentSelection
     * 
     */
    public MyAnalysesPanel(final String caption, final String idCurrentSelection) {
        this.idCurrentSelection = idCurrentSelection;
        analyses_buttons = new LinkedHashMap<String, PushButton>();
        menus = new HashMap<String, Menu>();
        init(caption);
        initWorkspaceId();

        // setHeaderVisible(false);
        facadeAnalysisService = new AnalysisServiceFacade();
    }

    private void initWorkspaceId() {
        idWorkspace = UserInfo.getInstance().getWorkspaceId();
    }

    private void init(String caption) {
        // buildTopComponent();

        // setTopComponent(topComponentMenu);
        // setHeadingText(caption);
        // setLayout(new FitLayout());
        registerHandlers();

    }

    private void registerHandlers() {
        handlers = new ArrayList<HandlerRegistration>();
    }

    private void buildTopComponent() {
        topComponentMenu = new ToolBar();
        // topComponentMenu = getButtonBar();
        topComponentMenu.add(buildViewParamsButton());
        topComponentMenu.add(buildDeleteButton());
        topComponentMenu.add(buildCancelAnalysisButton());
        buildFilterField();
        topComponentMenu.add(filter);

    }

    private void setButtonState() {
        int selectionSize = 0;

        if (analysisGrid != null) {
            selectionSize = analysisGrid.getSelectionModel().getSelectedItems().size();
        }

        switch (selectionSize) {
            case 0:
                for (PushButton btn : analyses_buttons.values()) {
                    btn.setEnabled(false);
                }

                break;

            case 1:
                enableDeleteButtonByStatus();
                enableViewButtonByStatus();
                enableCancelAnalysisButtonByStatus();
                break;

            default:
                analyses_buttons.get(DELETE_ITEM_ID).setEnabled(true);
                analyses_buttons.get(VIEW_PARAMETER_ITEM_ID).setEnabled(false);
                enableCancelAnalysisButtonByStatus();
        }
    }

    private PushButton buildDeleteButton() {
        PushButton b = new PushButton(AbstractImagePrototype.create(Resources.ICONS.cancel())
                .createImage());
        b.ensureDebugId(DELETE_ITEM_ID);
        b.setText(I18N.DISPLAY.delete());
        b.setEnabled(false);
        b.addClickHandler(new DeleteClickHandler());
        analyses_buttons.put(DELETE_ITEM_ID, b);
        return b;
    }

    private PushButton buildCancelAnalysisButton() {
        PushButton b = new PushButton(AbstractImagePrototype.create(Resources.ICONS.stop())
                .createImage());
        b.setText(I18N.DISPLAY.cancelAnalysis());
        b.ensureDebugId(CANCEL_ANALYSIS_ITEM_ID);
        b.setEnabled(false);
        b.addClickHandler(new CancelAnalysisClickHandler());
        analyses_buttons.put(CANCEL_ANALYSIS_ITEM_ID, b);

        return b;
    }

    private PushButton buildViewParamsButton() {
        PushButton b = new PushButton(AbstractImagePrototype.create(Resources.ICONS.fileView())
                .createImage());
        b.setText(I18N.DISPLAY.viewParamLbl());
        b.ensureDebugId(VIEW_PARAMETER_ITEM_ID);
        b.setEnabled(false);
        b.addClickHandler(new ViewParamClickHandler());

        analyses_buttons.put(VIEW_PARAMETER_ITEM_ID, b);
        return b;
    }

    /**
     * Builds a text field for filtering items displayed in the data container.
     */
    private void buildFilterField() {
        filter = new TextField() {
            @Override
            public void onKeyUp(Event fe) {
                // We want to apply the filters, so we will set them to enabled to do so.
                // analysisGrid.getStore().setEnableFilters(true);
            }
        };
        filter.setEmptyText(I18N.DISPLAY.filterAnalysesList());
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
      
        buildCheckBoxSelectionModel(menus);
        analysisGrid = MyAnalysesGrid.createInstance(sm);
        if (getIdCurrentSelection() != null && analysisGrid.getStore() != null) {
            analysisGrid.setCurrentSelection(getIdCurrentSelection());
        }

        analysisGrid.getStore().addFilter(new StoreFilterImpl());
        analysisGrid.getView().setEmptyText(I18N.DISPLAY.noAnalyses());

        ToolBar toolBar = new ToolBar();
        toolBar.add(new LiveToolItem(analysisGrid));
        toolBar.addStyleName(ThemeStyles.getStyle().borderTop());
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");

        buildTopComponent();
        new Resizable(this);
        add(topComponentMenu, new VerticalLayoutData(1, -1));
        // add(analysisGrid);
        // add(toolBar);
        add(new WidgetComponent(analysisGrid), new VerticalLayoutData(1, 1));
        add(toolBar, new VerticalLayoutData(1, 25));

        addGridEventListeners();
    }

    /**
     * Builds the CheckBoxSelectionModel used in the ColumnDisplay.ALL ColumnModel.
     * 
     * @param menus2
     */
    protected static void buildCheckBoxSelectionModel(final HashMap<String, Menu> floating_menus) {
        if (sm != null) {
            return;
        }
        sm = new CheckBoxSelectionModel<AnalysisExecution>(
                new IdentityValueProvider<AnalysisExecution>());
        sm.getColumn().setAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }

    private void addGridEventListeners() {
        analysisGrid.getSelectionModel().addSelectionHandler(new SelectionHandler<AnalysisExecution>() {

            @Override
            public void onSelection(SelectionEvent<AnalysisExecution> event) {
                setButtonState();
                AnalysisExecution ae = analysisGrid.getSelectionModel().getSelectedItem();
                if (ae != null) {
                    idCurrentSelection = ae.getId();
                }

            }
        });

        analysisGrid.getStore().addStoreUpdateHandler(new StoreUpdateHandler<AnalysisExecution>() {

            @Override
            public void onUpdate(StoreUpdateEvent<AnalysisExecution> event) {
                setButtonState();
            }
        });
    }

    private void doDelete() {
        if (analysisGrid.getSelectionModel().getSelectedItems().size() > 0) {
            final List<AnalysisExecution> execs = analysisGrid
                    .getSelectionModel().getSelectedItems();
            ConfirmMessageBox mb = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                    I18N.DISPLAY.analysesExecDeleteWarning());
            mb.addHideHandler(new DeleteMessageBoxHandler(execs, mb));
        }
    }

    private void doCancelAnalysis() {
        if (analysisGrid.getSelectionModel().getSelectedItems().size() > 0) {
            final List<AnalysisExecution> execs = analysisGrid.getSelectionModel().getSelectedItems();
            for (AnalysisExecution ae : execs) {
                if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.SUBMITTED.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.IDLE.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.RUNNING.toString()))) {
                    facadeAnalysisService.stopAnalysis(ae.getId(), new CancelAnalysisServiceCallback(ae));
                }
            }
        }

    }

    private void enableViewButtonByStatus() {
        AnalysisExecution ae = analysisGrid.getSelectionModel()
                .getSelectedItem();
        if (ae != null) {
            if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.COMPLETED.toString()))
                    || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.FAILED.toString()))) {
                analyses_buttons.get(VIEW_PARAMETER_ITEM_ID).setEnabled(true);
            } else {
                analyses_buttons.get(VIEW_PARAMETER_ITEM_ID).setEnabled(true);
            }
        }
    }

    private void enableDeleteButtonByStatus() {
        List<AnalysisExecution> aes = analysisGrid.getSelectionModel().getSelectedItems();
        boolean enable = true;
        for (AnalysisExecution ae : aes) {
            if (ae != null) {
                if (!ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.COMPLETED.toString()))
                        && !ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.FAILED.toString()))) {
                    enable = false;
                    break;
                }
            }
        }
        analyses_buttons.get(DELETE_ITEM_ID).setEnabled(enable);
    }

    private void enableCancelAnalysisButtonByStatus() {
        List<AnalysisExecution> aes = analysisGrid.getSelectionModel().getSelectedItems();
        boolean enable = false;
        for (AnalysisExecution ae : aes) {
            if (ae != null) {
                if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.SUBMITTED.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.IDLE.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.RUNNING.toString()))) {
                    enable = true;
                    break;
                }
            }
        }
        analyses_buttons.get(CANCEL_ANALYSIS_ITEM_ID).setEnabled(enable);

    }

    /**
     * {@inheritDoc}
     */
    public void cleanup() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
        analysisGrid.cleanup();
    }

    /**
     * update id of the execution that needs to selected
     * 
     * @param id id of the analysis execution that needs to selected
     */
    public void updateSelection(String idCurrentSelection) {
        if (analysisGrid != null) {
            analysisGrid.selectModel(idCurrentSelection);
        }
    }

    /**
     * @return the idCurrentSelection
     */
    public String getIdCurrentSelection() {
        return idCurrentSelection;
    }

    private class StoreFilterImpl implements StoreFilter<AnalysisExecution> {
        @Override
        public boolean select(Store<AnalysisExecution> store, AnalysisExecution parent,
                AnalysisExecution item) {
            if (filter != null && filter.getValue() != null && !filter.getValue().isEmpty()) {
                return item.getName().toLowerCase().startsWith(filter.getValue().toLowerCase())
                        || item.getAnalysisName().toLowerCase()
                                .startsWith(filter.getValue().toLowerCase());
            }
    
            return true;
        }
    
    }

    private class DeleteClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            doDelete();
        }
    }

    private class CancelAnalysisClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            doCancelAnalysis();
        }
    }

    private class ViewParamClickHandler implements ClickHandler {
    
        @Override
        public void onClick(ClickEvent event) {
            AnalysisExecution ae = analysisGrid.getSelectionModel().getSelectedItem();
            Dialog d = new Dialog();
            d.setResizable(false);
            d.setHeadingText(I18N.DISPLAY.viewParameters(ae.getName()));
            d.add(new WidgetComponent(new AnalysisParameterViewerPanel(ae.getId())));
            d.setSize("520", "375");
            d.setPredefinedButtons(Dialog.PredefinedButton.OK);
            d.setHideOnButtonClick(true);
            d.show();
            d.toFront();
        }
    
    }

    private final class DeleteSeviceCallback implements AsyncCallback<String> {
        private final List<AnalysisExecution> execs;
        private final List<AnalysisExecution> items_to_delete;

        private DeleteSeviceCallback(List<AnalysisExecution> items_to_delete,
                List<AnalysisExecution> execs) {
            this.execs = execs;
            this.items_to_delete = items_to_delete;
        }

        @Override
        public void onSuccess(String arg0) {
            updateGrid(execs);

        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.deleteAnalysisError(), caught);
        }

        private void updateGrid(List<AnalysisExecution> execs) {
            for (AnalysisExecution ae : items_to_delete) {
                analysisGrid.getStore().remove(ae);
            }

            if (items_to_delete == null || execs.size() != items_to_delete.size()) {
                AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.warning(),
                        I18N.DISPLAY.analysesNotDeleted());
                amb.show();
            }
        }
    }

    private final class CancelAnalysisServiceCallback implements AsyncCallback<String> {

        private final AnalysisExecution ae;

        public CancelAnalysisServiceCallback(final AnalysisExecution ae) {
            this.ae = ae;
        }

        @Override
        public void onSuccess(String result) {
            NotifyInfo.notify(NotificationHelper.Category.ANALYSIS, I18N.DISPLAY.success(),
                    I18N.DISPLAY.analysisStopSuccess(ae.getName()), null);
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.stopAnalysisError(ae.getName()), caught);
        }

    }

    private class DeleteMessageBoxHandler implements HideHandler {
        private final List<AnalysisExecution> execs;
        private final ArrayList<AnalysisExecution> items_to_delete;
        private final ConfirmMessageBox mb;
    
        public DeleteMessageBoxHandler(final List<AnalysisExecution> execs, final ConfirmMessageBox mb) {
            this.execs = execs;
            this.mb = mb;
            items_to_delete = new ArrayList<AnalysisExecution>();
        }
    
        @Override
        public void onHide(HideEvent event) {
            if (mb.getHideButton() == mb.getButtonById(PredefinedButton.YES.name())) {
                String body = buildDeleteRequestBody(execs);
                facadeAnalysisService.deleteAnalysis(idWorkspace, body, new DeleteSeviceCallback(
                        items_to_delete, execs));
            }
        }
    
        private String buildDeleteRequestBody(List<AnalysisExecution> execs2) {
            JSONObject obj = new JSONObject();
            JSONArray items = new JSONArray();
            int count = 0;
            for (AnalysisExecution ae : execs2) {
                if (ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.COMPLETED.toString()))
                        || ae.getStatus().equalsIgnoreCase((EXECUTION_STATUS.FAILED.toString()))) {
                    items.set(count++, new JSONString(ae.getId()));
                    items_to_delete.add(ae);
                }
    
            }
            obj.put("executions", items); //$NON-NLS-1$
            return obj.toString();
        }
    
    }

}
