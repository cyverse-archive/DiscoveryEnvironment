package org.iplantc.de.client.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckNodes;

public class CategorySelectionDialog extends Dialog {

    private TreePanel<AnalysisGroupTreeModel> tree;
    private List<AnalysisGroupTreeModel> selectedGroups;

    public CategorySelectionDialog(TreeStore<AnalysisGroupTreeModel> store) {
        initDialog();
        initTree(store);
        selectedGroups = new ArrayList<AnalysisGroupTreeModel>();
        setModal(true);
    }

    private void initDialog() {
        setHeading(I18N.DISPLAY.browse());
        setScrollMode(Scroll.AUTO);
        setSize(400, 450);
        setButtons(OKCANCEL);
        setFrame(true);
        setHideOnButtonClick(true);
    }

    private void initTree(TreeStore<AnalysisGroupTreeModel> store) {
        tree = new TreePanel<AnalysisGroupTreeModel>(store);
        tree.setStyleAttribute("background-color", "#fff");
        tree.setDisplayProperty("name");
        tree.setCheckable(true);
        tree.setAutoLoad(true);
        tree.setCheckStyle(CheckCascade.CHILDREN);
        tree.setCheckNodes(CheckNodes.LEAF);
        tree.addCheckListener(new CheckChangedListener<AnalysisGroupTreeModel>() {
            @Override
            public void checkChanged(CheckChangedEvent<AnalysisGroupTreeModel> event) {
                selectedGroups.clear();
                selectedGroups.addAll(tree.getCheckedSelection());
            }
        });
        tree.addListener(Events.Render, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                tree.expandAll();
                doSelection();

            }

        });
        add(tree);
    }

    private void doSelection() {
        ArrayList<AnalysisGroupTreeModel> items_check = new ArrayList<AnalysisGroupTreeModel>();
        for (AnalysisGroupTreeModel item : selectedGroups) {
            for (AnalysisGroupTreeModel agtm : tree.getStore().getAllItems()) {
                if (item.getId().equals(agtm.getId())) {
                    items_check.add(agtm);
                }
            }
        }
        tree.setCheckedSelection(items_check);
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
        if (tree != null) {
            tree.setHeight(height);
        }
    }

    /**
     * @return the selectedGroups
     */
    public List<AnalysisGroupTreeModel> getSelectedGroups() {
        return selectedGroups;
    }

    public void setSelectedItems(List<AnalysisGroupTreeModel> items) {
        if (items != null) {
            selectedGroups = items;
        }
    }

}
