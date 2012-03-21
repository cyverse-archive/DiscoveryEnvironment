package org.iplantc.de.client.views.windows;

import org.iplantc.core.client.pipelines.views.panels.PipelineEditorPanel;
import org.iplantc.de.client.services.TemplateServiceFacade;
import org.iplantc.de.client.views.panels.CatalogCategoryPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;

public class PipelineEditorWindow extends IPlantWindow {
    private PipelineEditorPanel editorPanel;

    public PipelineEditorWindow(String tag) {
        super(tag);

        init();
        compose();
    }

    private void init() {
        setHeading("Automate Workflow");
        setLayout(new FitLayout());
        setSize(800, 410);
    }

    private void compose() {
        editorPanel = new PipelineEditorPanel(new CatalogCategoryPanel(), new TemplateServiceFacade(),
                new PublishCallbackCommand());
        add(editorPanel);
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
        // TODO Auto-generated method stub
        return null;
    }
}
