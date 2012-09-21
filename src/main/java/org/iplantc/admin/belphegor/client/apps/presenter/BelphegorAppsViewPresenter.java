package org.iplantc.admin.belphegor.client.apps.presenter;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.Services;
import org.iplantc.admin.belphegor.client.apps.views.widgets.BelphegorAppsToolbar;
import org.iplantc.admin.belphegor.client.apps.views.widgets.BelphegorAppsToolbarImpl;
import org.iplantc.admin.belphegor.client.apps.views.widgets.EditAppDetailsWidget;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.callbacks.AdminServiceCallback;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapplications.client.presenter.AppsViewPresenter;
import org.iplantc.core.uiapplications.client.presenter.proxy.AppGroupProxy;
import org.iplantc.core.uiapplications.client.services.AppServiceFacade;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;
import org.iplantc.de.client.DeCommonI18N;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Presenter class for the Belphegor <code>AppsView</code>.
 * 
 * The belphegor uses a different {@link AppServiceFacade} implementation than the one used in the
 * Discovery Environment. Through the use of deferred binding, the different {@link AppServiceFacade}
 * implementations are resolved, enabling the ability to reuse code.
 * 
 * <b> There are two places in the {@link AppsViewPresenter} where this deferred binding takes place; in
 * the {@link #go(com.google.gwt.user.client.ui.HasOneWidget)} method, and in the {@link AppGroupProxy}.
 * 
 * TODO JDS Deletion and restoration of apps needs to be tested. Arr!
 * 
 * 
 * @author jstroot
 * 
 */
public class BelphegorAppsViewPresenter extends AppsViewPresenter implements
        BelphegorAppsToolbar.Presenter {

    private final BelphegorAppsToolbar toolbar;
    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public BelphegorAppsViewPresenter(final AppsView view) {
        super(view);

        toolbar = new BelphegorAppsToolbarImpl();
        view.setNorthWidget(toolbar);
        this.toolbar.setPresenter(this);
    }

    @Override
    protected void selectFirstApp() {
        // Do nothing
    }

    @Override
    public void onAppGroupSelected(final AppGroup ag) {
        if (ag == null)
            return;

        view.setCenterPanelHeading(ag.getName());
        toolbar.setAddAppGroupButtonEnabled(true);
        toolbar.setRenameAppGroupButtonEnabled(true);
        toolbar.setDeleteButtonEnabled(true);
        toolbar.setRestoreButtonEnabled(false);
        fetchApps(ag);
    }

    @Override
    public void onAppSelected(final App app) {
        if (app == null)
            return;

        AppGroup currentAppGroup = getSelectedAppGroup();
        view.deSelectAllAppGroups();

        toolbar.setAddAppGroupButtonEnabled(false);
        toolbar.setRenameAppGroupButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(true);
        // Determine if the app is in the trash. If so, enable restore button
        if ((currentAppGroup != null)
                && currentAppGroup.getId().equals(
                        ToolIntegrationAdminProperties.getInstance().getDefaultTrashAnalysisGroupId())) {
            toolbar.setRestoreButtonEnabled(true);
        } else {
            toolbar.setRestoreButtonEnabled(false);
        }
    }

    @Override
    public void onAddAppGroupClicked() {
        if (getSelectedAppGroup() == null) {
            return;
        }
        final AppGroup selectedAppGroup = getSelectedAppGroup();

        // Check if a new AppGroup can be created in the target AppGroup.
        if (selectedAppGroup.getAppCount() > 0) {
            ErrorHandler.post(I18N.ERROR.deleteCategoryPermissionError());
            return;
        }

        IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.add(), 340, new IPlantPromptPanel(
                I18N.DISPLAY.addCategoryPrompt()) {
            @Override
            public void handleOkClick() {
                final String name = field.getValue();

                view.maskCenterPanel(DeCommonI18N.DISPLAY.loadingMask());
                Services.ADMIN_APP_SERVICE.addCategory(name, selectedAppGroup.getId(),
                        new AdminServiceCallback() {
                            @Override
                            protected void onSuccess(JSONObject jsonResult) {

                                // Get result
                                AutoBean<AppGroup> group = AutoBeanCodex.decode(factory,
                                        AppGroup.class, jsonResult.get("category").toString());

                                view.getTreeStore().add(selectedAppGroup, group.as());
                                view.unMaskCenterPanel();
                            }

                            @Override
                            protected String getErrorMessage() {
                                view.unMaskCenterPanel();
                                return I18N.ERROR.addAppGroupError(name);
                            }
                        });

            }
        });
        dlg.disableOkButton();
        dlg.show();

    }

    @Override
    public void onRenameAppGroupClicked() {
        if (getSelectedAppGroup() == null) {
            return;
        }
        final AppGroup selectedAppGroup = getSelectedAppGroup();

        PromptMessageBox msgBox = new PromptMessageBox(I18N.DISPLAY.rename(),
                I18N.DISPLAY.renamePrompt());
        final TextField field = ((TextField)msgBox.getField());
        field.setAutoValidate(true);
        field.setAllowBlank(false);
        field.setText(selectedAppGroup.getName());
        msgBox.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                Dialog btn = (Dialog)event.getSource();
                String text = btn.getHideButton().getItemId();
                if (text.equals(PredefinedButton.OK.name())) {
                    view.maskWestPanel(DeCommonI18N.DISPLAY.loadingMask());
                    Services.ADMIN_APP_SERVICE.renameAppGroup(selectedAppGroup.getId(),
                            field.getText(), new AsyncCallback<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    AutoBean<AppGroup> group = AutoBeanCodex.decode(factory,
                                            AppGroup.class, result);
                                    selectedAppGroup.setName(group.as().getName());
                                    view.getTreeStore().update(selectedAppGroup);
                                    view.unMaskWestPanel();
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    ErrorHandler.post(I18N.ERROR.renameCategoryError(selectedAppGroup
                                            .getName()));
                                    view.unMaskWestPanel();
                                }
                            });
                }

            }
        });
        msgBox.show();

    }

    @Override
    public void onDeleteClicked() {
        // Determine if the current selection is an AnalysisGroup
        if(getSelectedAppGroup() != null){
            final AppGroup selectedAppGroup = getSelectedAppGroup();
            
            // Determine if the selected AnalysisGroup can be deleted.
            if(selectedAppGroup.getAppCount() > 0){
                ErrorHandler.post(I18N.ERROR.deleteCategoryPermissionError());
                return; 
            }
            
            ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(), I18N.DISPLAY.confirmDeleteAppGroup(selectedAppGroup.getName()));
            msgBox.addHideHandler(new HideHandler() {
                
                @Override
                public void onHide(HideEvent event) {
                    Dialog btn = (Dialog)event.getSource();
                    String text = btn.getHideButton().getItemId();
                    if (text.equals(PredefinedButton.YES.name())) {
                        view.maskWestPanel(DeCommonI18N.DISPLAY.loadingMask());
                        Services.ADMIN_APP_SERVICE.deleteAppGroup(selectedAppGroup.getId(),
                                new AsyncCallback<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        // Refresh the catalog, so that the proper category counts
                                        // display.
                                        // FIXME JDS These events need to be common to ui-applications.
                                        EventBus.getInstance().fireEvent(
                                                new CatalogCategoryRefreshEvent());
                                        view.unMaskWestPanel();
                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        ErrorHandler.post(I18N.ERROR
                                                .deleteAppGroupError(selectedAppGroup.getName()));
                                        view.unMaskWestPanel();
                                    }
                                });
                    }

                }
            });
            msgBox.show();

        } else if(getSelectedApp() != null){
            final App selectedApp = getSelectedApp();
            ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                    I18N.DISPLAY.confirmDeleteAppTitle());
            msgBox.addHideHandler(new HideHandler() {

                @Override
                public void onHide(HideEvent event) {
                    Dialog btn = (Dialog)event.getSource();
                    String text = btn.getHideButton().getItemId();
                    if (text.equals(PredefinedButton.YES.name())) {
                        view.maskCenterPanel(DeCommonI18N.DISPLAY.loadingMask());
                        Services.ADMIN_APP_SERVICE.deleteApplication(selectedApp.getId(),
                                new AsyncCallback<String>() {

                                    @Override
                                    public void onSuccess(String result) {
                                        EventBus.getInstance().fireEvent(
                                                new CatalogCategoryRefreshEvent());
                                        view.removeApp(selectedApp);
                                        view.unMaskCenterPanel();
                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        ErrorHandler.post(I18N.ERROR.deleteApplicationError(selectedApp
                                                .getName()));
                                        view.unMaskCenterPanel();
                                    }
                                });
                    }
                }
            });
            msgBox.show();
        }

    }

    @Override
    public void onRestoreAppClicked() {
        if (getSelectedApp() == null) {
            return;
        }
        final App selectedApp = getSelectedApp();

        Services.ADMIN_APP_SERVICE.restoreApplication(selectedApp.getId(), new AsyncCallback<String>() {

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

                    MessageBox msgBox = new MessageBox(
                            I18N.DISPLAY.restoreAppSucessMsgTitle(), I18N.DISPLAY.restoreAppSucessMsg(
                                    selectedApp.getName(), names_display.toString()));
                    msgBox.setIcon(MessageBox.ICONS.info());
                    msgBox.setPredefinedButtons(PredefinedButton.OK);
                    msgBox.show();
                }
                EventBus.getInstance().fireEvent(new CatalogCategoryRefreshEvent());
            }

            @Override
            public void onFailure(Throwable caught) {
                JSONObject obj = JSONParser.parseStrict(caught.getMessage()).isObject();
                String reason = JsonUtil.trim(obj.get("reason").toString());
                if (reason.contains("orphaned")) {
                    AlertMessageBox alertBox = new AlertMessageBox(I18N.DISPLAY
                            .restoreAppFailureMsgTitle(), I18N.DISPLAY.restoreAppFailureMsg(selectedApp
                            .getName()));
                    alertBox.show();
                } else {
                    ErrorHandler.post(reason);
                }
            }
        });
    }

    @Override
    public void onAppNameSelected(final App app) {
        final Store<App>.Record appRecord = view.getListStore().getRecord(app);
        final AsyncCallback<String> tmpCallback = new AppEditCompleteCallback(appRecord);
        final EditAppDetailsWidget appDetailsWidget = new EditAppDetailsWidget(appRecord);
        appDetailsWidget.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                Dialog btn = (Dialog)event.getSource();
                String text = btn.getHideButton().getItemId();
                if (text.equals(PredefinedButton.OK.name())) {
                    if (app.getName() != null) {
                        ConfluenceServiceFacade.getInstance().movePage(app.getName(),
                                appDetailsWidget.getAppName(),
                                new ConfluenceServiceMovePageCallback(tmpCallback, appDetailsWidget));
                    } else {
                        Services.ADMIN_APP_SERVICE.updateApplication(appDetailsWidget.appAsJson(), tmpCallback);
                    }
                } else if (text.equals(PredefinedButton.CANCEL.name())) {
                    appDetailsWidget.hide();
                    view.getListStore().rejectChanges();
                }
            }
        });
        appDetailsWidget.show();

    }

    private class ConfluenceServiceMovePageCallback implements AsyncCallback<String> {

        private final AsyncCallback<String> callback;
        private final EditAppDetailsWidget appDetailsWidget;

        public ConfluenceServiceMovePageCallback(AsyncCallback<String> callback,
                EditAppDetailsWidget appDetailsWidget) {
            this.callback = callback;
            this.appDetailsWidget = appDetailsWidget;
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught.getMessage());
            Services.ADMIN_APP_SERVICE.updateApplication(appDetailsWidget.appAsJson(), callback);
        }

        @Override
        public void onSuccess(String result) {
            appDetailsWidget.setWikiUrl(result);
            Services.ADMIN_APP_SERVICE.updateApplication(appDetailsWidget.appAsJson(), callback);
        }
    }

    private class AppEditCompleteCallback implements AsyncCallback<String> {
        private final Store<App>.Record appRecord;

        public AppEditCompleteCallback(Store<App>.Record record) {
            this.appRecord = record;
        }

        @Override
        public void onSuccess(String result) {
            appRecord.commit(true);
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.updateApplicationError());
        }
    }
}
