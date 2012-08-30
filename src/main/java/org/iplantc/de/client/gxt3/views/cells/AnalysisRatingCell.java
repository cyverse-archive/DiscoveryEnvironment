package org.iplantc.de.client.gxt3.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisFeedback;
import org.iplantc.de.client.images.Icons;
import org.iplantc.de.client.services.TemplateServiceFacade;
import org.iplantc.de.client.views.dialogs.AppCommentDialog;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AnalysisRatingCell extends AbstractCell<Analysis> {
    private static final String GOLD_STAR_RATING_STYLE = "apps_rating_gold_button"; //$NON-NLS-1$
    private static final String WHITE_STAR_RATING_STYLE = "apps_rating_white_button"; //$NON-NLS-1$
    private static final String RED_STAR_RATING_STYLE = "apps_rating_red_button"; //$NON-NLS-1$
    private static final String UNRATE_RATING_STYLE = "apps_rating_unrate_button"; //$NON-NLS-1$
    private static final String UNRATE_HOVER_RATING_STYLE = "apps_rating_unrate_button_hover"; //$NON-NLS-1$
    
    private final SafeStyles imgStyle1 = SafeStylesUtils.fromTrustedString("float: left;");
    private final SafeStyles imgStyl2 = SafeStylesUtils.fromTrustedString("float: left; display: none;");

    private final TemplateServiceFacade templateService = new TemplateServiceFacade();

    public static enum RATING_CONSTANT {

        HATE_IT(I18N.DISPLAY.hateIt()), DID_NOT_LIKE_IT(I18N.DISPLAY.didNotLike()), LIKED_IT(
                I18N.DISPLAY.likedIt()), REALLY_LIKED_IT(I18N.DISPLAY.reallyLikedIt()), LOVED_IT(
                I18N.DISPLAY.lovedIt());

        private String displayText;

        private RATING_CONSTANT(String displaytext) {
            this.displayText = displaytext;
        }

        /**
         * Returns a string that identifies the RATING_CONSTANT.
         * 
         * @return
         */
        public String getTypeString() {
            return toString().toLowerCase();
        }

        /**
         * Null-safe and case insensitive variant of valueOf(String)
         * 
         * @param typeString name of an EXECUTION_STATUS constant
         * @return
         */
        public static RATING_CONSTANT fromTypeString(String typeString) {
            if (typeString == null || typeString.isEmpty()) {
                return null;
            }

            return valueOf(typeString.toUpperCase());
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<div  name=\"{0}\" title=\"{1}\" class=\"{2}\" style=\"{3}\"></div>")
        SafeHtml cell(String name, String toolTip, String cssClass, SafeStyles styles);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    private final List<String> ratings;

    public AnalysisRatingCell(final Icons icons) {
        super(CLICK, MOUSEOVER, MOUSEOUT);

        ratings = new ArrayList<String>();
        ratings.add(0, RATING_CONSTANT.HATE_IT.displayText);
        ratings.add(1, RATING_CONSTANT.DID_NOT_LIKE_IT.displayText);
        ratings.add(2, RATING_CONSTANT.LIKED_IT.displayText);
        ratings.add(3, RATING_CONSTANT.REALLY_LIKED_IT.displayText);
        ratings.add(4, RATING_CONSTANT.LOVED_IT.displayText);

    }

    @Override
    public void render(Cell.Context context, Analysis value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        int rating = (int)((value.getRating().getUserRating() != 0) ? value.getRating().getUserRating()
                : Math.floor(value.getRating()
                .getAverageRating()));
        // Build five rating stars
        for (int i = 0; i < ratings.size(); i++) {
            if (i < rating) {
                if (value.getRating().getUserRating() != 0) {
                    sb.append(templates.cell("Rating-" + i, ratings.get(i), GOLD_STAR_RATING_STYLE,
                            imgStyle1));
                } else {
                    sb.append(templates.cell("Rating-" + i, ratings.get(i), RED_STAR_RATING_STYLE,
                            imgStyle1));
                }
            } else {
                sb.append(templates.cell("Rating-" + i, ratings.get(i), WHITE_STAR_RATING_STYLE,
                        imgStyle1));
            }
        }

        // Determine if user has rated the app, and if so, add the unrate icon/button
        if (value.getRating().getUserRating() > 0) {
            // Add unrate icon
            sb.append(templates.cell("Unrate", "Unrate", UNRATE_RATING_STYLE, imgStyle1));
        } else {
            sb.append(templates.cell("Unrate", "Unrate", UNRATE_RATING_STYLE, imgStyl2));
        }

    }

    @Override
    public boolean handlesSelection() {
        return true;
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Analysis value, NativeEvent event,
            ValueUpdater<Analysis> valueUpdater) {
        if (value == null) {
            return;
        }

        if (parent.isOrHasChild(Element.as(event.getEventTarget()))) {
            Element eventTarget = Element.as(event.getEventTarget());

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    onRatingClicked(parent, eventTarget, value);
                    break;
                case Event.ONMOUSEOVER:
                    onRatingMouseOver(parent, eventTarget);
                    break;
                case Event.ONMOUSEOUT:
                    resetRatingStarColors(parent, value);
                    break;
                default:
                    // TODO JDS Handle this case
                    break;
            }
        }
    }

    private void resetRatingStarColors(Element parent, Analysis value) {
        int rating = (int)((value.getRating().getUserRating() != 0) ? value.getRating().getUserRating()
                : Math.floor(value.getRating().getAverageRating()));

        for (int i = 0; i < parent.getChildCount(); i++) {
            Element child = Element.as(parent.getChild(i));
            // Reset rating stars
            if (child.getAttribute("name").startsWith("Rating")) {
                if (i < rating) {
                    if (value.getRating().getUserRating() != 0) {
                        child.setClassName(GOLD_STAR_RATING_STYLE);
                    } else {
                        child.setClassName(RED_STAR_RATING_STYLE);
                    }
                } else {
                    child.setClassName(WHITE_STAR_RATING_STYLE);
                }
            } else if (child.getAttribute("name").equalsIgnoreCase("unrate")) {
                // Show/Hide unrate button
                if (value.getRating().getUserRating() != 0) {
                    child.getStyle().setDisplay(Display.BLOCK);
                    child.setClassName(UNRATE_RATING_STYLE);
                } else {
                    // if there is no rating, hide the cell.
                    child.getStyle().setDisplay(Display.NONE);
                }
            }
        }
    }

    private void onRatingMouseOver(Element parent, Element eventTarget) {
        boolean setWhiteStar = false;
        for (int i = 0; i < parent.getChildCount(); i++) {
            Element child = Element.as(parent.getChild(i));
            if (child.getAttribute("name").startsWith("Rating")) {
                if (setWhiteStar) {
                    child.setClassName(WHITE_STAR_RATING_STYLE);
                } else {
                    child.setClassName(GOLD_STAR_RATING_STYLE);
                }
                if (child.getAttribute("name").equals(eventTarget.getAttribute("name"))) {
                    setWhiteStar = true;
                }
            }
        }
        if (eventTarget.getAttribute("name").equalsIgnoreCase("unrate")) {
            eventTarget.setClassName(UNRATE_HOVER_RATING_STYLE);
        }
    }

    private void onRatingClicked(final Element parent, Element eventTarget, final Analysis value) {
        if (eventTarget.getAttribute("name").startsWith("Rating")) {
            String[] g = eventTarget.getAttribute("name").split("-");
            final int score = Integer.parseInt(g[1]) + 1;

            // populate dialog via an async call if previous comment ID exists, otherwise show blank dlg
            final AppCommentDialog dlg = new AppCommentDialog(value.getName());
            Long commentId = value.getRating().getCommentId();
            if (commentId == null) {
                dlg.unmaskDialog();
            } else {
                ConfluenceServiceFacade.getInstance().getComment(commentId, new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String comment) {
                        dlg.setText(comment);
                        dlg.unmaskDialog();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        // ErrorHandler.post(e.getMessage(), e);
                        dlg.unmaskDialog();
                    }
                });
            }
            Command onConfirm = new Command() {
                @Override
                public void execute() {
                    persistRating(value, score, dlg.getComment(), parent);
                }
            };
            dlg.setCommand(onConfirm);
            dlg.show();
        } else if (eventTarget.getAttribute("name").equalsIgnoreCase("unrate")) {
            // Hide unrate button
            eventTarget.getStyle().setDisplay(Display.NONE);
            onUnrate(parent, value);
        }

    }

    private void onUnrate(final Element parent, final Analysis value) {
        Long commentId = null;
        try {
            AnalysisFeedback feedback = value.getRating();
            if (feedback != null) {
                commentId = feedback.getCommentId();
            }
        } catch (NumberFormatException e) {
            // comment id empty or not a number, leave it null and proceed
        }

        templateService.deleteRating(value.getId(), parsePageName(value.getWikiUrl()), commentId,
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        value.getRating().setUserRating(0);
                        value.getRating().setCommentId(0);

                        if (result == null || result.isEmpty()) {
                            value.getRating().setAverageRating(0);
                        } else {
                            JSONObject jsonObj = JsonUtil.getObject(result);
                            if (jsonObj != null) {
                                double newAverage = JsonUtil.getNumber(jsonObj, "avg").doubleValue(); //$NON-NLS-1$
                                value.getRating().setAverageRating(newAverage);
                            }
                        }

                        resetRatingStarColors(parent, value);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught.getMessage(), caught);
                    }
                });

    }

    /** saves a rating to the database and the wiki page */
    private void persistRating(final Analysis value, final int score, String comment,
            final Element parent) {

        AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                AnalysisFeedback userFeedback = value.getRating();

                try {
                    userFeedback.setCommentId(Long.valueOf(result));
                } catch (NumberFormatException e) {
                    // no comment id, do nothing
                }

                userFeedback.setUserRating(score);

                resetRatingStarColors(parent, value);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(org.iplantc.de.client.I18N.ERROR.confluenceError(), caught);
            }
        };

        Long commentId = value.getRating().getCommentId();
        if ((commentId == null) || (commentId == 0)) {
            templateService.rateAnalysis(value.getId(), score, parsePageName(value.getWikiUrl()),
                    comment, value.getIntegratorEmail(), callback);
        } else {
            templateService.updateRating(value.getId(), score, parsePageName(value.getWikiUrl()),
                    commentId, comment, value.getIntegratorEmail(), callback);
        }
    }

    private String parsePageName(String url) {
        String name = null;
        if (url != null && !url.isEmpty()) {
            String[] tokens = url.split("/"); //$NON-NLS-1$
            name = URL.decode(tokens[tokens.length - 1]);
        }

        return name;
    }

}
