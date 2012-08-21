package org.iplantc.de.client.gxt3.views.cells;

import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.events.UserEvent;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

public class HyperlinkCell extends AbstractCell<String> {

    private final EventBus eventBus;
    private final CommonAppDisplayStrings displayStrings;

    public HyperlinkCell(final EventBus eventBus, final CommonAppDisplayStrings displayStrings) {
        this.eventBus = eventBus;
        this.displayStrings = displayStrings;
    }

    @Override
    public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
        String name = null;
        if (!(context.getKey() instanceof Analysis)) {
            return;
        }
        Analysis model = (Analysis)context.getKey();

        if (model.isFavorite()) {
            // FIXME JDS Replace hard-coded image path with something more safe
            name = "<img src='./images/fav.png'/>&nbsp;" + model.getName(); //$NON-NLS-1$
        } else {
            name = model.getName();
        }

        if (!model.isDisabled()) {
            Hyperlink link = new Hyperlink(name, "");
            link.setStyleName("analysis_name"); //$NON-NLS-1$
            link.setWidth(Integer.toString(model.getName().length()));

            sb.append(SafeHtmlUtils.fromString(link.toString()));
        } else {
            // FIXME JDS Replace hard-coded image path with something more safe
            name = "<img title ='" //$NON-NLS-1$
                    + displayStrings.appUnavailable()
                    + "' src='./images/exclamation.png'/>&nbsp;" + name; //$NON-NLS-1$

            sb.append(SafeHtmlUtils.fromString(name));
        }
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, String value, NativeEvent event,
            ValueUpdater<String> valueUpdater) {
        if (!(context.getKey() instanceof Analysis)) {
            return;
        }
        Analysis model = (Analysis)context.getKey();
        if (((HyperlinkImpl)GWT.create(HyperlinkImpl.class)).handleAsClick(Event.as(event))) {
            event.preventDefault();
            UserEvent e = new UserEvent(Constants.CLIENT.windowTag(), model.getId());
            eventBus.fireEvent(e);
        }
    }
}
