package org.iplantc.de.client.views.windows;

import org.iplantc.core.uicommons.client.models.WindowConfig;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;

public abstract class IPlantThreePanelWindow extends IPlantWindow {
    protected ContentPanel pnlContents;
    protected BorderLayoutData dataWest;
    protected BorderLayoutData dataCenter;
    protected BorderLayoutData dataEast;

    /**
     * 
     * @param tag a unique identifier for the window.
     * @param config a window configuration
     */
    protected IPlantThreePanelWindow(final String tag, final WindowConfig config) {
        super(tag, false, true, true, true);

        init();

        initModel();

        initLayout();

        initPanels();
    }

    /**
     * Do window initialization.
     * 
     * @param tag unique tag for this window.
     */
    protected void init() {
        pnlContents = new ContentPanel();
        pnlContents.setHeaderVisible(false);

        setId(tag);
        setTitle(getCaption());
        setInitialSize();
    }

    private BorderLayoutData initLayoutRegion(LayoutRegion region, float size, boolean collapsible) {
        BorderLayoutData ret = new BorderLayoutData(region);

        if (size > 0) {
            ret.setSize(size);
        }

        ret.setCollapsible(collapsible);
        ret.setSplit(true);

        return ret;
    }

    private void initLayout() {
        BorderLayout layout = new BorderLayout();

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        pnlContents.setLayout(layout);

        dataWest = initLayoutRegion(LayoutRegion.WEST, getWestWidth(), true);
        dataCenter = initLayoutRegion(LayoutRegion.CENTER, 0, false);
        dataEast = initLayoutRegion(LayoutRegion.EAST, getEastWidth(), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        add(pnlContents);
        compose();
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);

        resizeContents(getInnerWidth(), getInnerHeight());
    }

    @Override
    protected void onAfterLayout() {
        super.onAfterLayout();

        resizeContents(getInnerWidth(), getInnerHeight());
    }

    /**
     * Resizes this panel's inner tree panel.
     * 
     * @param width
     * @param height
     */
    private void resizeContents(int width, int height) {
        if (pnlContents != null) {
            pnlContents.setWidth(width);
            pnlContents.setHeight(height);
        }
    }

    /**
     * Retrieve the window caption.
     * 
     * @return window caption.
     */
    protected abstract String getCaption();

    /**
     * Initialize internal model.
     */
    protected abstract void initModel();

    /**
     * Initialize the three panels (west, center and east)
     */
    protected abstract void initPanels();

    /**
     * Perform layout.
     */
    protected abstract void compose();

    /**
     * Set the inital window size.
     */
    protected abstract void setInitialSize();

    /**
     * Retrieve the width for the west area of the border layout.
     * 
     * @return the width.
     */
    protected abstract int getWestWidth();

    /**
     * Retrieve the width for the east area of the border layout.
     * 
     * @return the width.
     */
    protected abstract int getEastWidth();
}
