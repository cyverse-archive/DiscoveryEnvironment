package org.iplantc.de.client.views.panels;

import org.iplantc.core.uiapplications.client.events.AnalysisGroupCountUpdateEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisGroupCountUpdateEvent.AnalysisGroupType;
import org.iplantc.core.uiapplications.client.events.AnalysisGroupCountUpdateEventHandler;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.views.windows.DECatalogWindow;

/**
 * 
 * A panel that displays a list of categories
 * 
 * @author sriram
 * 
 */
public class CatalogCategoryPanel extends AbstractCatalogCategoryPanel {

    /**
     * An util method to force deselect a category programmatically
     * 
     */
    public void deSelectCurrentCategory() {
        categoryPanel.getSelectionModel().deselectAll();
    }

    private String getCategoryNameWorkspace() {
        return DECatalogWindow.WORKSPACE;
    }

    private String getCategoryNameFav() {
        return DECatalogWindow.FAVORITES;
    }

    private String getCategoryNameDev() {
        return DECatalogWindow.APPLICATIONS_UNDER_DEVLOPMENT;
    }

    private String getBetaCategoryById() {
        return DECatalogWindow.BETA_GROUP_ID;
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        EventBus.getInstance().addHandler(AnalysisGroupCountUpdateEvent.TYPE,
                new AnalysisGroupCountUpdateEventHandler() {
                    @Override
                    public void onGroupCountUpdate(AnalysisGroupCountUpdateEvent event) {
                        updadteGroupDisplayName(event);
                    }
                });
    }

    /**
     * Called on favorites and deletion activity. Update group display name when the count in that group
     * changes. Display Name = <Group Name> + <(count)>. To update display name just update the count for
     * the group.
     * 
     * @param event
     */
    private void updadteGroupDisplayName(AnalysisGroupCountUpdateEvent event) {
        AnalysisGroup groupCurrent = (AnalysisGroup)categoryPanel.getSelectionModel().getSelectedItem();
        AnalysisGroup groupDevelopment = getGroupBy(AnalysisGroup.NAME, getCategoryNameDev());

        if (event.getAnalysisGroupType() == AnalysisGroupType.BETA) {
            // Increment the Beta category count
            AnalysisGroup groupBeta = getGroupBy(AnalysisGroup.ID, getBetaCategoryById());
            updateCountAndView(groupCurrent, groupBeta, true);

            // Always update the Dev category count here, since an App was moved from Dev to Beta.
            updateCountAndView(groupCurrent, groupDevelopment, event.isIncrement());

        } else if (event.getAnalysisGroupType() == AnalysisGroupType.FAVORITES) {
            // Update the Favorites category count
            AnalysisGroup groupFavorites = getGroupBy(AnalysisGroup.NAME, getCategoryNameFav());
            updateCountAndView(groupCurrent, groupFavorites, event.isIncrement());

            // Force any selected category to refresh for Favorite events.
            if (groupCurrent != null && groupFavorites != null) {
                // If the selected group is already favorites, then this event was already fired.
                if (!groupFavorites.getId().equals(groupCurrent.getId())) {
                    fireCategorySelectedEvent(groupCurrent);
                }
            }
        } else {
            // This event is only for a Dev. category count update.
            updateCountAndView(groupCurrent, groupDevelopment, event.isIncrement());
        }

        // Always update the Workspace group count for all events.
        AnalysisGroup groupWorkspace = getGroupBy(AnalysisGroup.NAME, getCategoryNameWorkspace());
        updateCountAndView(groupCurrent, groupWorkspace, event.isIncrement());

    }

    /**
     * Updates the group count for the given groupUpdate. If groupUpdate is the same group as
     * groupCurrent, then an AnalysisCategorySelectedEvent is fired for groupCurrent to refresh the view.
     * 
     * @param groupCurrent
     * @param groupUpdate
     */
    private void updateCountAndView(AnalysisGroup groupCurrent, AnalysisGroup groupUpdate,
            boolean increment) {
        if (groupUpdate != null) {
            // update the group count
            updateStoreModel(groupUpdate, increment);

            if (groupCurrent != null && groupUpdate.getId().equals(groupCurrent.getId())) {
                // refresh the current view
                fireCategorySelectedEvent(groupCurrent);
            }
        }
    }

    private AnalysisGroup getGroupBy(String property, String value) {
        return (AnalysisGroup)categoryPanel.getStore().findModel(property, value);
    }

    private void updateStoreModel(AnalysisGroup group, boolean increment) {
        updateCount(group, increment);
        categoryPanel.getStore().update(group);
    }

    private void updateCount(AnalysisGroup group, boolean increment) {
        int count = group.getCount();

        if (increment) {
            group.setCount(count + 1);
        } else {
            group.setCount(Math.max(count - 1, 0));
        }
    }
}
