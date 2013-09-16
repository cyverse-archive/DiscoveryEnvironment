package org.iplantc.admin.belphegor.client.refGenome.view;

import java.util.Date;
import java.util.List;

import org.iplantc.admin.belphegor.client.refGenome.RefGenomeView;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeProperties;
import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class RefGenomeViewImpl extends Composite implements RefGenomeView {

    private static RefGenomeViewImpleUiBinder uiBinder = GWT.create(RefGenomeViewImpleUiBinder.class);

    private final class FilterByNameStoreFilter implements StoreFilter<ReferenceGenome> {
        private String query;

        @Override
        public boolean select(Store<ReferenceGenome> store, ReferenceGenome parent, ReferenceGenome item) {
            return item.getName().toLowerCase().startsWith(query.toLowerCase());
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

    interface RefGenomeViewImpleUiBinder extends UiBinder<Widget, RefGenomeViewImpl> {}

    @UiField(provided = true)
    IplantResources res;
    @UiField(provided = true)
    IplantDisplayStrings strings;

    @UiField
    TextButton addBtn;

    @UiField
    Grid<ReferenceGenome> grid;

    @UiField
    ListStore<ReferenceGenome> store;

    private final ReferenceGenomeProperties rgProps;
    private final FilterByNameStoreFilter nameFilter;

    @Inject
    public RefGenomeViewImpl(IplantResources res, IplantDisplayStrings strings, ReferenceGenomeProperties rgProps) {
        this.res = res;
        this.strings = strings;
        this.rgProps = rgProps;
        initWidget(uiBinder.createAndBindUi(this));
        nameFilter = new FilterByNameStoreFilter();
        store.addFilter(nameFilter);
    }

    @UiFactory
    ListStore<ReferenceGenome> createListStore() {
        return new ListStore<ReferenceGenome>(rgProps.id());
    }

    @UiFactory
    ColumnModel<ReferenceGenome> createColumnModel() {
        ColumnConfig<ReferenceGenome, String> nameCol = new ColumnConfig<ReferenceGenome, String>(rgProps.name(), 90, strings.name());
        ColumnConfig<ReferenceGenome, String> pathCol = new ColumnConfig<ReferenceGenome, String>(rgProps.path(), 90, strings.path());
        ColumnConfig<ReferenceGenome, Date> createdOnCol = new ColumnConfig<ReferenceGenome, Date>(rgProps.createdDate(), 90, strings.createdOn());
        ColumnConfig<ReferenceGenome, String> createdByCol = new ColumnConfig<ReferenceGenome, String>(rgProps.createdBy(), 90, strings.createdBy());

        @SuppressWarnings("unchecked")
        List<ColumnConfig<ReferenceGenome, ?>> colList = Lists.<ColumnConfig<ReferenceGenome, ?>> newArrayList(nameCol, pathCol, createdByCol, createdOnCol);
        return new ColumnModel<ReferenceGenome>(colList);
    }

    @UiHandler("addBtn")
    void addButtonClicked(SelectEvent event) {

    }

    @UiHandler("filterField")
    void onFilterValueChanged(ValueChangeEvent<String> event) {
        store.removeFilters();
        final String query = Strings.nullToEmpty(event.getValue());
        if (query.isEmpty()) {
            return;
        }
        nameFilter.setQuery(query);
        store.addFilter(nameFilter);
    }

}
