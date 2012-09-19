package org.iplantc.admin.belphegor.client.gxt3.presenter;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.Services;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.gxt3.views.widgets.BelphegorAppsToolbar;
import org.iplantc.admin.belphegor.client.gxt3.views.widgets.BelphegorAppsToolbarImpl;
import org.iplantc.admin.belphegor.client.services.callbacks.AdminServiceCallback;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

/**
 * Presenter class for the Belphegor <code>AppsView</code>.
 * 
 * The belphegor uses a different {@link AppServiceFacade} implementation than the one used in
 * the Discovery Environment. Through the use of deferred binding, the different
 * {@link AppServiceFacade} implementations are resolved, enabling the ability to reuse code.
 * 
 * <b> There are two places in the {@link AppsViewPresenter} where this deferred binding takes place; in
 * the {@link #go(com.google.gwt.user.client.ui.HasOneWidget)} method, and in the
 * {@link AppGroupProxy}.
 * 
 * @author jstroot
 * 
 */
public class BelphegorAppsViewPresenter extends AppsViewPresenter implements
        BelphegorAppsToolbar.Presenter {

    private final BelphegorAppsToolbar toolbar;

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

        view.deSelectAllAppGroups();

        toolbar.setAddAppGroupButtonEnabled(false);
        toolbar.setRenameAppGroupButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(true);
        toolbar.setRestoreButtonEnabled(true);
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

                final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                view.maskCenterPanel(DeCommonI18N.DISPLAY.loadingMask());
                Services.ADMIN_TEMPLATE_SERVICE.addCategory(name, selectedAppGroup.getId(),
                        new AdminServiceCallback() {
                            @Override
                            protected void onSuccess(JSONObject jsonResult) {

                                AutoBean<AppGroup> group = AutoBeanCodex.decode(factory,
                                        AppGroup.class, jsonResult.get("category").toString());
                                // Get result

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
        // TODO Auto-generated method stub

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
                        Services.ADMIN_TEMPLATE_SERVICE.deleteAppGroup(selectedAppGroup.getId(),
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
            
        }

    }

    @Override
    public void onRestoreAppClicked() {
        // TODO Auto-generated method stub

    }
}
