package org.iplantc.de.client.views.windows;

import org.iplantc.core.client.pipelines.views.panels.PipelineEditorPanel;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.PipelineEditorWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.services.TemplateServiceFacade;
import org.iplantc.de.client.views.panels.CatalogCategoryPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;

public class PipelineEditorWindow extends IPlantWindow {
    private PipelineEditorPanel editorPanel;
    private WindowConfig config;

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
        editorPanel = new PipelineEditorPanel(new CatalogCategoryPanel(), new TemplateServiceFacade(),
                new PublishCallbackCommand());
        add(editorPanel);

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
            this.config = (PipelineEditorWindowConfig)config;
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
        JSONObject obj = super.getWindowViewState();
        obj.put(PipelineEditorWindowConfig.PIPELINE_CONFIG, editorPanel.toJson());

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.pipelineEditorTag(),
                obj);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.pipelineEditorTag(),
                ActionType.DISPLAY_WINDOW);
    }
}
