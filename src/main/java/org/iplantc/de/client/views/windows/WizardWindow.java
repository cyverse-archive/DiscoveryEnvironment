package org.iplantc.de.client.views.windows;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.metadata.WizardPropertyGroupContainer;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.events.JobLaunchedEvent;
import org.iplantc.de.client.events.JobLaunchedEventHandler;
import org.iplantc.de.client.events.WizardValidationEvent;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.models.WizardWindowConfig;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.TemplateServiceFacade;
import org.iplantc.de.client.strategies.WizardValidationBroadcastStrategy;
import org.iplantc.de.client.utils.builders.WizardBuilder;
import org.iplantc.de.client.views.dialogs.JobLaunchDialog;
import org.iplantc.de.client.views.taskbar.IPlantTaskButton;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Provides interface for setting parameters and launching jobs.
 * 
 * @author amuir
 * 
 */
public class WizardWindow extends IPlantWindow {
    private Button btnLaunchJob;
    private final ComponentValueTable tblComponentVals;

    private List<HandlerRegistration> handlers;
    private WizardWindowConfig config;
    private Status status;

    /**
     * Constructs an instance of the object given an identifier.
     * 
     * @param tag a unique identifier used as a "window handle."
     */
    public WizardWindow(String tag, WindowConfig config) {
        super(tag, false, true, false, true);

        tblComponentVals = new ComponentValueTable(new WizardValidationBroadcastStrategy());
        this.config = (WizardWindowConfig)config;
        init();
        build();
    }

    private void init() {
        setBorders(false);
        setShadow(false);
        setResizable(false);
        setSize(640, 410);
    }

    private void enableValidation() {
        // now that the panels have been initialized, we can allow validation
        tblComponentVals.enableValidation();

        // we need to enforce contracts before any validation can occur
        tblComponentVals.enforceContracts();

        // perform our initial validation... we circumvent the normal method of validation
        // because this window is created from a fired event and if our validator fires an
        // event (that would
        // potentially disable our buttons) that event is never picked up by the system.
        List<String> errors = tblComponentVals.validate(false, false);

        if (errors.isEmpty()) {
            btnLaunchJob.enable();
        } else {
            btnLaunchJob.disable();
        }
    }

    private void doJobLaunch() {
        createOutputFolderByDefault();
    }

    private void buildLaunchJobButton() {
        btnLaunchJob = new Button(I18N.DISPLAY.launchAnalysis());
        btnLaunchJob.setIcon(AbstractImagePrototype.create(Resources.ICONS.applicationLaunch()));
        btnLaunchJob.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                // validate again because the user might have clicked the button without blurring an
                // invalid field
                status.setBusy(I18N.DISPLAY.loadingMask());
                List<String> errors = tblComponentVals.validate(false, true);
                if (errors == null || errors.isEmpty()) {
                    doJobLaunch();
                    btnLaunchJob.disable();
                }
            }
        });
    }

    private ContentPanel buildButtonPanel() {
        ContentPanel ret = new ContentPanel();
        ret.setSize(640, 30);
        ret.setHeaderVisible(false);
        ret.setLayout(new FitLayout());

        buildLaunchJobButton();
        status = new Status();

        ToolBar bar = new ToolBar();

        bar.add(status);
        bar.add(new FillToolItem());
        bar.add(btnLaunchJob);
        ret.add(bar);
        return ret;
    }

    private void build() {
        if (config != null) {
            initWizard(config.getWizardConfig().toString());
        } else {
            TemplateServiceFacade facade = new TemplateServiceFacade();
            facade.getTemplate(tag, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.unableToRetrieveWorkflowGuide(), caught);
                }

                @Override
                public void onSuccess(String json) {
                    initWizard(json);
                }
            });
        }

    }

    private void handleJobLaunch(final String name) {
        hide();
    }

    /**
     * Add handlers for events
     */
    private void registerEventHandlers() {
        handlers = new ArrayList<HandlerRegistration>();

        EventBus eventbus = EventBus.getInstance();

        handlers.add(eventbus.addHandler(WizardValidationEvent.TYPE,
                new org.iplantc.de.client.events.WizardValidationEventHandler() {
                    @Override
                    public void onInvalid(WizardValidationEvent event) {
                        btnLaunchJob.disable();
                    }

                    @Override
                    public void onValid(WizardValidationEvent event) {
                        // validate to see if all other fields are valid as well; if so, enable the
                        // button.
                        // XXX button not re-enabled when valid input is entered into two previously
                        // valid fields,
                        // but it'll have to do until the validation rewrite.
                        List<String> errors = tblComponentVals.validate(false, false);
                        btnLaunchJob.setEnabled(errors == null || errors.isEmpty());
                    }
                }));

        handlers.add(eventbus.addHandler(JobLaunchedEvent.TYPE, new JobLaunchedEventHandler() {
            @Override
            public void onLaunch(JobLaunchedEvent event) {
                if (event.getTag().equals(tag)) {
                    handleJobLaunch(event.getName());
                }
            }
        }));
    }

    private void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        if (handlers != null) {
            for (HandlerRegistration reg : handlers) {
                eventbus.removeHandler(reg);
            }

            // clear our list
            handlers.clear();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
        super.cleanup();
        removeEventHandlers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeading(String text) {
        super.setHeading(text);

        IPlantTaskButton btn = getData("taskButton"); //$NON-NLS-1$

        if (btn != null) {
            btn.updateText();
        }
    }

    /**
     * Applies a window configuration to the window.
     * 
     * @param config
     */
    @Override
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof WizardWindowConfig) {
            this.config = (WizardWindowConfig)config;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        super.show();
        setWindowViewState();
    }

    @Override
    public JSONObject getWindowState() {
        WizardWindowConfig configData = new WizardWindowConfig(getWindowViewState());
        configData.setWizardConfig(tblComponentVals.getWizardPorpertyGroupContainerAsJson());

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.wizardTag(),
                configData);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.wizardTag(), ActionType.DISPLAY_WINDOW);
    }

    private void initWizard(String json) {
        WizardPropertyGroupContainer container = new WizardPropertyGroupContainer(json);
        WizardBuilder builder = new WizardBuilder();
        setHeading(container.getLabel());

        final ContentPanel pnlGroupContainer = builder.build(container, tblComponentVals);
        final ContentPanel pnlLaunchButton = buildButtonPanel();
        pnlLaunchButton.addListener(Events.Render, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                pnlGroupContainer.setSize(getInnerWidth(),
                        getInnerHeight() - pnlLaunchButton.getHeight());

            }
        });

        add(pnlGroupContainer);
        add(pnlLaunchButton);

        layout();

        registerEventHandlers();
        enableValidation();
    }

    protected void createOutputFolderByDefault() {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        facade.getDefaultOutput(DEProperties.getInstance().getDefaultOutputFolderName(),
                new OutputFolderCreateCallback());
    }

    private class OutputFolderCreateCallback implements AsyncCallback<String> {

        public OutputFolderCreateCallback() {
        }

        @Override
        public void onSuccess(String result) {
            status.clearStatus("");
            JSONObject obj = JsonUtil.getObject(result);
            String path = JsonUtil.getString(obj, "path");
            JobLaunchDialog dlg = new JobLaunchDialog(tag, tblComponentVals, path);
            dlg.show();
            btnLaunchJob.enable();
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
        }
    }
}