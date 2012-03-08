package org.iplantc.de.client.views.panels;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEvent;
import org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEventHandler;
import org.iplantc.de.client.models.ClientDataModel;
import org.iplantc.de.client.services.DiskResourceServiceCallback;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.ScrollSupport;
import com.extjs.gxt.ui.client.dnd.StatusProxy;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A tree panel to display hierarchical user irods directory structure
 * 
 * @author sriram
 * 
 */
public class DataNavigationPanel extends AbstractDataPanel {
    private TreePanel<Folder> pnlTree;
    private boolean enableDragAndDrop = true;
    private final DataNavToolBar toolBar;
    private final Mode mode;
    private Component maskingParent;
    private ClientDataModel model;

    private TreePanelDragSource dndSource;

    /**
     * 
     * 
     */
    public DataNavigationPanel(final String tag, Mode mode) {
        super(tag);
        this.mode = mode;
        toolBar = new DataNavToolBar(tag, mode);
        setTopComponent(toolBar);
    }

    /**
     * Describes the view/edit mode of the toolbar that will control what buttons are displayed.
     * 
     * How the widget treats the enum values:
     * <dl>
     * <dt>VIEW</dt>
     * <dd>Informs this widget to display only view-mode buttons: Up.</dd>
     * <dt>EDIT</dt>
     * <dd>Informs this widget to display view and edit-mode buttons: New Folder & Import.</dd>
     * </dl>
     * 
     * @author Paul
     * 
     */
    public static enum Mode {
        /**
         * Only view-mode toolbar buttons (Up button) will be displayed.
         */
        VIEW,
        /**
         * Include edit-mode buttons (New Folder and Import) in the toolbar.
         */
        EDIT
    }

    /**
     * Enables Drag-and-Drop in the navigation panel. Must be set before seed is called.
     */
    public void enableDragAndDrop() {
        enableDragAndDrop = true;
    }

    /**
     * Disables Drag-and-Drop in the navigation panel. Must be set before seed is called.
     */
    public void disableDragAndDrop() {
        enableDragAndDrop = false;
    }

    private void initTreeIcons() {
        pnlTree.setIconProvider(new ModelIconProvider<Folder>() {
            @Override
            public AbstractImagePrototype getIcon(Folder model) {
                return (pnlTree.isExpanded(model)) ? pnlTree.getStyle().getNodeOpenIcon() : pnlTree
                        .getStyle().getNodeCloseIcon();
            }
        });
    }

    public Folder getSelectedItem() {
        if (pnlTree == null) {
            return null;
        }

        return pnlTree.getSelectionModel().getSelectedItem();
    }

    public String getRootFolderId() {
        if (pnlTree == null) {
            return null;
        }

        return pnlTree.getStore().getRootItems().get(0).getId();
    }

