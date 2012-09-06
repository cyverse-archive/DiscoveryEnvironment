/**
 * Sencha GXT 3.0.1 - Sencha for GWT Copyright(c) 2007-2012, Sencha, Inc. licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
package org.iplantc.de.client.gxt3.desktop.widget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class AutoVerticalLayoutContainer extends VerticalLayoutContainer {
    @Override
    public void add(Widget child) {
        super.add(child);
        doLayout();
    }
}