package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.panels.IPlantPromptPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
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
        buildActionButtons();
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
     * Builds a buttons with items for adding, renaming, and deleting categories.
     * 
     * 
     */
    private void buildActionButtons() {
        final Button addCategory = buildAddCategoryButton();
        final Button renameCategory = buildRenameCategoryButton();
        final Button deleteCategory = buildDeleteCategoryButton();

        add(addCategory);
        add(renameCategory);
        add(deleteCategory);
    }

    private Button buildDeleteCategoryButton() {
        Button ret = new Button();
        ret.setText(I18N.DISPLAY.delete());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));
        ret.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                final String name = selectedCategory.getName();

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
                                    // Refresh the catalog, so that the proper category counts display.
                                    EventBus.getInstance().fireEvent(new CatalogCategoryRefreshEvent());
                                }

                                @Override
                                protected String getErrorMessage() {
                                    return I18N.ERROR.deleteCategoryError(name);
                                }
                            });
                        }
                    }
                };

                MessageBox.confirm(I18N.DISPLAY.warning(), I18N.DISPLAY.confirmDeleteCategory(name),
                        callback);
            }
        });

        return ret;
    }

    private Button buildRenameCategoryButton() {
        Button ret = new Button();
        ret.setText(I18N.DISPLAY.rename());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.cat_edit()));
        ret.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.rename(), 340, new RenamePromptPanel(
                        I18N.DISPLAY.rename(), selectedCategory));

                dlg.show();
            }
        });
        return ret;
    }

    private Button buildAddCategoryButton() {
        Button ret = new Button();
        ret.setText(I18N.DISPLAY.add());
        ret.setIcon(AbstractImagePrototype.create(Resources.ICONS.category()));
        ret.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                final AnalysisGroup selectedCategory = getSelectedCategory();

                if (selectedCategory == null) {
                    return;
                }

                IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.add(), 340, new IPlantPromptPanel(
                        I18N.DISPLAY.add()) {
                    @Override
                    public void handleOkClick() {
                        final String name = field.getValue();

                        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(
                                maskingParent);
                        facade.addCategory(name, selectedCategory.getId(), new AdminServiceCallback() {
                            @Override
                            protected void onSuccess(JSONObject jsonResult) {
                                AnalysisGroup group = new AnalysisGroup(JsonUtil.getString(jsonResult,
                                        "categoryId"), name, "", 0, true); //$NON-NLS-1$ //$NON-NLS-2$

                                treePnlCategories.getStore().add(selectedCategory, group, false);
                            }

                            @Override
                            protected String getErrorMessage() {
                                return I18N.ERROR.addCategoryError(name);
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

    private final class RenamePromptPanel extends IPlantPromptPanel {
        private final AnalysisGroup selectedCategory;

        private RenamePromptPanel(String caption, AnalysisGroup selectedCategory) {
            super(caption);

            field.setValue(selectedCategory.getName());
            this.selectedCategory = selectedCategory;
        }

        @Override
        public void handleOkClick() {
            AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(maskingParent);
            facade.renameCategory(selectedCategory.getId(), field.getValue(),
                    new AdminServiceCallback() {
                        @Override
                        protected void onSuccess(JSONObject jsonResult) {
                            selectedCategory.setName(JsonUtil.getString(jsonResult,
                                    AnalysisGroupTreeModel.NAME));

                            treePnlCategories.getStore().update(selectedCategory);
                        }

                        @Override
                        protected String getErrorMessage() {
                            return I18N.ERROR.renameCategoryError(selectedCategory.getName());
                        }
                    });
        }
    }
}
