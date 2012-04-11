package org.iplantc.de.client.views.windows;

import java.util.ArrayList;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.events.AnalysisCategorySelectedEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisCategorySelectedEventHandler;
import org.iplantc.core.uiapplications.client.events.AnalysisSelectEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisSelectEventHandler;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.CatalogWindowDispatcher;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.BasicWindowConfig;
import org.iplantc.de.client.models.CatalogWindowConfig;
import org.iplantc.de.client.models.DEProperties;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.services.TemplateServiceFacade;
import org.iplantc.de.client.views.panels.CatalogCategoryPanel;
import org.iplantc.de.client.views.panels.CatalogMainPanel;

import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * A window that displays categories in left panel and, tools, pipelines, workflows and details in the
 * right side panel.
 * 
 * @author sriram
 * 
 */
public class DECatalogWindow extends IPlantThreePanelWindow {
    public static String WEST_COLLAPSE_BTN_ID = "idCategoryCollapseBtn"; //$NON-NLS-1$

    private ArrayList<HandlerRegistration> handlers;
    private CatalogCategoryPanel catPanel;
    private CatalogMainPanel mainPanel;

    public static String WORKSPACE;
    public static String FAVORITES;
    public static String APPLICATIONS_UNDER_DEVLOPMENT;
    public static String BETA_GROUP_ID;

    /**
     * 
     * @param tag
     * @param config this may be a BasicWindowConfig or a CatalogWindowConfig; the latter can be used to
     *            preselect a tool in the window
     */
    public DECatalogWindow(String tag, BasicWindowConfig config) {
        super(tag, config);
        initHandlers();
        initConstants();
    }

    private void initConstants() {
        DEProperties properties = DEProperties.getInstance();

        WORKSPACE = properties.getPrivateWorkspace();

        JSONArray items = JSONParser.parseStrict(properties.getPrivateWorkspaceItems()).isArray();
        if (items != null) {
            APPLICATIONS_UNDER_DEVLOPMENT = JsonUtil.getRawValueAsString(items.get(0));
            FAVORITES = JsonUtil.getRawValueAsString(items.get(1));
        }

        BETA_GROUP_ID = properties.getDefaultBetaCategoryId();
    }

    private void initHandlers() {
        EventBus eventbus = EventBus.getInstance();
        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(eventbus.addHandler(AnalysisCategorySelectedEvent.TYPE,
                new AnalysisCategorySelectedEventHandlerImpl()));

        handlers.add(eventbus.addHandler(AnalysisSelectEvent.TYPE, new AnalysisSelectEventHandler() {
            @Override
            public void onSelection(AnalysisSelectEvent event) {
                if (Constants.CLIENT.deCatalog().equals(event.getSourceTag())) {
                    CatalogWindowDispatcher dispatcher = new CatalogWindowDispatcher();
                    dispatcher.launchCatalogWindow(event.getCategoryId(), event.getAppId());
                }
            }
        }));
    }

    @Override
    public void cleanup() {
        for (HandlerRegistration hanlder : handlers) {
            hanlder.removeHandler();
        }

        super.cleanup();
    }

    @Override
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof CatalogWindowConfig) {
            this.config = (CatalogWindowConfig)config;
        }
    }

    @Override
    protected String getCaption() {
        return I18N.DISPLAY.applications();
    }

    @Override
    protected void initModel() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        super.show();

        if (config != null && config instanceof CatalogWindowConfig) {
            selectConfigData((CatalogWindowConfig)config);
            setWindowViewState();
            config = null;
        }

    }

    @Override
    protected void initPanels() {
        catPanel = new CatalogCategoryPanel();
        mainPanel = new CatalogMainPanel(Constants.CLIENT.deCatalog());
    }

    protected void selectConfigData(CatalogWindowConfig catalogConfig) {
        String appID = catalogConfig.getAppId();
        String categoryID = catalogConfig.getCategoryId();

        // The category panel will select the first root node by default, which is the workspace, if
        // this category ID is null.
        catPanel.selectCategory(categoryID);
        mainPanel.selectTool(appID);
    }

    @Override
    protected void compose() {
        pnlContents.add(catPanel, dataWest);
        pnlContents.add(mainPanel, dataCenter);
    }

    @Override
    protected void onAfterLayout() {
        super.onAfterLayout();

        // CORE-2992: Add an ID to the Categories panel collapse tool to assist QA.
        Component toolCollapseBtn = catPanel.getHeader().getTool(0);
        if (toolCollapseBtn != null) {
            toolCollapseBtn.setId(WEST_COLLAPSE_BTN_ID);
        }
    }

    @Override
    protected void setInitialSize() {
        setSize(800, 410);

    }

    @Override
    protected int getWestWidth() {
        return 220;
    }

    @Override
    protected int getEastWidth() {
        return 200;
    }

    private class AnalysisCategorySelectedEventHandlerImpl implements
            AnalysisCategorySelectedEventHandler {

        @Override
        public void onSelection(AnalysisCategorySelectedEvent event) {
            if (event.getSourcePanel() == catPanel) {
                mainPanel.setHeading(event.getGroup().getName());
                updateAnalysesListing(event.getGroup());
            }
        }

    }

    private void updateAnalysesListing(final AnalysisGroup group) {
        TemplateServiceFacade facade = new TemplateServiceFacade();
        mask(I18N.DISPLAY.loadingMask());
        facade.getAnalysis(group.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<Analysis> analyses = new ArrayList<Analysis>();

                JSONArray templates = JsonUtil.getArray(JsonUtil.getObject(result), "templates"); //$NON-NLS-1$
                if (templates != null) {
                    for (int i = 0; i < templates.size(); i++) {
                        Analysis analysis = new Analysis(JsonUtil.getObjectAt(templates, i));
                        analyses.add(analysis);
                    }
                }

                mainPanel.seed(analyses, group);
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                unmask();
            }
        });

    }

    @Override
    public JSONObject getWindowState() {
        JSONObject obj = super.getWindowViewState();
        if (mainPanel.getSelectedApp() != null) {
            obj.put(CatalogWindowConfig.APP_ID, new JSONString(mainPanel.getSelectedApp().getId()));
        }

        if (mainPanel.getCurrentCategoryId() != null) {
            obj.put(CatalogWindowConfig.CATEGORY_ID, new JSONString(mainPanel.getCurrentCategoryId()));
        }

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.deCatalog(), obj);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.deCatalog(), ActionType.DISPLAY_WINDOW);
    }
}
