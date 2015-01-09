package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.apps.client.views.AppsView;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.pipelines.Pipeline;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.PipelineEditorWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.pipelines.client.presenter.PipelineViewPresenter;
import org.iplantc.de.pipelines.client.views.PipelineView;
import org.iplantc.de.pipelines.client.views.PipelineViewImpl;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.user.client.Command;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

public class PipelineEditorWindow extends IplantWindowBase {
    class PublishCallbackCommand implements Command {
        @Override
        public void execute() {
            IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(displayStrings.publishWorkflowSuccess()));
            if (close_after_save) {
                close_after_save = false;
                PipelineEditorWindow.super.hide();
            } else {
                initPipelineJson = presenter.getPublishJson(presenter.getPipeline());
            }
        }
    }

    private final IplantDisplayStrings displayStrings;
    private final PipelineView.Presenter presenter;
    private boolean close_after_save;
    private String initPipelineJson;

    public PipelineEditorWindow(WindowConfig config,
                                final AppsView.Presenter appsViewPresenter) {
        super(null, config);

        displayStrings = org.iplantc.de.resources.client.messages.I18N.DISPLAY;
        setHeadingText(displayStrings.pipeline());
        setSize("900", "500"); //$NON-NLS-1$ //$NON-NLS-2$
        setMinWidth(640);
        setMinHeight(440);

        PipelineView view = new PipelineViewImpl();
        presenter = new PipelineViewPresenter(view, new PublishCallbackCommand(), appsViewPresenter);
        ensureDebugId(DeModule.WindowIds.WORKFLOW_EDITOR);

        if (config instanceof PipelineEditorWindowConfig) {
            PipelineEditorWindowConfig pipelineConfig = (PipelineEditorWindowConfig) config;
            Pipeline pipeline = pipelineConfig.getPipeline();

            if (pipeline != null) {
                presenter.setPipeline(pipeline);
                initPipelineJson = presenter.getPublishJson(pipeline);
            } else {
                Splittable serviceWorkflowJson = pipelineConfig.getServiceWorkflowJson();
                if (serviceWorkflowJson != null) {
                    initPipelineJson = serviceWorkflowJson.getPayload();
                }
                presenter.setPipeline(serviceWorkflowJson);
            }

        }

        presenter.go(this);
        close_after_save = false;
    }

    @Override
    public WindowState getWindowState() {
        PipelineEditorWindowConfig configData = ConfigFactory.workflowIntegrationWindowConfig();
        configData.setPipeline(presenter.getPipeline());
        return createWindowState(configData);
    }

    @Override
    public void hide() {
        if (!isMinimized()) {
            if (initPipelineJson != null
                    && !initPipelineJson.equals(presenter.getPublishJson(presenter.getPipeline()))) {
                checkForSave();
            } else if (initPipelineJson == null
                           && presenter.getPublishJson(presenter.getPipeline()) != null) {
                checkForSave();
            } else {
                PipelineEditorWindow.super.hide();
            }
        } else {
            PipelineEditorWindow.super.hide();
        }
    }

    private void checkForSave() {
        MessageBox box = new MessageBox(displayStrings.save(), "");
        box.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
        box.setIcon(MessageBox.ICONS.question());
        box.setMessage(displayStrings.unsavedChanges());
        box.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {

                if (PredefinedButton.NO.equals(event.getHideButton())) {
                    PipelineEditorWindow.super.hide();
                } else if (PredefinedButton.YES.equals(event.getHideButton())) {
                    presenter.saveOnClose();
                    close_after_save = true;
                }
            }
        });
        box.show();
    }
}