    private void initTreeListeners(Listener<BaseEvent> selctionChangeListener) {
        pnlTree.getSelectionModel().addListener(Events.SelectionChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                Folder selectedFolder = getSelectedItem();
                EventBus eventbus = EventBus.getInstance();
                DiskResourceSelectedEvent event = new DiskResourceSelectedEvent(selectedFolder, tag);
                eventbus.fireEvent(event);
            }
        });

        if (selctionChangeListener != null) {
            pnlTree.getSelectionModel().addListener(Events.SelectionChange, selctionChangeListener);
        }
    }

    private void initDragAndDrop() {
        TreePanelDropTargetImpl target = new TreePanelDropTargetImpl(pnlTree);
        target.setAllowDropOnLeaf(true);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.APPEND);

        dndSource = new TreePanelDragSourceImpl(pnlTree);
    }

    private void initTreePanel(TreeStore<Folder> store, Listener<BaseEvent> selctionChangeListener) {
        pnlTree = new TreePanel<Folder>(store);
        pnlTree.setDisplayProperty("name"); //$NON-NLS-1$
        pnlTree.setSelectionModel(new NavigationPanelSelectionModel());
        pnlTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initTreeIcons();
        initTreeListeners(selctionChangeListener);

        if (enableDragAndDrop) {
            initDragAndDrop();
        }
    }

    private void setModePrefernces() {
        toolBar.setSelectionModel(pnlTree.getSelectionModel());
        toolBar.setParentFolderId(getRootFolderId());
    }

    @Override
    protected void registerHandlers() {
        super.registerHandlers();
        EventBus eventbus = EventBus.getInstance();

        handlers.add(eventbus.addHandler(DiskResourceSelectedEvent.TYPE,
                new DiskResourceSelectedEventHandlerImpl()));
    }

    @Override
    protected void compose() {
        if (pnlTree != null) {
            add(pnlTree);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setHeading() {
        setHeaderVisible(false);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);

        resizeContents(getInnerWidth(), getInnerHeight());
    }

    @Override
    protected void onAfterLayout() {
        super.onAfterLayout();

        resizeContents(getInnerWidth(), getInnerHeight());
    }

    /**
     * Resizes this panel's inner tree panel.
     * 
     * @param width
     * @param height
     */
    private void resizeContents(int width, int height) {
        if (pnlTree != null) {
            pnlTree.setHeight(height);
        }
    }

    public void seed(final String lblRoot, final ClientDataModel model) {
        seed(lblRoot, model, null);
    }

    public void seed(final String lblRoot, final ClientDataModel model,
            Listener<BaseEvent> selctionChangeListener) {
        if (model != null) {
            this.model = model;
            initTreePanel(model.getHeirarchy(), selctionChangeListener);
            setModePrefernces();

            compose();
            layout();
        }
    }

    public Folder findFolder(final String id) {
        Folder ret = null; // assume failure

        // did we get a valid id?
        if (pnlTree != null && id != null) {
            TreeStore<Folder> store = pnlTree.getStore();

            // attempt to find the desired item
            for (Folder resource : store.getAllItems()) {
                if (resource.getId().equals(id)) {
                    // success
                    ret = resource;
                    break;
                }
            }
        }

        return ret;
    }

    public boolean selectFolder(final String path) {
        Folder target = findFolder(path);

        if (target == null) {
            // the target is not yet loaded into the navigation tree.
            return lazyLoadPath(path);
        }

        if (pnlTree != null) {
            TreePanelSelectionModel<Folder> modelSelection = pnlTree.getSelectionModel();

            // get our currently selected folder
            Folder selected = modelSelection.getSelectedItem();

            if (selected == null || !selected.getId().equals(target.getId())) {
                pnlTree.setExpanded(target, true);
                pnlTree.scrollIntoView(target);

                modelSelection.select(target, false);

                layout();
            }

            return true;
        }

        return false;
    }

    /**
     * Expand the given folder in the panel, without also selecting that target.
     * 
     * @param target
     */
    public void expandFolder(final Folder target) {
        if (pnlTree != null && target != null) {
            Folder match = findFolder(target.getId());
            pnlTree.setExpanded(match, true);

            layout();
        }
    }

    private class TreePanelDropTargetImpl extends TreePanelDropTarget {
        private ScrollSupport scrollSupport;

        public TreePanelDropTargetImpl(TreePanel<Folder> tree) {
            super(tree);
        }

        @Override
        public void onDragEnter(DNDEvent e) {
            if (isAutoScroll()) {
                if (scrollSupport == null) {
                    El scroll = getScrollElementId() != null ? new El(
                            DOM.getElementById(getScrollElementId())) : tree.el();
                    scrollSupport = new ScrollSupport(scroll);
                } else if (scrollSupport.getScrollElement() == null) {
                    El scroll = getScrollElementId() != null ? new El(
                            DOM.getElementById(getScrollElementId())) : tree.el();
                    scrollSupport.setScrollElement(scroll);
                }
                scrollSupport.start();
            }
        }

        @Override
        protected void onDragCancelled(DNDEvent event) {
            scrollSupport.stop();
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onDragMove(DNDEvent event) {
            super.onDragMove(event);

            // super may have cancelled the event
            if (event.isCancelled()) {
                return;
            }

            StatusProxy eventStatus = event.getStatus();
            List<DiskResource> dragSources = (List<DiskResource>)event.getData();

            // Reset the status text, in case a previous onDragMove set it to the permission error text.
            eventStatus.update(Format.substitute(dndSource.getStatusText(), dragSources.size()));

            @SuppressWarnings("rawtypes")
            TreeNode node = pnlTree.findNode(event.getTarget());

            if (node == null) {
                event.setCancelled(true);
                eventStatus.setStatus(false);
                return;
            }

            // get our dest folder
            DiskResource target = (DiskResource)node.getModel();

            if (!DataUtils.isMovable(dragSources) || !target.getPermissions().isWritable()) {
                eventStatus.setStatus(false);
                eventStatus.update(I18N.DISPLAY.permissionErrorMessage());
                event.setCancelled(true);
                return;
            }

            String destId = target.getId();
            for (DiskResource src : dragSources) {
                // if the target folder is one of the src folders, don't allow a drop there
                String srcId = src.getId();

                if (srcId.equals(destId) || DataUtils.parseParent(srcId).equals(destId)) {
                    event.setCancelled(true);
                    eventStatus.setStatus(false);
                }
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onDragDrop(DNDEvent event) {
            @SuppressWarnings("rawtypes")
            TreeNode node = pnlTree.findNode(event.getTarget());

            if (node != null) {
                // get our dest folder
                DiskResource res = (DiskResource)node.getModel();
                String idDestFolder = res.getId();

                // call service to move files and folders
                FolderServiceFacade facade = new FolderServiceFacade(maskingParent);
                facade.moveDiskResources((List<DiskResource>)event.getData(), idDestFolder);
            }
        }

        @Override
        protected void onDragFail(DNDEvent event) {
            scrollSupport.stop();
        }

        @Override
        protected void onDragLeave(DNDEvent e) {
            scrollSupport.stop();
        }
    }

    private class TreePanelDragSourceImpl extends TreePanelDragSource {
        public TreePanelDragSourceImpl(TreePanel<Folder> tree) {
            super(tree);

            setStatusText(I18N.DISPLAY.dataDragDropStatusText());
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void onDragStart(DNDEvent e) {
            Element dragStartElement = (Element)e.getDragEvent().getStartElement();

            TreeNode node = tree.findNode(dragStartElement);
            if (node == null) {
                e.setCancelled(true);
                return;
            }

            if (!tree.getView().isSelectableTarget(node.getModel(), dragStartElement)) {
                e.setCancelled(true);
                return;
            }

            List items = pnlTree.getSelectionModel().getSelectedItems();

            if (items != null && items.size() > 0) {
                e.setData(items);
                e.getStatus().update(Format.substitute(getStatusText(), items.size()));
            } else {
                e.setCancelled(true);
                e.getStatus().setStatus(false);
            }
        }

        @Override
        public void onDragDrop(DNDEvent e) {
            // do nothing intentionally
        }
    }

    public void renameFolder(String id, String name) {
        Folder folder = findFolder(name);

        if (folder != null) {
            pnlTree.recalculate();
        }
    }

    public void removeTreePanel() {
        remove(pnlTree);
    }

    public void setMaskingParent(Component component) {
        maskingParent = component;

        if (toolBar != null) {
            toolBar.setMaskingParent(maskingParent);
        }
    }

    private class NavigationPanelSelectionModel extends TreePanelSelectionModel<Folder> {
        @Override
        protected void onKeyDown(TreePanelEvent<Folder> e) {
            if (maskingParent.isMasked()) {
                return;
            } else {
                super.onKeyDown(e);
            }

        }

        @Override
        protected void onKeyLeft(TreePanelEvent<Folder> ce) {
            if (maskingParent.isMasked()) {
                return;
            } else {
                super.onKeyLeft(ce);
            }
        }

        @Override
        protected void onKeyRight(TreePanelEvent<Folder> ce) {
            if (maskingParent.isMasked()) {
                return;
            } else {
                super.onKeyRight(ce);
            }
        }

        @Override
        protected void onKeyUp(TreePanelEvent<Folder> e) {
            if (maskingParent.isMasked()) {
                return;
            } else {
                super.onKeyUp(e);
            }
        }

    }

    private class DiskResourceSelectedEventHandlerImpl implements DiskResourceSelectedEventHandler {

        @Override
        public void onSelected(DiskResourceSelectedEvent event) {
            DiskResource dr = event.getResource();
            if (dr instanceof Folder && event.getTag().equals(tag)) {
                selectFolder(dr.getId());
            }

        }

    }

    /**
     * Expands folders in a path that have not already been expanded and their subfolders loaded. At
     * least one folder in the path must already be loaded (such as the root folder) into the Navigation
     * panel's store model. Since subfolders are loaded remotely, the window will remain masked until the
     * async process completes with success or failure.
     * 
     * @param path The full path of the folder to expand and select.
     * 
     * @return true if a valid path could be parsed and a folder was found in it that could be expanded.
     */
    private boolean lazyLoadPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        // the last path in this stack will be selected by the Navigation panel.
        final Stack<String> paths = new Stack<String>();
        paths.push(path);

        // find the first parent folder that is already loaded into the model that can be expanded,
        // pushing all parent folders in between it and the given path onto the stack.
        String parentPath = DataUtils.parseParent(path);
        Folder parent = findFolder(parentPath);

        while (parent == null && !parentPath.isEmpty()) {
            paths.push(parentPath);

            parentPath = DataUtils.parseParent(parentPath);
            parent = findFolder(parentPath);
        }

        if (parent == null) {
            // the entire path was parsed, but no parent folder was found already loaded in the model.
            displayRetrieveFolderError(path);
            return false;
        }

        // start expanding the parent folders in order.
        maskingParent.mask(I18N.DISPLAY.loadingMask());

        AsyncTreeLoaderCallBack callback = new AsyncTreeLoaderCallBack(paths);
        model.setTreeLoaderCallback(callback);

        // expand the first parent found that is already loaded in the model to load its children.
        expandFolder(parent);

        if (pnlTree.isExpanded(parent) || !parent.hasSubFolders()) {
            // AsyncTreeLoaderCallBack will not be called if the parent is already expanded (it has
            // already loaded all of its children) or the parent does not have any subfolders. So the
            // next child to expand could not be found. We'll reload parent to be sure we have its latest
            // info, in case the next child was created by a service.
            reloadFolderThenExpand(parent, path, callback);
        }

        // return true since this method or the AsyncTreeLoaderCallBack will select some folder along the
        // given path
        return true;
    }

    /**
     * Re-loads a folder that may have a subfolder on disk but not in the navigation tree. If the folder
     * is a root/home folder, then the entire Data Window is refreshed and the given path is expanded and
     * selected.
     * 
     * @param target The target folder to refresh.
     * @param path Path to the final folder in the lazy-load chain.
     */
    private void reloadFolderThenExpand(final Folder target, final String path,
            final AsyncTreeLoaderCallBack callback) {
        new FolderServiceFacade().getFolderContents(target.getId(), false,
                new DiskResourceServiceCallback() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject jsonTarget = JsonUtil.getObject(result);
                        Folder freshFolder = new Folder(jsonTarget);

                        // Make sure the target folder with the latest info has subfolders.
                        if (freshFolder.hasSubFolders()) {
                            Folder parent = findFolder(DataUtils.parseParent(target.getId()));

                            if (parent == null) {
                                // The folder we need to refresh is the home folder, so we'll brute-force
                                // a refresh of the whole data window.
                                model.setTreeLoaderCallback(null);

                                ManageDataRefreshEvent event = new ManageDataRefreshEvent(tag, path);
                                EventBus.getInstance().fireEvent(event);
                            } else {
                                // Remove and re-add the target folder with the latest info.
                                model.deleteDiskResource(target.getId());
                                freshFolder = model.createFolder(parent.getId(), jsonTarget);

                                // Continue the lazy-loading process by expanding the target folder.
                                expandFolder(freshFolder);
                            }
                        } else {
                            // Let the lazy-load callback generate the error message for the missing
                            // child folder.
                            callback.onSuccess(Arrays.asList(freshFolder));
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // There was a problem reloading the target folder, so stop lazy-loading and
                        // display the error message.
                        unsetCallbackAndSelectFolder(target.getId());

                        super.onFailure(caught);
                    }

                    @Override
                    protected String getErrorMessageDefault() {
                        return I18N.ERROR.retrieveFolderInfoFailed();
                    }

                    @Override
                    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
                        return getErrorMessageForFolders(code, target.getName());
                    }
                });
    }

    private void unsetCallbackAndSelectFolder(String path) {
        model.setTreeLoaderCallback(null);
        selectFolder(path);
        maskingParent.unmask();
    }

    private void displayRetrieveFolderError(final String path) {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                String errMsgFolderName = I18N.ERROR.folderNotFound(DataUtils.parseNameFromPath(path));
                Exception errFullPath = new Exception(I18N.ERROR.folderNotFound(path));

                ErrorHandler.post(errMsgFolderName, errFullPath);
            }
        });
    }

    /**
     * An AsyncCallback for the ClientDataModel's Tree Loader that will expand the folders in the given
     * paths stack, remotely loading each folder's children one at a time.
     * 
     * @author psarando
     * 
     */
    private final class AsyncTreeLoaderCallBack implements AsyncCallback<List<Folder>> {
        private final Stack<String> paths;

        private AsyncTreeLoaderCallBack(Stack<String> paths) {
            this.paths = paths;
        }

        @Override
        public void onSuccess(List<Folder> result) {
            String path = paths.pop();
            Folder nextFolder = findFolder(path);

            if (nextFolder == null) {
                // the next folder to expand or select was not found, even after the parent's subfolders
                // were loaded, so stop the process here and select the deepest folder we could load.
                unsetCallbackAndSelectFolder(DataUtils.parseParent(path));
                displayRetrieveFolderError(path);

                return;
            }

            if (paths.isEmpty()) {
                // we've reached our destination folder, so select it and end this callback process.
                unsetCallbackAndSelectFolder(nextFolder.getId());
            } else {
                // nextFolder is a parent of the destination folder.
                if (nextFolder.hasSubFolders()) {
                    expandFolder(nextFolder);
                } else {
                    // This AsyncCallback will not be called again if the next folder to expand does not
                    // have any subfolders, so stop the process here and select the deepest folder we
                    // could load.
                    unsetCallbackAndSelectFolder(path);
                    displayRetrieveFolderError(paths.pop());
                }
            }
        }

        @Override
        public void onFailure(Throwable caught) {
            // the onFailure method in the ClientDataModel's proxy will post an error message to the user
            // before this method is called, so just end the callback process here.
            model.setTreeLoaderCallback(null);

            maskingParent.unmask();
        }
    }

}
