package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uiapplications.client.store.AnalysisToolGroupStoreWrapper;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.ScrollSupport;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogCategoryAdminPanel extends AbstractCatalogCategoryPanel {
    private final CatalogCategoryToolBar toolBar;

    public CatalogCategoryAdminPanel() {
        toolBar = new CatalogCategoryToolBar();
        toolBar.setMaskingParent(this);

        setTopComponent(toolBar);

        loadCategories();
    }

    private void loadCategories() {
        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade();

        facade.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AnalysisToolGroupStoreWrapper wrapper = new AnalysisToolGroupStoreWrapper();
                        wrapper.updateWrapper(result);
                        seed(wrapper.getStore());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }
                });
    }

    @Override
    public void seed(TreeStore<AnalysisGroupTreeModel> models) {
        super.seed(models);

        initDragAndDrop();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        toolBar.setCategoryTreePanel(categoryPanel);
    }

    private void initDragAndDrop() {
        CatalogCategoryAdminPanelDropTarget target = new CatalogCategoryAdminPanelDropTarget(
                categoryPanel, this);
        target.setAllowDropOnLeaf(true);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.APPEND);

        new CatalogCategoryAdminPanelDragSource(categoryPanel);
    }

    private class CatalogCategoryAdminPanelDropTarget extends TreePanelDropTarget {
        private final Component maskingCaller;
        private ScrollSupport scrollSupport;

        public CatalogCategoryAdminPanelDropTarget(TreePanel<AnalysisGroupTreeModel> tree,
                Component maskingCaller) {
            super(tree);

            this.maskingCaller = maskingCaller;
        }

        @Override
        public void onDragEnter(DNDEvent event) {
            if (isAutoScroll()) {
                if (scrollSupport == null) {
                    scrollSupport = new ScrollSupport(getScrollElement());
                } else if (scrollSupport.getScrollElement() == null) {
                    scrollSupport.setScrollElement(getScrollElement());
                }
                scrollSupport.start();
            }
        }

        private El getScrollElement() {
            if (getScrollElementId() == null) {
                return tree.el();
            }

            return new El(DOM.getElementById(getScrollElementId()));
        }

        @Override
        protected void onDragCancelled(DNDEvent event) {
            scrollSupport.stop();
        }

        @Override
        protected void onDragMove(DNDEvent event) {
            super.onDragMove(event);

            // super may have cancelled the event
            if (event.isCancelled()) {
                return;
            }

            @SuppressWarnings("rawtypes")
            TreeNode node = categoryPanel.findNode(event.getTarget());

            if (node == null) {
                event.setCancelled(true);
                event.getStatus().setStatus(false);
                return;
            }

            // get our destination category
            AnalysisGroup target = (AnalysisGroup)node.getModel();

            // if the target category is the src category, don't allow a drop there
            if (target.isLeaf() && target.getCount() > 0) {
                event.setCancelled(true);
                event.getStatus().setStatus(false);
            }
        }

        @Override
        public void onDragDrop(DNDEvent event) {
            @SuppressWarnings("rawtypes")
            TreeNode node = categoryPanel.findNode(event.getTarget());
            final AnalysisGroup category = (AnalysisGroup)event.getData();

            if (node != null && category != null) {
                // get our destination category
                final AnalysisGroup target = (AnalysisGroup)node.getModel();

                // call service to move category
                AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(maskingCaller);
                facade.moveCategory(category.getId(), target.getId(), new AdminServiceCallback() {
                    @Override
                    protected void onSuccess(JSONObject jsonResult) {
                        loadCategories();
                    }

                    @Override
                    protected String getErrorMessage() {
                        // TODO Auto-generated method stub
                        return "Error moving category";
                    }
                });
            }
        }

        @Override
        protected void onDragFail(DNDEvent event) {
            scrollSupport.stop();
        }

        @Override
        protected void onDragLeave(DNDEvent event) {
            scrollSupport.stop();
        }
    }

    private class CatalogCategoryAdminPanelDragSource extends TreePanelDragSource {
        public CatalogCategoryAdminPanelDragSource(TreePanel<AnalysisGroupTreeModel> tree) {
            super(tree);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void onDragStart(DNDEvent event) {
            Element dragStartElement = (Element)event.getDragEvent().getStartElement();

            TreeNode node = tree.findNode(dragStartElement);
            if (node == null) {
                event.setCancelled(true);
                return;
            }

            if (!tree.getView().isSelectableTarget(node.getModel(), dragStartElement)) {
                event.setCancelled(true);
                return;
            }

            AnalysisGroup category = (AnalysisGroup)categoryPanel.getSelectionModel().getSelectedItem();

            if (category != null) {
                event.setData(category);
                event.getStatus().update(category.getDisplayName());
            } else {
                event.setCancelled(true);
                event.getStatus().setStatus(false);
            }
        }

        @Override
        public void onDragDrop(DNDEvent e) {
            // do nothing intentionally
        }
    }
}
