package org.iplantc.de.client.views.panels;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

/**
 * Panel for displaying an image.
 * 
 * @author amuir
 * 
 */
public class ImagePanel extends ProvenanceContentPanel {
    private String urlImage;
    private VerticalPanel panel = new VerticalPanel();

    /**
     * Instantiate from a file identifier and URL.
     * 
     * @param fileIdentifier file identifier associated with this panel.
     * @param urlImage URL of image to retrieve.
     */
    public ImagePanel(FileIdentifier fileIdentifier, String urlImage) {
        super(fileIdentifier);

        this.urlImage = urlImage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabHeader() {
        return I18N.DISPLAY.image();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProvenance(String provenance) {
        super.updateProvenance(provenance);

        int height = (provenance != null && provenance.trim().length() > 0) ? 280 : 360;
        panel.setHeight(height);

        layout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        panel.setSpacing(5);
        panel.setScrollMode(Scroll.AUTO);
        panel.setWidth("100%"); //$NON-NLS-1$
        panel.setStyleAttribute("background-color", "white"); //$NON-NLS-1$ //$NON-NLS-2$

        panel.add(new Image(urlImage));
        add(panel, centerData);
    }
}
