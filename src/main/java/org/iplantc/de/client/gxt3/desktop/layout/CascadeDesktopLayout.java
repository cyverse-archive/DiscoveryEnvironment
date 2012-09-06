/**
 * Sencha GXT 3.0.1 - Sencha for GWT Copyright(c) 2007-2012, Sencha, Inc. licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
package org.iplantc.de.client.gxt3.desktop.layout;

import org.iplantc.de.client.views.windows.IPlantWindowInterface;

import com.google.gwt.user.client.Element;

public class CascadeDesktopLayout extends LimitedDesktopLayout implements DesktopLayout {

    protected int left;
    protected int top;
    protected int offsetIndex;

    @Override
    public DesktopLayoutType getDesktopLayoutType() {
        return DesktopLayoutType.CASCADE;
    }

    @Override
    public void layoutDesktop(IPlantWindowInterface requestWindow, RequestType requestType,
            Element element, Iterable<IPlantWindowInterface> windows, int containerWidth,
            int containerHeight) {

        if (requestType == RequestType.LAYOUT || requestType == RequestType.RESIZE) {
            left = 0;
            top = 0;
            offsetIndex = 0;
        }

        super.layoutDesktop(requestWindow, requestType, element, windows, containerWidth,
                containerHeight);
    }

    @Override
    protected void layoutWindow(IPlantWindowInterface window, int containerWidth, int containerHeight,
            int width, int height) {

        int offset = window.getHeaderOffSetHeight();

        window.setPixelSize(width, height);

        if (top + height > containerHeight) {
            left = ++offsetIndex * offset;
            top = 0;
        }

        if (left + width > containerWidth) {
            left = 0;
            top = 0;
            offsetIndex = 0;
        }

        window.setPosition(left, top);

        left += offset;
        top += offset;
    }

}
