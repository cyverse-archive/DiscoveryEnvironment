package org.iplantc.de.client.viewer.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * 
 * 
 * @author sriram
 * 
 */
public class ImageViewerImpl implements FileViewer {

    private static ImageViewerUiBinder uiBinder = GWT.create(ImageViewerUiBinder.class);

    private final Widget widget;
    private Presenter presenter;

    @UiField(provided = true)
    Image img;

    @UiField
    VerticalLayoutContainer con;

    public ImageViewerImpl(String url) {
        img = new Image(url);
        widget = uiBinder.createAndBindUi(this);
        con.setScrollMode(ScrollMode.AUTO);
    }

    @UiTemplate("ImageViewer.ui.xml")
    interface ImageViewerUiBinder extends UiBinder<Widget, ImageViewerImpl> {
    }

    public ImageViewerImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;
    }

    @Override
    public void setData(Object data) {
        // Do nothing intentionally

    }

}
