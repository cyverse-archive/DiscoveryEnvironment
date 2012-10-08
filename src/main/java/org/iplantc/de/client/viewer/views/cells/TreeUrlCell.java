/**
 * 
 */
package org.iplantc.de.client.viewer.views.cells;

import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.viewer.models.TreeUrl;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author sriram
 * 
 */
public class TreeUrlCell extends AbstractCell<TreeUrl> {

    public TreeUrlCell() {
        super("click");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, TreeUrl model, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<div style=\"cursor:pointer;text-decoration:underline;white-space:pre-wrap;\">"
                + model.getUrl() + "</div>");

    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
            TreeUrl value, NativeEvent event, ValueUpdater<TreeUrl> valueUpdater) {

        if (value == null) {
            return;
        }
        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        WindowUtil.open(value.getUrl(), "width=100,height=100");
    }

}
