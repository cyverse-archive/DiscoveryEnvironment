package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.Services;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEvent;
import org.iplantc.admin.belphegor.client.events.CatalogCategoryRefreshEventHandler;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.callbacks.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.impl.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uiapplications.client.store.AnalysisToolGroupStoreWrapper;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.ScrollSupport;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogCategoryAdminPanel extends AbstractCatalogCategoryPanel {
    private final CatalogCategoryToolBar toolBar;
    private HandlerRegistration handlerRefresh;

    public CatalogCategoryAdminPanel(String tag) {
        super(tag);

        toolBar = new CatalogCategoryToolBar();
        toolBar.setMaskingParent(this);

        setTopComponent(toolBar);

        // Select the Beta Category by default.
        selectCategory(ToolIntegrationAdminProperties.getInstance().getDefaultBetaAnalysisGroupId());

        loadCategories();
    }

    private void reloadCategories() {
        setDefaultCategoryId(categoryPanel.getSelectionModel().getSelectedItem().getId());
        loadCategories();
    }

    private void loadCategories() {
        Services.ADMIN_TEMPLATE_SERVICE.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AnalysisToolGroupStoreWrapper wrapper = new AnalysisToolGroupStoreWrapper();
                        wrapper.updateWrapper(result);
                        seed(wrapper.getStore());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                    }
                });
    }

    @Override
    public void seed(TreeStore<AnalysisGroupTreeModel> models) {
        super.seed(models);

        initDragAndDrop();

        toolBar.setCategoryTreePanel(categoryPanel);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        if (handlerRefresh != null) {
            handlerRefresh.removeHandler();
        }

        handlerRefresh = EventBus.getInstance().addHandler(CatalogCategoryRefreshEvent.TYPE,
                new CatalogCategoryRefreshEventHandler() {
                    @Override
                    public void onRefresh(CatalogCategoryRefreshEvent event) {
                        reloadCategories();
                    }
                });
    }

    private void moveAnalysisGroupTreeModel(final AnalysisGroupTreeModel source,
            final AnalysisGroup destination) {
        String srcId = source.getId();
        String destId = destination.getId();

        AdminServiceCallback callback = new AdminServiceCallback() {
            @Override
            protected void onSuccess(JSONObject jsonResult) {
                reloadCategories();
            }

            @Override
            protected String getErrorMessage() {
                if (source instanceof Analysis) {
                    return I18N.ERROR.moveApplicationError(source.getName());
                }

                return I18N.ERROR.moveCategoryError(source.getName());
            }
        };

        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade(this);

        if (source instanceof Analysis) {
            facade.moveApplication(srcId, destId, callback);
        } else {
            facade.moveCategory(srcId, destId, callback);
        }
    }

    private void initDragAndDrop() {
        CatalogCategoryAdminPanelDropTarget target = new CatalogCategoryAdminPanelDropTarget(
                categoryPanel);
        target.setAllowDropOnLeaf(true);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.APPEND);

        new CatalogCategoryAdminPanelDragSource(categoryPanel);
    }

    /**
     * TreePanelDropTarget for drag-n-drop support of moving categories and apps.
     * 
     * @author psarando
     * 
     */
    private class CatalogCategoryAdminPanelDropTarget extends TreePanelDropTarget {
        private ScrollSupport scrollSupport;

        public CatalogCategoryAdminPanelDropTarget(TreePanel<AnalysisGroupTreeModel> tree) {
            super(tree);
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

            // Get our destination category.
            @SuppressWarnings("rawtypes")
            TreeNode node = categoryPanel.findNode(event.getTarget());

            if (node == null) {
                event.setCancelled(true);
                event.getStatus().setStatus(false);
                return;
            }

            AnalysisGroup target = (AnalysisGroup)node.getModel();

            // Check if the source may be dropped into the target.
            AnalysisGroupTreeModel source = (AnalysisGroupTreeModel)event.getData();

            if (source instanceof AnalysisGroup) {
                if (target.isLeaf() && target.getCount() > 0) {
                    // Don't allow a category drop into a category leaf with apps in it.
                    event.setCancelled(true);
                    event.getStatus().setStatus(false);
                }
            } else if (!target.isLeaf()) {
                // Apps can only be dropped into leaf categories.
                event.setCancelled(true);
                event.getStatus().setStatus(false);
            }
        }

        @Override
        public void onDragDrop(DNDEvent event) {
            @SuppressWarnings("rawtypes")
            TreeNode node = categoryPanel.findNode(event.getTarget());
            final AnalysisGroupTreeModel source = (AnalysisGroupTreeModel)event.getData();

            if (node != null && source != null) {
                // get our destination category
                final AnalysisGroup target = (AnalysisGroup)node.getModel();

                // call service to move category
                moveAnalysisGroupTreeModel(source, target);
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

    /**
     * TreePanelDragSource for moving categories.
     * 
     * @author psarando
     * 
     */
    private class CatalogCategoryAdminPanelDragSource extends TreePanelDragSource {
        public CatalogCategoryAdminPanelDragSource(TreePanel<AnalysisGroupTreeModel> tree) {
            super(tree);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void onDragStart(DNDEvent event) {
            // Check if a valid tree node was selected.
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

            // Set the drag source in the event
            AnalysisGroup source = (AnalysisGroup)categoryPanel.getSelectionModel().getSelectedItem();

            if (source != null) {
                event.setData(source);
                event.getStatus().update(source.getDisplayName());
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
