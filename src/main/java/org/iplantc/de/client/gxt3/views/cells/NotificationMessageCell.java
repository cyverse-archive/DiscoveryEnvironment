package org.iplantc.de.client.gxt3.views.cells;

import org.iplantc.de.client.gxt3.model.NotificationMessage;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;

public class NotificationMessageCell extends AbstractCell<NotificationMessage> {

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, NotificationMessage value,
            SafeHtmlBuilder sb) {

        String message = value.getMessage();
        // TODO: implement notification context
        String con = null;
        HTML html = new HTML(message, true);
        sb.append(SafeHtmlUtils.fromTrustedString(html.getHTML()));
        if (con != null && !con.isEmpty()) {
            // html.setStyleName("notification_context");
            // html.addClickHandler(new ClickHandler() {
            //
            // @Override
            // public void onClick(ClickEvent event) {
            // // NotificationHelper.getInstance().view(value);
            // }
            // });
        }

    }
}
