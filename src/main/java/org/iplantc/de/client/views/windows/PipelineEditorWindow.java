package org.iplantc.de.client.views.windows;

import org.iplantc.core.client.pipelines.views.panels.PipelineEditorPanel;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uiapplications.client.store.AnalysisToolGroupStoreWrapper;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.PipelineEditorWindowConfig;
import org.iplantc.de.client.views.panels.CatalogCategoryPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PipelineEditorWindow extends IPlantWindow {
    private PipelineEditorPanel editorPanel;
    private AbstractCatalogCategoryPanel categoryPanel;
    private WindowConfig config;

    private final AppTemplateUserServiceFacade templateService = GWT
            .create(AppTemplateUserServiceFacade.class);

    public PipelineEditorWindow(String tag) {
        super(tag);

        init();
        compose();
    }

    private void init() {
        setHeading(I18N.DISPLAY.pipeline());
        setLayout(new FitLayout());
        setSize(800, 410);
    }

    private void compose() {
        categoryPanel = new CatalogCategoryPanel(tag);
        editorPanel = new PipelineEditorPanel(tag, categoryPanel, templateService,
                new PublishCallbackCommand());
        add(editorPanel);
    }

    private void getData() {
        // TemplateServiceFacade facade = new TemplateServiceFacade();

        templateService.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AnalysisToolGroupStoreWrapper wrapper = new AnalysisToolGroupStoreWrapper();
                        wrapper.updateWrapper(result);
                        categoryPanel.seed(wrapper.getStore());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        super.show();
        if (config != null) {
            editorPanel.configure(((PipelineEditorWindowConfig)config).getPipelineConfig());
            setWindowViewState();
            // reset config
            config = null;
        }

    }

    /**
     * Applies a window configuration to the window.
     * 
     * @param config
     */
    @Override
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof PipelineEditorWindowConfig) {
            this.config = config;
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        editorPanel.cleanup();
    }

    class PublishCallbackCommand implements Command {
        @Override
        public void execute() {
            hide();
        }

    }

    @Override
    public JSONObject getWindowState() {
        PipelineEditorWindowConfig configData = new PipelineEditorWindowConfig(config);
        storeWindowViewState(configData);

        configData.setPipelineConfig(editorPanel.toJson());

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.pipelineEditorTag(),
                configData);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.pipelineEditorTag(),
                ActionType.DISPLAY_WINDOW);
    }
}
