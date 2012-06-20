package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.accordian.AccordionChildPanelCollapseEvent;
import org.iplantc.de.client.events.accordian.AccordionChildPanelExpandEvent;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToolButton;

/**
 * An accordion child panel that can be used in wizards
 * 
 * @author sriram
 * 
 */
public class AccordionChildPanel extends ContentPanel {
    protected ToolButton collapse;

    protected ToolButton expand;

    /**
     * Constructs an instance of a panel given a unique identifier.
     * 
     * @param id unique id for this panel.
     */
    public AccordionChildPanel(String id) {
        setId(id);
        setAutoHeight(true);
        collapse = new ToolButton("x-tool-collapse"); //$NON-NLS-1$
        collapse.setToolTip(I18N.DISPLAY.hide());
        expand = new ToolButton("x-tool-expand"); //$NON-NLS-1$
        expand.setToolTip(I18N.DISPLAY.show());
        collapse.setId("collapse"); //$NON-NLS-1$
        expand.setId("expand"); //$NON-NLS-1$

        getHeader().setStyleName("accordianTitle"); //$NON-NLS-1$
        getHeader().addTool(collapse);
        setBodyBorder(true);

        registerHandlers();
    }

    private void registerHandlers() {
        // add listeners to the collapse button
        collapse.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                collapse.removeStyleName("x-tool-collapse-hover");
                handleCollapseEvent();
            }
        });

        collapse.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collapse.addStyleName("x-tool-collapse-hover");
            }
        });

        collapse.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                collapse.removeStyleName("x-tool-collapse-hover");
            }
        });

        // add listeners to the expand button
        expand.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                expand.removeStyleName("x-tool-expand-hover");
                handleExpandEvent();
            }
        });

        expand.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                expand.addStyleName("x-tool-expand-hover");
            }
        });

        expand.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                expand.removeStyleName("x-tool-expand-hover");
            }
        });

        // add listeners to this panel
        this.addListener(Events.Expand, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                handleExpandEvent();
            }
        });

        this.addListener(Events.Collapse, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                handleCollapseEvent();
            }
        });
    }

    /**
     * Handle the expand event of the panel.
     */
    private void handleExpandEvent() {
        if (isCollapsed()) {
            expand();
        }

        if (getHeader().getToolCount() > 0) {
            ToolButton btn = (ToolButton)this.getHeader().getTool(0);
            if (btn != null && btn.getId().equals("expand")) { //$NON-NLS-1$
                getHeader().removeTool(expand);
                getHeader().addTool(collapse);
            }
        }

        // notify other panels
        EventBus eventbus = EventBus.getInstance();
        AccordionChildPanelExpandEvent event = new AccordionChildPanelExpandEvent(this.getId());
        eventbus.fireEvent(event);
    }

    /**
     * Handle the collapse event of the panel.
     */
    private void handleCollapseEvent() {
        if (isExpanded()) {
            collapse();
        }

        if (getHeader().getToolCount() > 0) {
            ToolButton btn = (ToolButton)this.getHeader().getTool(0);
            if (btn != null && btn.getId().equals("collapse")) { //$NON-NLS-1$
                getHeader().removeTool(collapse);
                getHeader().addTool(expand);
            }
        }

        // notify other panels
        EventBus eventbus = EventBus.getInstance();
        AccordionChildPanelCollapseEvent event = new AccordionChildPanelCollapseEvent(this.getId());
        eventbus.fireEvent(event);
    }
}
