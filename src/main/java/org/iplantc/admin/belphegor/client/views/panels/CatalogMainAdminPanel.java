package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.core.uiapplications.client.views.panels.BaseCatalogMainPanel;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A panel that displays apps in a grid.
 */
public class CatalogMainAdminPanel extends BaseCatalogMainPanel {

    /**
     * Creates a new CatalogMainAdminPanel.
     * 
     * @param templateService
     */
    public CatalogMainAdminPanel() {
        initToolBar();
    }

    private void initToolBar() {
        addToToolBar(buildDeleteButton());
    }

    private Button buildDeleteButton() {
        Button btn = new Button(I18N.DISPLAY.delete());

        btn.setId("idDelete"); //$NON-NLS-1$
        btn.setIcon(AbstractImagePrototype.create(Resources.ICONS.delete()));

        return btn;
    }
}
