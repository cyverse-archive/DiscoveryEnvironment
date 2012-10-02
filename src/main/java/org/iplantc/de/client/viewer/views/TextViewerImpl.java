/**
 * 
 */
package org.iplantc.de.client.viewer.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * @author sriram
 * 
 */
public class TextViewerImpl implements FileViewer {

    private static TextViewerUiBinder uiBinder = GWT.create(TextViewerUiBinder.class);

    @UiTemplate("TextViewer.ui.xml")
    interface TextViewerUiBinder extends UiBinder<Widget, TextViewerImpl> {
    }

    private final Widget widget;

    private Presenter presenter;

    @UiField
    HTML textArea;

    @UiField
    VerticalLayoutContainer con;

    public TextViewerImpl() {
        widget = uiBinder.createAndBindUi(this);
        con.setScrollMode(ScrollMode.AUTO);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        presenter = p;
    }

    @Override
    public void setData(Object data) {
        textArea.setHTML((String)data);
    }

}
