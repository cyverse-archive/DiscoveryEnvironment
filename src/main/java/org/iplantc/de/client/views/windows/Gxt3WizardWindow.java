package org.iplantc.de.client.views.windows;

import org.iplantc.core.client.widgets.appWizard.view.AppWizardView;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.services.AppUserServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.models.WizardWindowConfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Gxt3WizardWindow extends Gxt3IplantWindow {

    private final AppWizardView.Presenter presenter;
    private final AppUserServiceFacade templateService = Services.USER_APP_SERVICE;
    
    public Gxt3WizardWindow(String tag, WizardWindowConfig config) {
        super(tag, false, true, true, true, config);
        setSize("640", "410");
        setBorders(false);
        
        
        presenter = GWT.create(AppWizardView.Presenter.class);
        init(presenter, config);
        
    }

    /**
     * Fetches the App Template from the config or the backend service, sends it to the presenter, 
     * then starts the presenter.
     * 
     * XXX JDS Fetching of the template should be done in the presenter.
     * @param presenter
     * @param config
     */
    private void init(final AppWizardView.Presenter presenter, WizardWindowConfig config) {
        
        if (config != null) {
            presenter.setAppTemplateFromJsonString(config.getWizardConfig().toString());
            presenter.getAppTemplate().getLabel();
            GWT.log("Wizard Config had template");
            presenter.go(this);
        } else {
            templateService.getTemplate(tag, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.unableToRetrieveWorkflowGuide(), caught);
                }

                @Override
                public void onSuccess(String json) {
                    presenter.setAppTemplateFromJsonString(json);
                    presenter.getAppTemplate().getLabel();
                    presenter.go(Gxt3WizardWindow.this);
                }
            });
        }
        
    }

    @Override
    public JSONObject getWindowState() {
        return null;
    }

}
