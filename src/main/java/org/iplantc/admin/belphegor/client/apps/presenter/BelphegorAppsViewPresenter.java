package org.iplantc.admin.belphegor.client.apps.presenter;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.apps.views.editors.AppEditor;
import org.iplantc.admin.belphegor.client.apps.views.widgets.BelphegorAppsToolbar;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.callbacks.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.impl.AppAdminServiceFacade;
import org.iplantc.admin.belphegor.client.services.impl.AppAdminUserServiceFacade;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.presenter.AppsViewPresenter;
import org.iplantc.core.uiapps.client.presenter.proxy.AppGroupProxy;
import org.iplantc.core.uiapps.client.services.AppServiceFacade;
import org.iplantc.core.uiapps.client.views.AppsView;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppSearchRpcProxy;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantPromptDialog;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
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
 * FIXME JDS DND Implement drag and drop reordering of AppGroups and Apps for belphegor only.
 * 
 * 
 * @author jstroot
 * 
 */
public class BelphegorAppsViewPresenter extends AppsViewPresenter implements
        BelphegorAppsToolbar.Presenter, AppEditor.Presenter {

    private final BelphegorAppsToolbar toolbar;
    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
    private final AppAdminServiceFacade adminAppService;

    @Inject
    public BelphegorAppsViewPresenter(final AppsView view, final AppGroupProxy proxy,
            final BelphegorAppsToolbar toolbar, AppAdminServiceFacade appService,
            AppAdminUserServiceFacade appUserService) {
        super(view, proxy, null, appService, appUserService);
        this.adminAppService = appService;

        this.toolbar = toolbar;
        view.setNorthWidget(this.toolbar);
        this.toolbar.setPresenter(this);
    }

    @Override
    public AppSearchRpcProxy getAppSearchRpcProxy() {
        return toolbar.getAppSearchRpcProxy();
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

        ToolIntegrationAdminProperties props = ToolIntegrationAdminProperties.getInstance();

        // Check if a new AppGroup can be created in the target AppGroup.
        if ((!selectedAppGroup.getName().contains("Public Apps"))
                && selectedAppGroup.getAppCount() > 0
                && selectedAppGroup.getGroups().size() == 0
                || ((props.getDefaultTrashAnalysisGroupId() == selectedAppGroup.getId()) || props
                        .getDefaultBetaAnalysisGroupId() == selectedAppGroup.getId())) {
            ErrorHandler.post(I18N.ERROR.addCategoryPermissionError());
            return;
        }

        final IPlantPromptDialog dlg = new IPlantPromptDialog(I18N.DISPLAY.add(), 0, "", null);
        dlg.setHeadingText(I18N.DISPLAY.addCategoryPrompt());
        dlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {

                final String name = dlg.getFieldText();

                view.maskCenterPanel(I18N.DISPLAY.loadingMask());
                adminAppService.addCategory(name, selectedAppGroup.getId(), new AdminServiceCallback() {
                    @Override
                    protected void onSuccess(JSONObject jsonResult) {

                        // Get result
                        AutoBean<AppGroup> group = AutoBeanCodex.decode(factory, AppGroup.class,
                                jsonResult.get("category").toString());

                        view.addAppGroup(selectedAppGroup, group.as());
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
                    view.maskWestPanel(I18N.DISPLAY.loadingMask());
                    adminAppService.renameAppGroup(selectedAppGroup.getId(), field.getText(),
                            new AsyncCallback<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    AutoBean<AppGroup> group = AutoBeanCodex.decode(factory,
                                            AppGroup.class, result);
                                    selectedAppGroup.setName(group.as().getName());
                                    view.updateAppGroup(selectedAppGroup);
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
        if (getSelectedAppGroup() != null) {
            final AppGroup selectedAppGroup = getSelectedAppGroup();

            // Determine if the selected AnalysisGroup can be deleted.
            if (selectedAppGroup.getAppCount() > 0) {
                ErrorHandler.post(I18N.ERROR.deleteCategoryPermissionError());
                return;
            }

            ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                    I18N.DISPLAY.confirmDeleteAppGroup(selectedAppGroup.getName()));
            msgBox.addHideHandler(new HideHandler() {

                @Override
                public void onHide(HideEvent event) {
                    Dialog btn = (Dialog)event.getSource();
                    String text = btn.getHideButton().getItemId();
                    if (text.equals(PredefinedButton.YES.name())) {
                        view.maskWestPanel(I18N.DISPLAY.loadingMask());
                        adminAppService.deleteAppGroup(selectedAppGroup.getId(),
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

        } else if (getSelectedApp() != null) {
            final App selectedApp = getSelectedApp();
            ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                    I18N.DISPLAY.confirmDeleteAppTitle());
            msgBox.addHideHandler(new HideHandler() {

                @Override
                public void onHide(HideEvent event) {
                    Dialog btn = (Dialog)event.getSource();
                    String text = btn.getHideButton().getItemId();
                    if (text.equals(PredefinedButton.YES.name())) {
                        view.maskCenterPanel(I18N.DISPLAY.loadingMask());
                        adminAppService.deleteApplication(selectedApp.getId(),
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

        adminAppService.restoreApplication(selectedApp.getId(), new AsyncCallback<String>() {

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

                    MessageBox msgBox = new MessageBox(I18N.DISPLAY.restoreAppSucessMsgTitle(),
                            I18N.DISPLAY.restoreAppSucessMsg(selectedApp.getName(),
                                    names_display.toString()));
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
        new AppEditor(app, this).show();
    }

    @Override
    public void onAppEditorSave(App app) {
        final AsyncCallback<String> editCompleteCallback = new AppEditCompleteCallback();

        // Serialize App to JSON object
        String jsonString = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(app)).getPayload();
        final JSONObject jsonObj = JsonUtil.getObject(jsonString);

        if (app.getName() != null) {
            ConfluenceServiceFacade.getInstance().movePage(app.getName(), app.getName(),
                    new AsyncCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            adminAppService.updateApplication(jsonObj, editCompleteCallback);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught.getMessage());
                            adminAppService.updateApplication(jsonObj, editCompleteCallback);
                        }
                    });
            // new ConfluenceServiceMovePageCallback(tmpCallback, jsonObj));
        } else {
            adminAppService.updateApplication(jsonObj, editCompleteCallback);
        }

    }

    private class AppEditCompleteCallback implements AsyncCallback<String> {

        public AppEditCompleteCallback() {
        }

        @Override
        public void onSuccess(String result) {
            // appRecord.commit(true);
        }

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(I18N.ERROR.updateApplicationError());
        }
    }

}
