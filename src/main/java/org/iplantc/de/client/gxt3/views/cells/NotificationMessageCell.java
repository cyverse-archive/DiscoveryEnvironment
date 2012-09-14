package org.iplantc.de.client.gxt3.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import java.util.Arrays;
import java.util.HashSet;

import org.iplantc.de.client.gxt3.model.NotificationMessage;
import org.iplantc.de.client.utils.NotificationHelper;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.cell.core.client.LabelProviderSafeHtmlCell;
import com.sencha.gxt.data.shared.StringLabelProvider;

public class NotificationMessageCell extends LabelProviderSafeHtmlCell<NotificationMessage> {

    public NotificationMessageCell() {
        super(new MessageLabelProvider(), new HashSet<String>(Arrays.asList(CLICK, MOUSEOUT, MOUSEOVER)));
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, NotificationMessage value,
            NativeEvent event, ValueUpdater<NotificationMessage> valueUpdater) {
        if (value == null) {
            return;
        }
        if (value.getContext() == null) {
            return;
        }

        if (parent.isOrHasChild(Element.as(event.getEventTarget()))) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    NotificationHelper.getInstance().view(value);
                    break;
            }
        }
    }
}

final class MessageLabelProvider extends StringLabelProvider<NotificationMessage> {
    @Override
    public String getLabel(NotificationMessage msg) {
        return msg.getMessage();
    }
}
