package org.iplantc.de.client.utils.builders;

import java.util.List;

import org.iplantc.core.client.widgets.events.ValidationListener;
import org.iplantc.core.client.widgets.factory.WizardWidgetFactory;
import org.iplantc.core.client.widgets.metadata.WizardPropertyGroupContainer;
import org.iplantc.core.client.widgets.panels.WizardWidgetPanel;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.property.groups.PropertyGroup;
import org.iplantc.core.metadata.client.property.groups.PropertyGroupContainer;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.events.WizardValidationEvent;
import org.iplantc.de.client.views.panels.AccordionChildPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;

/**
 * Builds a wizard accordian from a PropertyGroupContainer.
 * 
 * @author amuir
 * 
 */
public class WizardBuilder {
    private AccordionLayout initAccordian() {
        AccordionLayout ret = new AccordionLayout();
        ret.setHideCollapseTool(true);

        return ret;
    }

    private ContentPanel initPanel() {
        ContentPanel ret = new ContentPanel();
        AccordionLayout accordian = initAccordian();

        ret.setHeaderVisible(false);
        ret.setLayout(accordian);
        ret.setBodyBorder(false);
        ret.setBodyStyleName("accordianbody"); //$NON-NLS-1$
        ret.setBorders(false);
        ret.setShadow(false);
        ret.setFrame(false);

        return ret;
    }

    private ContentPanel allocateContentPanel(int numGroups, final PropertyGroup group) {
        ContentPanel ret;

        if (numGroups == 1) {
            ret = new ContentPanel();
            ret.setBodyStyleName("accordianbody"); //$NON-NLS-1$
            ret.setBorders(false);
            ret.setShadow(false);
            ret.setFrame(false);
            ret.setHeaderVisible(false);
        } else {
            ret = new AccordionChildPanel(group.getId());
            ret.setHeading(group.getLabel());
        }

        return ret;
    }

    private void buildPanel(final ContentPanel panelOuter, final PropertyGroup group,
            final ComponentValueTable tblComponentVals, int numGroups) {
        if (group != null) {
            // build the panel for our accordian
            // final ContentPanel panel = (numGroups == 1) ? new ContentPanel() : new
            // AccordionChildPanelImpl(group.getId());
            final ContentPanel panel = allocateContentPanel(numGroups, group);
            panel.setScrollMode(Scroll.AUTOY);
            panel.setAutoHeight(false);

            // add an inner panel for formatting purposes
            final VerticalPanel panelInner = new VerticalPanel();
            panelInner.setAutoHeight(true);
            panelInner.setWidth(500);
            panelInner.setSpacing(10);
            panelInner.setAutoHeight(false);

            WizardWidgetFactory factory = new WizardWidgetFactory(new DiskResourceSelectorBuilderImpl());
            List<Property> properties = group.getProperties();

            ValidationListener vListener = new ValidationListener() {
                @Override
                public void onValidation(boolean pass) {
                    EventBus.getInstance().fireEvent(new WizardValidationEvent(pass));
                }
            };
            
            // try to build a widget for each property
            for (Property property : properties) {
                WizardWidgetPanel widget = factory.build(property, tblComponentVals);
                widget.setAutoHeight(false);

                // were we able to build a widget?
                if (widget != null) {
                    // store all of our top-level components
                    tblComponentVals.addComponent(property.getId(), widget);

                    // add our widget to the formatted panel
                    panelInner.add(widget);
                    
                    // fire a WizardValidationEvent on field-level validation events
                    widget.addValidationListener(vListener);
                }
            }

            panel.add(panelInner);
            panelOuter.add(panel);
        }
    }

    private int getVisibleGroupCount(final List<PropertyGroup> groups) {
        int ret = 0;

        // simply loop through our groups and count the visible ones
        for (PropertyGroup group : groups) {
            if (group.isVisible()) {
                ret++;
            }
        }

        return ret;
    }

    private void buildPanels(final ContentPanel panel, final PropertyGroupContainer container,
            final ComponentValueTable tblComponentVals) {
        List<PropertyGroup> groups = container.getGroups();

        if (groups != null) {
            int numVisibleGroups = getVisibleGroupCount(groups);

            for (PropertyGroup group : groups) {
                // do we want to display this group?
                if (group.isVisible()) {
                    buildPanel(panel, group, tblComponentVals, numVisibleGroups);
                }
            }
        }
    }

    public ContentPanel build(final WizardPropertyGroupContainer container,
            final ComponentValueTable tblComponentVals) {
        ContentPanel ret = null; // assume failure

        if (container != null) {
            ret = initPanel();

            tblComponentVals.seed(container);

            buildPanels(ret, container, tblComponentVals);
        }

        return ret;
    }
}
