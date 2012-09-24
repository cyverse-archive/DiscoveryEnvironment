/**
 * 
 */
package org.iplantc.de.client.analysis.views.cells;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.de.client.analysis.models.AnalysisParameter;
import org.iplantc.de.client.utils.DataViewContextExecutor;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author sriram
 * 
 */
public class AnalysisParamValueCell extends AbstractCell<AnalysisParameter> {

    public AnalysisParamValueCell() {
        super("click");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, AnalysisParameter value,
            SafeHtmlBuilder sb) {
        String info_type = value.getInfoType();
        // // At present,reference genome info types are not supported by DE viewers
        boolean valid_info_type = !info_type.equalsIgnoreCase("ReferenceGenome")
                && !info_type.equalsIgnoreCase("ReferenceSequence")
                && !info_type.equalsIgnoreCase("ReferenceAnnotation");
        if (value.getType().equalsIgnoreCase("Input") && valid_info_type) {
            sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                    + value.getValue() + "</div>");
        } else {
            sb.appendHtmlConstant("<div style=\"white-space:pre-wrap;\">" + value.getValue() + "</div>");
        }

    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
            AnalysisParameter value, NativeEvent event, ValueUpdater<AnalysisParameter> valueUpdater) {

        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if ("click".equals(event.getType())) {
            String info_type = value.getInfoType();
            boolean valid_info_type = !info_type.equalsIgnoreCase("ReferenceGenome")
                    && !info_type.equalsIgnoreCase("ReferenceSequence")
                    && !info_type.equalsIgnoreCase("ReferenceAnnotation");
            if (value.getInfoType().equalsIgnoreCase("Input") && valid_info_type) {
                List<String> contexts = new ArrayList<String>();
                DataContextBuilder builder = new DataContextBuilder();
                contexts.add(builder.build(value.getValue()));
                DataViewContextExecutor executor = new DataViewContextExecutor();
                executor.execute(contexts);
            }
        }

    }

}
