package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.util.WindowUtil;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TreeHyperlinkGridPanel extends ViewerContentPanel {
    public static final String TREE_URLS = "tree-urls"; //$NON-NLS-1$

    private static final String LABEL = "label"; //$NON-NLS-1$
    private static final String URL = "url"; //$NON-NLS-1$

    private Grid<ModelData> grid;
    private List<ModelData> links;
    private int tabIndex;

    public TreeHyperlinkGridPanel(FileIdentifier fileIdentifier, final JSONArray items) {
        super(fileIdentifier);

        init(items);

        setTabIndex(1);
    }

    private void init(JSONArray items) {
        setScrollMode(Scroll.AUTO);
        reconfigure(items);
    }

    private void reconfigure(JSONArray items) {
        // build list for urls tab
        links = new ArrayList<ModelData>();

        if (items != null) {
            for (int i = 0,len = items.size(); i < len; i++) {
                JSONObject jsonTreeUrl = items.get(i).isObject();

                String url = JsonUtil.getString(jsonTreeUrl, URL);
                String label = JsonUtil.getString(jsonTreeUrl, LABEL);

                if (url != null && !url.isEmpty()) {
                    ModelData model = new BaseModel();
                    model.set(URL, url);
                    model.set(LABEL, label);

                    links.add(model);
                }
            }
        }

        if (isRendered() && grid != null) {
            ListStore<ModelData> store = grid.getStore();
            store.removeAll();
            store.add(links);
        }
    }

    private Hyperlink buildLink(final String text) {
        Hyperlink ret = new Hyperlink(text, "de_tnrs_hyperlink"); //$NON-NLS-1$

        ret.addListener(Events.OnClick, new Listener<ComponentEvent>() {
            @Override
            public void handleEvent(ComponentEvent be) {
                WindowUtil.open(text, "width=100,height=100"); //$NON-NLS-1$
            }
        });

        return ret;
    }

    private void compose() {
        // build column model
        ColumnConfig label = new ColumnConfig(LABEL, I18N.DISPLAY.label(), 75);
        ColumnConfig url = new ColumnConfig(URL, I18N.DISPLAY.treeUrl(), 280);
        url.setRenderer(new GridCellRenderer<ModelData>() {

            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
                return buildLink(model.get(URL).toString());
            }
        });

        ColumnModel cm = new ColumnModel(Arrays.asList(label, url));

        // build store
        ListStore<ModelData> store = new ListStore<ModelData>();
        store.add(links);

        // create and add grid
        grid = new Grid<ModelData>(store, cm);
        grid.setAutoExpandColumn(url.getId());
        grid.setSize(getWidth() - 2, getHeight());

        add(grid);
    }

    /**
     * Calls the tree URL service to fetch the URLs to display in the grid.
     */
    public void callTreeCreateService() {
        mask(I18N.DISPLAY.loadingMask());

        FileEditorServiceFacade facade = new FileEditorServiceFacade();

        facade.getTreeUrl(fileIdentifier.getFileId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null && !result.isEmpty()) {
                    JSONArray urls = JsonUtil.getArray(JsonUtil.getObject(result), TREE_URLS);

                    if (urls != null && urls.size() > 0) {
                        // successfully retrieved URLs
                        reconfigure(urls);
                        unmask();

                        return;
                    }
                }

                // couldn't find any tree URLs in the response, so display an error.
                onFailure(new Exception(result));
            }

            @Override
            public void onFailure(Throwable caught) {
                unmask();

                String errMsg = I18N.ERROR.unableToRetrieveTreeUrls(fileIdentifier.getFilename());
                ErrorHandler.post(errMsg, caught);
            }
        });
    }

    @Override
    public String getTabHeader() {
        return I18N.CONSTANT.trees();
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Sets the desired tab position with the given index.
     * 
     * @param index desired tab position.
     */
    @Override
    public void setTabIndex(int index) {
        tabIndex = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        compose();
    }
}
