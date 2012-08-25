package org.iplantc.de.client.gxt3.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisFeedback;
import org.iplantc.de.client.images.Icons;
import org.iplantc.de.client.images.Resources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

public class AnalysisRatingCell extends AbstractCell<AnalysisFeedback> {
    private static final String GOLD_STAR_RATING_STYLE = "apps_rating_gold_button"; //$NON-NLS-1$
    private static final String WHITE_STAR_RATING_STYLE = "apps_rating_white_button"; //$NON-NLS-1$
    
    private static final SafeHtml GOLD_RATING_BUTTON = makeImage(Resources.ICONS.starGold());
    private static final SafeHtml RED_RATING_BUTTON = makeImage(Resources.ICONS.starRed());
    private static final SafeHtml WHITE_RATING_BUTTON = makeImage(Resources.ICONS.starWhite());
    private static final SafeHtml DELETE_RATING_BUTTON = makeImage(Resources.ICONS.deleteRating());
    private static final SafeHtml DELETE_RATING_BUTTON_HOVER = makeImage(Resources.ICONS
            .deleteRatingHover());

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        /**
         * The template for this Cell, which includes styles and a value.
         * 
         * @param styles the styles to include in the style attribute of the div
         * @param value the safe value. Since the value type is {@link SafeHtml}, it will not be escaped
         *            before including it in the template. Alternatively, you could make the value type
         *            String, in which case the value would be escaped.
         * @return a {@link SafeHtml} instance
         */
        @SafeHtmlTemplates.Template("<span name=\"{0}\" style=\"{1}\">{2}</span>")
        SafeHtml cell(String name, SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    public AnalysisRatingCell(final Icons icons) {
        // Sink browser events
        // TODO JDS May need to remove sink of KEYDOWN if we find it unnecessary
        super(CLICK, MOUSEOVER, MOUSEOUT, KEYDOWN);

        Resources.ICONS.add();
    }

    @Override
    public void render(Cell.Context context, AnalysisFeedback value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to cells if the underlying
         * data contains a null, or if the data arrives out of order.
         */
        if (value == null) {
            return;
        }

        SafeStyles imgStyle = SafeStylesUtils.fromTrustedString("");

        // If the value comes from the user, we escape it to avoid XSS attacks.
        // SafeHtml safeValue = SafeHtmlUtils.fromString(value.getAverageRating());

        // Build five rating stars
        SafeHtml ratingStar = templates.cell("Rating1", imgStyle, WHITE_RATING_BUTTON);
        sb.append(ratingStar);
        ratingStar = templates.cell("Rating2", imgStyle, WHITE_RATING_BUTTON);
        sb.append(ratingStar);
        ratingStar = templates.cell("Rating3", imgStyle, WHITE_RATING_BUTTON);
        sb.append(ratingStar);
        ratingStar = templates.cell("Rating4", imgStyle, WHITE_RATING_BUTTON);
        sb.append(ratingStar);
        ratingStar = templates.cell("Rating5", imgStyle, WHITE_RATING_BUTTON);
        sb.append(ratingStar);

        // Determine if user has rated the app, and if so, add the unrate icon/button
        if (value.getUserRating() > 0) {
            // Add unrate icon
            SafeHtml unrateIcon = templates.cell("Unrate", imgStyle, DELETE_RATING_BUTTON);
            sb.append(unrateIcon);
        }

    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, AnalysisFeedback value,
            NativeEvent event, ValueUpdater<AnalysisFeedback> valueUpdater) {
        if (!(context.getKey() instanceof Analysis)) {
            return;
        }
        Analysis model = (Analysis)context.getKey();

        // Handle Click event
        if (((HyperlinkImpl)GWT.create(HyperlinkImpl.class)).handleAsClick(Event.as(event))) {
            // Ignore clicks that occur outside of the outermost element.
            EventTarget eventTarget = event.getEventTarget();
            if (parent.isOrHasChild(Element.as(eventTarget))) {

            }
        }

        // Handle OnMouseOver event
        if (Event.as(event).equals(Event.ONMOUSEOVER)) {
            // Ignore mouse over events that occur outside of the outermost element.
            EventTarget eventTarget = event.getEventTarget();
            if (parent.isOrHasChild(Element.as(eventTarget))) {

            }

            /*
             * for (int i = 0; i <= hoveredRating; i++) { icon = getIcon(pnlParent, i);
             * icon.setStyleName("GOLD_STAR_RATING_STYLE"); } for (int j = i; j < ratings.size(); j++) {
             * icon = getIcon(pnlParent, j); icon.setStyleName("WHITE_STAR_RATING_STYLE"); }
             */
        }

    }

    private static SafeHtml makeImage(ImageResource resource) {
        AbstractImagePrototype proto = AbstractImagePrototype.create(resource);

        return proto.getSafeHtml();
    }

}
