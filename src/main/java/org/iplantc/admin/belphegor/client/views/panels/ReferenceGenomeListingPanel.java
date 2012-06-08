package org.iplantc.admin.belphegor.client.views.panels;

import java.util.Arrays;

import org.iplantc.admin.belphegor.client.models.ReferenceGenome;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 * A grid panel to display a list of reference genomes
 * 
 * @author sriram
 * 
 */
public class ReferenceGenomeListingPanel extends ContentPanel {

    private Grid<ReferenceGenome> grid;

    public ReferenceGenomeListingPanel() {
        init();
    }

    private void init() {
        setLayout(new FitLayout());
        setSize(1024, 768);
        buildGrid();
    }

    private void buildGrid() {
        grid = new Grid<ReferenceGenome>(new ListStore<ReferenceGenome>(), buildColumnModel());
        add(grid);
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig name = new ColumnConfig(ReferenceGenome.NAME, ReferenceGenome.NAME, 200);
        ColumnConfig path = new ColumnConfig(ReferenceGenome.PATH, ReferenceGenome.PATH, 200);
        ColumnConfig createdon = new ColumnConfig(ReferenceGenome.CREATED_ON,
                ReferenceGenome.CREATED_ON, 150);
        ColumnConfig createdby = new ColumnConfig(ReferenceGenome.CREATED_BY,
                ReferenceGenome.CREATED_BY, 150);
        return new ColumnModel(Arrays.asList(name, path, createdon, createdby));
    }
}
