/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author sriram
 * 
 */
public interface AnalysesToolbarView extends IsWidget {

    public interface Presenter {
        void onDeleteClicked();

        void onViewParamClicked();

        void onCancelClicked();
    }

    void setDeleteButtonEnabled(boolean enabled);

    void setViewParamButtonEnabled(boolean enabled);

    void setCancelButtonEnabled(boolean enabled);

    void setPresenter(Presenter p);

}
