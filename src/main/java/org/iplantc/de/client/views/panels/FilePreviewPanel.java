package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.FileEditorWindowDirtyEvent;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/**
 * A panel that show file preview if available
 * 
 * @author sriram
 * 
 */
public class FilePreviewPanel extends ViewerContentPanel {

    private FileIdentifier file;
    private String data;
    private TextArea areaData;
    private String textOrig;
    private boolean editable;

    /**
     * Constructs an instance given a file identifier, the data to display as preview, and a boolean
     * indicating if the panel is editable.
     * 
     * @param fileIdentifier a unique identifier for the file.
     * @param data the textual data to display in the panel.
     * @param editable a boolean flag indicating true then data is editable; otherwise false.
     */
    public FilePreviewPanel(FileIdentifier fileIdentifier, String data, boolean editable) {
        super(fileIdentifier);
        this.data = data;
        this.editable = editable;
        buildTextArea();
        setHeading(I18N.DISPLAY.filePreviewNotice());
        setHeaderVisible(true);
    }

    private void buildTextArea() {
        areaData = buildTextArea(editable);

        // we don't need to listen for changes if we are not editable
        if (editable) {
            areaData.addListener(Events.OnKeyUp, new Listener<FieldEvent>() {
                public void handleEvent(FieldEvent be) {
                    String text = areaData.getValue();

                    if (!text.equals(textOrig)) {
                        textOrig = text;

                        // don't fire event if we are already dirty
                        if (!dirty) {
                            dirty = true;
                            EventBus eventbus = EventBus.getInstance();
                            FileEditorWindowDirtyEvent event = new FileEditorWindowDirtyEvent(file
                                    .getFileId(), true);
                            eventbus.fireEvent(event);
                        }
                    }
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        if (data != null) {
            textOrig = data;
            areaData.setValue(data);
            areaData.setWidth(getWidth());

            ContentPanel panel = new ContentPanel();
            panel.setHeaderVisible(false);
            panel.setLayout(new FitLayout());
            panel.setWidth(getWidth());
            panel.setBottomComponent(buildWrapCheckBox());

            panel.add(areaData);

            add(panel, centerData);
        }
    }

    /**
     * Creates a check box that enables or disables text wrapping.
     * 
     * @return a new CheckBox
     */
    private CheckBox buildWrapCheckBox() {
        final CheckBox cb = new CheckBox();
        cb.setBoxLabel(I18N.DISPLAY.wrap());
        cb.setValue(true);
        cb.addListener(Events.Change, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                // calculate the scroll bar position as a percentage of the total height
                int scrollTop = areaData.getElement().getFirstChildElement().getScrollTop();
                int scrollHeight = areaData.getElement().getFirstChildElement().getScrollHeight();
                int visibleHeight = areaData.getHeight();
                float scrollRatio = scrollHeight > visibleHeight ? (float)scrollTop
                        / (scrollHeight - visibleHeight) : 0;

                // the "wrap" attribute cannot be changed on an existing text area, so make a new one
                LayoutContainer parent = (LayoutContainer)areaData.getParent();
                areaData.removeFromParent();
                boolean wrap = Boolean.TRUE.equals(((FieldEvent)be).getValue());

                // the <TextArea> DOM element is a child of areaData
                areaData.el().getChild(0).dom.setAttribute("wrap", wrap ? "on" : "off"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                parent.add(areaData);
                parent.layout(true);

                // restore scroll bar position (height may have changed, so restore relative position)
                int newScrollHeight = areaData.getElement().getFirstChildElement().getScrollHeight();
                float newScrollTop = (newScrollHeight - visibleHeight) * scrollRatio;
                areaData.getElement().getFirstChildElement().setScrollTop(Math.round(newScrollTop));
            }
        });
        return cb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        areaData.el().setElementAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabHeader() {
        return I18N.DISPLAY.preview();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return 1;
    }
}
