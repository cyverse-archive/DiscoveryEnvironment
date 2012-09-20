/**
 * 
 */
package org.iplantc.de.client.gxt3.views.cells;

import java.util.Date;

import org.iplantc.de.client.gxt3.model.NotificationMessage;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * A cell to render notification timestamp formatted in a grid
 * 
 * @author sriram
 * 
 */
public class NotificationMessageTmestampCell extends AbstractCell<NotificationMessage> {

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, NotificationMessage value,
            SafeHtmlBuilder sb) {
        String dateString = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
                .format(new Date(value.getTimestamp()));
        sb.append(SafeHtmlUtils.fromTrustedString(dateString));

    }
}
