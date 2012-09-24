package org.iplantc.de.client.analysis.views;

import java.util.List;

import org.iplantc.de.client.analysis.models.Analysis;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * 
 * 
 * @author sriram
 * 
 */
public interface AnalysesView extends IsWidget {
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void onAnalysesSelection(List<Analysis> selectedItems);

    }

    public void setPresenter(final Presenter presenter);

    void setNorthWidget(IsWidget widget);

    public void loadAnalyses(List<Analysis> items);

    public List<Analysis> getSelectedAnalyses();

    public void removeFromStore(List<Analysis> items);

}
