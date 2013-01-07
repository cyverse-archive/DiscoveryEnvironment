package org.iplantc.de.client.preferences.presenter;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.preferences.views.PreferencesView;
import org.iplantc.de.client.preferences.views.PreferencesView.Presenter;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.utils.DEInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * 
 * A presenter impl for user preferences
 * 
 * @author sriram
 * 
 */
public class PreferencesPresenterImpl implements Presenter {

    PreferencesView view;

    public PreferencesPresenterImpl(PreferencesView view) {
        this.view = view;
        this.view.setPresenter(this);
        this.view.setValues();
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    @Override
    public void save() {
        UserSettings us = view.getValues();
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.saveUserPreferences(us.toJson(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                DEInfo.display(I18N.DISPLAY.save(), I18N.DISPLAY.saveSettings());
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void setDefaults() {
        view.setDefaultValues();
    }

}
