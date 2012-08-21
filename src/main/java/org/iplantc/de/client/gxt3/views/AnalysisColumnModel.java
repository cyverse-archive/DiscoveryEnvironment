package org.iplantc.de.client.gxt3.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisProperties;
import org.iplantc.de.client.gxt3.views.cells.AnalysisRatingCell;
import org.iplantc.de.client.gxt3.views.cells.HyperlinkCell;
import org.iplantc.de.client.images.Icons;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class AnalysisColumnModel extends ColumnModel<Analysis> {


    public AnalysisColumnModel(final EventBus eventBus, final CommonAppDisplayStrings displayStrings,
            final Icons icons) {
        super(createColumnConfigList(eventBus, displayStrings, icons));
    }

    public static List<ColumnConfig<Analysis, ?>> createColumnConfigList(final EventBus eventBus,
            final CommonAppDisplayStrings displayStrings, final Icons icons) {
        AnalysisProperties props = GWT.create(AnalysisProperties.class);
        List<ColumnConfig<Analysis, ?>> list = new ArrayList<ColumnConfig<Analysis, ?>>();

        ColumnConfig<Analysis, String> name = new ColumnConfig<Analysis, String>(props.getName(), 180,
                I18N.DISPLAY.name());
        ColumnConfig<Analysis, String> integrator = new ColumnConfig<Analysis, String>(
                props.getIntegratedBy(), 130, I18N.DISPLAY.integratedby());
        ColumnConfig<Analysis, Analysis.AnalysisFeedback> rating = new ColumnConfig<Analysis, Analysis.AnalysisFeedback>(
                props.getRating(),
                105, "Rating"); //$NON-NLS-1$

        // FIXME JDS Implement name and rating cells
        name.setCell(new HyperlinkCell(eventBus, displayStrings));
        rating.setCell(new AnalysisRatingCell(icons));

        list.add(name);
        list.add(integrator);
        list.add(rating);
        return list;
    }

}


