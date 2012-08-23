package org.iplantc.de.client.gxt3.views;


import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

public interface AppsView extends IsWidget {

    public interface Presenter {
        void onAnalysisSelected(final Analysis analysis);

        void onAnalysisGroupSelected(final AnalysisGroup ag);

        // XXX JDS Need to verify necessity of these methods. Does the view need them, or something else?
        void selectCategory(String categoryId);
        
        void selectApp(String appID);

        void deSelectCurrentCategory();

        Analysis getSelectedApp();

        AnalysisGroup getSelectedCategory();
    }

    void setPresenter(final Presenter presenter);

    ListStore<Analysis> getListStore();

    TreeStore<AnalysisGroup> getTreeStore();

    void setListLoader(final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader);

    void setTreeLoader(final TreeLoader<AnalysisGroup> treeLoader);

    void setMainPanelHeading(final String name);

    void maskMainPanel(final String loadingMask);

    void unMaskMainPanel();

    GridSelectionModel<Analysis> getGridSelectionModel();


}
