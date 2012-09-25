/**
 * 
 */
package org.iplantc.de.client.analysis.views.cells;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.analysis.models.Analysis;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.WindowConfigFactory;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author sriram
 * 
 */
public class AnalysisAppNameCell extends AbstractCell<Analysis> {

    public AnalysisAppNameCell() {
        super("click");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, Analysis model,
            SafeHtmlBuilder sb) {
        if (model != null && model.getResultFolderId() != null && !model.getResultFolderId().isEmpty()) {
            sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                    + model.getAnalysisName() + "</div>");
        } else {
            sb.appendHtmlConstant("<div style=\"white-space:pre-wrap;\">" + model.getAnalysisName()
                    + "</div>");
        }

    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
            Analysis value, NativeEvent event, ValueUpdater<Analysis> valueUpdater) {
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.wizardTag(), //$NON-NLS-1$
                JsonUtil.getObject(value.getAnalysisId()));

        // Dispatch window display action with this config
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        dispatcher.dispatchAction(Constants.CLIENT.wizardTag());
    }

}
