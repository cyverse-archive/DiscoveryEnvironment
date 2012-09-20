/**
 * 
 */
package org.iplantc.de.client.gxt3.views.cells;

import java.util.Date;

import org.iplantc.de.client.gxt3.model.Analysis;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author sriram
 * 
 */
public class AnalysesTimeStampCell extends AbstractCell<Analysis> {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context,
     * java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
     */
    @Override
    public void render(Context context, Analysis value, SafeHtmlBuilder sb) {
        String dateString = null;
        if (context.getColumn() == 3) {
            dateString = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
                    .format(new Date(value.getStartDate()));
        } else {
            dateString = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
                    .format(new Date(value.getEndDate()));
        }
        sb.append(SafeHtmlUtils.fromTrustedString(dateString));
    }

}
