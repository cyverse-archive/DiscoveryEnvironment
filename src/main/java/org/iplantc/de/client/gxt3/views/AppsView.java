package org.iplantc.de.client.gxt3.views;


import java.util.List;

import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public interface AppsView extends IsWidget {

    public interface Presenter extends org.iplantc.de.client.gxt3.presenter.Presenter {
        void onAnalysisSelected(final Analysis analysis);

        void onAnalysisGroupSelected(final AnalysisGroup ag);

        Analysis getSelectedAnalysis();

        AnalysisGroup getSelectedAnalysisGroup();
    }

    void setPresenter(final Presenter presenter);

    ListStore<Analysis> getListStore();

    TreeStore<AnalysisGroup> getTreeStore();

    void setListLoader(final ListLoader<ListLoadConfig, ListLoadResult<Analysis>> listLoader);

    void setTreeLoader(final TreeLoader<AnalysisGroup> treeLoader);

    void setMainPanelHeading(final String name);

    void maskMainPanel(final String loadingMask);

    void unMaskMainPanel();

    void selectAnalysis(String analysisId);

    void selectAnalysisGroup(String analysisGroupId);

    Analysis getSelectedAnalysis();

    AnalysisGroup getSelectedAnalysisGroup();

    void setAnalyses(List<Analysis> analyses);

}
