package org.iplantc.de.client.notifications.views.cells;

import org.iplantc.de.client.notifications.models.NotificationMessage;
import org.iplantc.de.client.utils.NotificationHelper;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * 
 * A cell to render notification messages in a Grid
 * 
 * @author sriram
 * 
 */
public class NotificationMessageCell extends AbstractCell<NotificationMessage> {

    public NotificationMessageCell() {
        super("click");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, NotificationMessage value,
            SafeHtmlBuilder sb) {
        if (value.getContext() != null) {
            sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                    + value.getMessage() + "</div>");
        } else {
            sb.appendHtmlConstant("<div style=\"white-space:pre-wrap;\">" + value.getMessage()
                    + "</div>");
        }

    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
            NotificationMessage value, NativeEvent event, ValueUpdater<NotificationMessage> valueUpdater) {
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if ("click".equals(event.getType())) {
            if (value.getContext() != null) {
                NotificationHelper.getInstance().view(value);
            }
        }
    }

}
