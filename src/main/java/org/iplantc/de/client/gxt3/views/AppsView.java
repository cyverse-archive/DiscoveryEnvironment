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

    }

    void setPresenter(final Presenter presenter);

    ListStore<Analysis> getListStore();

    TreeStore<AnalysisGroup> getTreeStore();

    void setListLoader(final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader);

    void setTreeLoader(final TreeLoader<AnalysisGroup> treeLoader);

    void setMainPanelHeading(final String name);

    void maskMainPanel(final String loadingMask);

    GridSelectionModel<Analysis> getGridSelectionModel();

}
