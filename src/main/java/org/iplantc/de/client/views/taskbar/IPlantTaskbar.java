package org.iplantc.de.client.views.taskbar;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Implementation of the taskbar where the start button resides.
 * 
 * @author amuir
 */
public class IPlantTaskbar extends LayoutContainer {
    /**
     * Defines a taskbar panel that appears in the Center of the layout container.
     */
    protected TasksButtonsPanel tbPanel; // center

    /**
     * Constructs an instance of a taskbar.
     */
    public IPlantTaskbar() {
        setId("ux-taskbar"); //$NON-NLS-1$
        setLayout(new RowLayout(Orientation.HORIZONTAL));
        tbPanel = new TasksButtonsPanel();
        add(tbPanel, new RowData(1, 1));
    }

    /**
     * Adds a button.
     * 
     * @param win the window
     * @return the new task button
     */
    public IPlantTaskButton addTaskButton(Window win) {
        return tbPanel.addButton(win);
    }

    /**
     * Returns the bar's buttons.
     * 
     * @return the buttons
     */
    public List<IPlantTaskButton> getButtons() {
        return tbPanel.getItems();
    }

    /**
     * Removes a button.
     * 
     * @param btn the button to remove
     */
    public void removeTaskButton(IPlantTaskButton btn) {
        tbPanel.removeButton(btn);
    }

    /**
     * Sets the active button.
     * 
     * @param btn the button
     */
    public void setActiveButton(IPlantTaskButton btn) {
        tbPanel.setActiveButton(btn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setStyleAttribute("zIndex", "10"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}

/**
 * Helper class for displaying taskbar buttons.
 * 
 * @author amuir
 * 
 */
class TasksButtonsPanel extends BoxComponent {
    private int buttonMargin = 2;
    private int buttonWidth = 168;
    private boolean buttonWidthSet = false;
    private boolean enableScroll = true;
    private List<IPlantTaskButton> items;
    private int lastButtonWidth;
    private int minButtonWidth = 118;
    private boolean resizeButtons = true;
    private int scrollIncrement = -1;
    private El stripWrap, strip, edge;

    /**
     * Constructs an instance of the panel.
     */
    TasksButtonsPanel() {
        setId("ux-taskbuttons-panel"); //$NON-NLS-1$
        items = new ArrayList<IPlantTaskButton>();
    }

    /**
     * Add a button to the taskbar buttons panel.
     * 
     * @param win
     * @return
     */
    public IPlantTaskButton addButton(Window win) {
        Element li = strip.createChild("<li></li>", edge.dom).dom; //$NON-NLS-1$
        IPlantTaskButton btn = new IPlantTaskButton(win, li);
        items.add(btn);

        if (!buttonWidthSet) {
            lastButtonWidth = li.getOffsetWidth();
        }

        setActiveButton(btn);
        win.setData("taskButton", btn); //$NON-NLS-1$

        if (isAttached()) {
            ComponentHelper.doAttach(btn);
        }

        if (!isEnabled()) {
            btn.disable();
        }

        return btn;
    }

    /**
     * Retrieve buttons.
     * 
     * @return
     */
    public List<IPlantTaskButton> getItems() {
        return items;
    }

    /**
     * Remove button from the taskbar buttons panel.
     * 
     * @param btn
     */
    public void removeButton(IPlantTaskButton btn) {
        Element li = (Element)btn.getElement().getParentElement();

        if (li != null && li.getParentElement() != null) {
            li.getParentElement().removeChild(li);
        }

        items.remove(btn);

        delegateUpdates();
        ComponentHelper.doDetach(btn);
    }

    /**
     * Set active button.
     * 
     * @param btn
     */
    public void setActiveButton(IPlantTaskButton btn) {
        delegateUpdates();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAttachChildren() {
        super.doAttachChildren();

        for (IPlantTaskButton btn : items) {
            ComponentHelper.doAttach(btn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDetachChildren() {
        super.doDetachChildren();

        for (IPlantTaskButton btn : items) {
            ComponentHelper.doDetach(btn);
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDisable() {
        super.onDisable();

        for (IPlantTaskButton btn : items) {
            btn.disable();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onEnable() {
        super.onEnable();

        for (IPlantTaskButton btn : items) {
            btn.enable();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        setElement(DOM.createDiv(), target, index);
        setStyleName("ux-taskbuttons-panel"); //$NON-NLS-1$

        stripWrap = el().createChild(
                "<div class='ux-taskbuttons-strip-wrap'><ul class='ux-taskbuttons-strip'></ul></div>"); //$NON-NLS-1$
        el().createChild("<div class='ux-taskbuttons-strip-spacer'></div>"); //$NON-NLS-1$
        strip = stripWrap.firstChild();
        edge = strip.createChild("<li class='ux-taskbuttons-edge'></li>"); //$NON-NLS-1$
        strip.createChild("<div class='x-clear'></div>"); //$NON-NLS-1$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        delegateUpdates();
    }

    private void autoScroll() {
        // auto scroll not functional
    }

    private void autoSize() {
        int count = items.size();
        int aw = el().getStyleWidth();

        if (!resizeButtons || count < 1) {
            return;
        }

        int each = (int)Math.max(Math.min(Math.floor((aw - 4) / count) - buttonMargin, buttonWidth),
                minButtonWidth);
        NodeList<com.google.gwt.dom.client.Element> btns = stripWrap.dom.getElementsByTagName("button"); //$NON-NLS-1$

        El b = items.get(0).el();
        lastButtonWidth = b.findParent("li", 5).getWidth(); //$NON-NLS-1$

        for (int i = 0,len = btns.getLength(); i < len; i++) {
            Element btn = btns.getItem(i).cast();

            int tw = items.get(i).el().getParent().dom.getOffsetWidth();
            int iw = btn.getOffsetWidth();

            btn.getStyle().setPropertyPx("width", (each - (tw - iw))); //$NON-NLS-1$
        }
    }

    private void delegateUpdates() {
        if (resizeButtons && rendered) {
            autoSize();
        }

        if (enableScroll && rendered) {
            autoScroll();
        }
    }
}
