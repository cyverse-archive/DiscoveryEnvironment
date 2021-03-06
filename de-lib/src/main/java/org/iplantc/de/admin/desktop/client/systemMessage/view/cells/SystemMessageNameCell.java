package org.iplantc.de.admin.desktop.client.systemMessage.view.cells;

import org.iplantc.de.admin.desktop.client.systemMessage.SystemMessageView;
import org.iplantc.de.client.models.systemMessages.SystemMessage;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author jstroot
 */
public class SystemMessageNameCell extends AbstractCell<SystemMessage> {

    public interface SystemMessageNameCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "smName";

        void render(SafeHtmlBuilder sb, SystemMessage value);
    }

    private final SystemMessageView view;
    private final SystemMessageNameCellAppearance appearance = GWT.create(SystemMessageNameCellAppearance.class);

    public SystemMessageNameCell(final SystemMessageView view) {
        super(CLICK);
        this.view = view;
    }

    @Override
    public void render(Cell.Context arg0, SystemMessage value, SafeHtmlBuilder sb) {
        appearance.render(sb, value);
    }

    @Override
    public void onBrowserEvent(Cell.Context context,
                               Element parent,
                               SystemMessage value,
                               NativeEvent event,
                               ValueUpdater<SystemMessage> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)
                && (Event.as(event).getTypeInt() == Event.ONCLICK)
                && eventTarget.getAttribute("name").equalsIgnoreCase(appearance.CLICKABLE_ELEMENT_NAME)) {
            view.editSystemMessage(value);
        }
    }
}
