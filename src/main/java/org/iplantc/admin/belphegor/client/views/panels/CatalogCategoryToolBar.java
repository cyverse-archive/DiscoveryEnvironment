package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A toolbar for Category Admin actions.
 * 
 * @author psarando
 * 
 */
public class CatalogCategoryToolBar extends ToolBar {

    private TreePanel<AnalysisGroupTreeModel> treePnlCategories;
    private Component maskingParent;

    /**
     * create a new instance of this tool bar
     * 
     * @param tag a tag for this widget
     */
    public CatalogCategoryToolBar() {
        add(buildActionsMenuButton());
    }

    public void setMaskingParent(Component maskingParent) {
        this.maskingParent = maskingParent;
    }

    /**
     * Sets the tree panel associated with this action toolbar.
     * 
     * @param treePnlCategories
     */
    public void setCategoryTreePanel(TreePanel<AnalysisGroupTreeModel> treePnlCategories) {
        this.treePnlCategories = treePnlCategories;
    }

    protected AnalysisGroup getSelectedCategory() {
        if (treePnlCategories == null) {
            return null;
        }

        return (AnalysisGroup)treePnlCategories.getSelectionModel().getSelectedItem();
    }

    /**
     * Builds a menu button with items for adding, renaming, and deleting categories.
     * 
     * @return The navigation panel's toolbar's Actions menu button.
     */
    private Button buildActionsMenuButton() {
        // build the menu and menu actions
        Menu actionMenu = new Menu();

        final MenuItem addCategory = buildAddCategoryMenuItem();
        final MenuItem renameCategory = buildRenameCategoryMenuItem();
        final MenuItem deleteCategory = buildDeleteCategoryMenuItem();

        actionMenu.add(addCategory);
        actionMenu.add(renameCategory);
        actionMenu.add(deleteCategory);

        // create the menu button
        Button ret = new Button(I18N.DISPLAY.moreActions());

        ret.setMenu(actionMenu);

        // Add a selection listener that will disable or enable actions based on the selected category.
        ret.addSelectionListener(new MoreActionsListener(addCategory, renameCategory, deleteCategory));

        return ret;
    }

    private MenuItem buildDeleteCategoryMenuItem() {
        MenuItem ret = new MenuItem();
        ret.setText(I18N.DISPLAY.delete());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));
        ret.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
                    @Override
                    public void handleEvent(MessageBoxEvent ce) {
                        // did the user click yes?
                        if (ce.getButtonClicked().getItemId().equals(Dialog.YES)) {
                            AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(
                                    maskingParent);
                            facade.deleteCategory(selectedCategory.getId(), new AdminServiceCallback() {
                                @Override
                                protected void onSuccess(JSONObject jsonResult) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                protected String getErrorMessage() {
                                    // TODO Auto-generated method stub
                                    return null;
                                }
                            });
                        }
                    }
                };

                MessageBox.confirm(I18N.DISPLAY.warning(),
                        I18N.DISPLAY.confirmDeleteCategory(selectedCategory.getName()), callback);
            }
        });

        return ret;
    }

    private MenuItem buildRenameCategoryMenuItem() {
        MenuItem ret = new MenuItem();
        ret.setText(I18N.DISPLAY.rename());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.category_open()));
        ret.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 340, new IPlantPromptPanel(
                        I18N.DISPLAY.rename()) {
                    @Override
                    public void handleOkClick() {
                        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(
                                maskingParent);
                        facade.renameCategory(selectedCategory.getId(), field.getValue(),
                                new AdminServiceCallback() {
                                    @Override
                                    protected void onSuccess(JSONObject jsonResult) {
                                        // TODO parse actual result from service
                                        selectedCategory.setName(JsonUtil.getString(jsonResult,
                                                AnalysisGroupTreeModel.NAME));

                                        treePnlCategories.getStore().update(selectedCategory);
                                    }

                                    @Override
                                    protected String getErrorMessage() {
                                        // TODO Auto-generated method stub
                                        return null;
                                    }
                                });
                    }
                });

                dlg.show();
            }
        });
        return ret;
    }

    private MenuItem buildAddCategoryMenuItem() {
        MenuItem ret = new MenuItem();
        ret.setText(I18N.DISPLAY.add());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.category()));
        ret.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.add(), 340, new IPlantPromptPanel(
                        I18N.DISPLAY.add()) {
                    @Override
                    public void handleOkClick() {
                        // TODO Auto-generated method stub
                        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(
                                maskingParent);
                        facade.addCategory(field.getValue(), selectedCategory.getId(),
                                new AdminServiceCallback() {
                                    @Override
                                    protected void onSuccess(JSONObject jsonResult) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    protected String getErrorMessage() {
                                        // TODO Auto-generated method stub
                                        return null;
                                    }
                                });

                    }
                });

                dlg.disableOkButton();
                dlg.show();
            }
        });

        return ret;
    }

    private final class MoreActionsListener extends SelectionListener<ButtonEvent> {
        private final MenuItem addCategory;
        private final MenuItem renameCategory;
        private final MenuItem deleteCategory;

        private MoreActionsListener(MenuItem addCategory, MenuItem renameCategory,
                MenuItem deleteCategory) {
            this.addCategory = addCategory;
            this.renameCategory = renameCategory;
            this.deleteCategory = deleteCategory;
        }

        @Override
        public void componentSelected(ButtonEvent ce) {
            // enable all actions if a category is selected
            boolean actionMenuItemsEnabled = (getSelectedCategory() != null);

            addCategory.setEnabled(actionMenuItemsEnabled);
            renameCategory.setEnabled(actionMenuItemsEnabled);
            deleteCategory.setEnabled(actionMenuItemsEnabled);
        }

    }
}
