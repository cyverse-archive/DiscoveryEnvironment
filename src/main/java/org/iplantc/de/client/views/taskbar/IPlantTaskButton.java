package org.iplantc.de.client.views.taskbar;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Definition of a button that can be added to the taskbar.
 * 
 * @author amuir
 * 
 */
public class IPlantTaskButton extends Button {
    private Window win;

    IPlantTaskButton(Window win, Element parent) {
        this.win = win;

        updateText();
        setIcon(win.getIcon());
        template = new Template(getButtonTemplate());

        render(parent);
    }

    private native String getButtonTemplate() /*-{
                                              return [
                                              '<table border="0" cellpadding="0" cellspacing="0" class="x-btn-wrap"><tbody><tr>',
                                              '<td class="ux-taskbutton-left"><i>&#160;</i></td><td class="ux-taskbutton-center"><em unselectable="on"><button class="x-btn-text" type="{1}" style="height:28px;">{0}</button></em></td><td class="ux-taskbutton-right"><i>&#160;</i></td>',
                                              '</tr></tbody></table>'
                                              ].join("");
                                              }-*/;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void autoWidth() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClick(ComponentEvent ce) {
        super.onClick(ce);

        if (win.getData("minimized") != null || !win.isVisible()) { //$NON-NLS-1$
            win.show();
        } else if (win == WindowManager.get().getActive()) {
            win.minimize();
        } else {
            win.toFront();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        buttonEl.setSize(width - 8, height, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon(AbstractImagePrototype icon) {
        if (rendered) {
            El oldIcon = buttonEl.selectNode(".x-taskbutton-icon"); //$NON-NLS-1$

            if (oldIcon != null) {
                oldIcon.remove();
                buttonEl.setPadding(new Padding(7, 0, 7, 0));
            }

            if (icon != null) {
                buttonEl.setPadding(new Padding(7, 0, 7, 20));

                Element e = (Element)icon.createElement().cast();
                e.setClassName("x-taskbutton-icon"); //$NON-NLS-1$
                buttonEl.insertFirst(e);
                El.fly(e).makePositionable(true);

                String align = "b-b"; // assume bottom alignment //$NON-NLS-1$
                if (getIconAlign() == IconAlign.TOP) {
                    align = "t-t"; //$NON-NLS-1$
                } else if (getIconAlign() == IconAlign.LEFT) {
                    align = "l-l"; //$NON-NLS-1$
                } else if (getIconAlign() == IconAlign.RIGHT) {
                    align = "r-r"; //$NON-NLS-1$
                }

                El.fly(e).alignTo(buttonEl.dom, align, null);
            }
        }

        this.icon = icon;
    }

    private String sizeTextForButton(String in) {
        String ret = ""; //$NON-NLS-1$

        if (in != null) {
            // if the text is too long, we will truncate and add ellipses
            ret = (in.length() < 21) ? in : in.substring(0, 18) + "..."; //$NON-NLS-1$
        }

        return ret;
    }

    /**
     * Update the text for the button.
     */
    public void updateText() {
        if (win != null) {
            String text = sizeTextForButton(win.getHeading());
            setText(text);
        }
    }
}
