package org.iplantc.admin.belphegor.client.gxt3.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.admin.belphegor.client.gxt3.views.cells.AvgAnalysisUserRatingCell;
import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisFeedback;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisProperties;
import org.iplantc.core.uiapplications.client.views.cells.HyperlinkCell;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class BelphegorAnalysisColumnModel extends ColumnModel<Analysis> {

    public BelphegorAnalysisColumnModel(final EventBus eventBus,
            final CommonAppDisplayStrings displayStrings) {
        super(createColumnConfigList(eventBus, displayStrings));
    }

    public static List<ColumnConfig<Analysis, ?>> createColumnConfigList(final EventBus eventBus,
            final CommonAppDisplayStrings displayStrings) {
        AnalysisProperties props = GWT.create(AnalysisProperties.class);
        List<ColumnConfig<Analysis, ?>> list = new ArrayList<ColumnConfig<Analysis, ?>>();

        ColumnConfig<Analysis, Analysis> name = new ColumnConfig<Analysis, Analysis>(
                new IdentityValueProvider<Analysis>(), 180, I18N.DISPLAY.name());
        ColumnConfig<Analysis, String> integrator = new ColumnConfig<Analysis, String>(
                props.integratorName(), 130, I18N.DISPLAY.integratedby());
        ColumnConfig<Analysis, AnalysisFeedback> rating = new ColumnConfig<Analysis, AnalysisFeedback>(
                props.rating(), 40, "Average User Rating"); //$NON-NLS-1$

        name.setResizable(true);
        rating.setResizable(false);

        name.setCell(new HyperlinkCell(eventBus, displayStrings));
        rating.setCell(new AvgAnalysisUserRatingCell());

        rating.setAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        list.add(name);
        list.add(integrator);
        list.add(rating);
        return list;
    }

}