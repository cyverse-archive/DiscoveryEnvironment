package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.client.events.DiskResourceSelectionChangedEventHandler;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;

public class DataDetailsPanel extends AbstractDataPanel {
    private DataDetailListPanel pnlDetails;
//    private DataProvenancePanel pnlProvenance;

    public DataDetailsPanel(final String tag) {
        super(tag);

        initListeners();

        initPanels();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
    }

    private void initListeners() {
        addListener(Events.Resize, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (pnlDetails != null) {
                    pnlDetails.layout();
                }

//                if (pnlProvenance != null) {
//                    pnlProvenance.handleResize();
//                }
            }
        });
    }

    private void initPanels() {
        pnlDetails = new DataDetailListPanel();
        // pnlProvenance = new DataProvenancePanel();
    }

    @Override
    protected void registerHandlers() {
        super.registerHandlers();

        EventBus eventbus = EventBus.getInstance();

        handlers.add(eventbus.addHandler(DiskResourceSelectionChangedEvent.TYPE,
                new DiskResourceSelectionChangedEventHandler() {
                    @Override
                    public void onChange(DiskResourceSelectionChangedEvent event) {
                        if (event.getTag().equals(tag)) {
                            List<DiskResource> resources = event.getSelected();

                            pnlDetails.update(resources);
                            // pnlProvenance.update(resources);
                        }
                    }
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(pnlDetails);
        // add(pnlProvenance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setHeading() {
        setHeading(I18N.DISPLAY.details());
    }

}
