/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.AnalysisPayloadEventHandler;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.utils.AnalysisViewContextExecutor;
import org.iplantc.de.client.utils.DataViewContextExecutor;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.utils.NotifyInfo;
import org.iplantc.de.client.utils.builders.context.AnalysisContextBuilder;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.json.client.JSONObject;

/**
 * @author sriram
 * 
 */
public class ViewNotification extends Menu {

    private ListStore<Notification> store;
    private DataContextBuilder dataContextBuilder;
    private AnalysisContextBuilder analysisContextBuilder;
    private DataViewContextExecutor dataContextExecutor;
    private AnalysisViewContextExecutor analysisContextExecutor;

    public ViewNotification() {
        setLayout(new FitLayout());
        initContextBuilders();
        initContextExecuters();
        registerEventHandlers();
        CustomListView<Notification> view = initList();
        LayoutContainer lc = buildPanel();
        lc.add(view);
        add(lc);
    }

    private LayoutContainer buildPanel() {
        LayoutContainer lc = new LayoutContainer();
        lc.setLayout(new FitLayout());
        lc.setSize(250, 270);
        lc.setBorders(false);
        return lc;
    }

    private CustomListView<Notification> initList() {
        store = new ListStore<Notification>();

        CustomListView<Notification> view = new CustomListView<Notification>();

        view.setTemplate(getTemplate());
        view.getSelectionModel().addSelectionChangedListener(
                new SelectionChangedListener<Notification>() {

                    @Override
                    public void selectionChanged(SelectionChangedEvent<Notification> se) {
                        final Notification notification = se.getSelectedItem();
                        if (notification == null) {
                            return;
                        }
                        view(notification);
                    }
                });
        view.setItemSelector("div.search-item"); //$NON-NLS-1$
        view.setStore(store);
        view.setEmptyText("No new notifications!");
        return view;
    }

    private void registerEventHandlers() {
        final EventBus eventbus = EventBus.getInstance();

        // handle data events
        eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandler() {
            @Override
            public void onFire(DataPayloadEvent event) {
                addFromEventHandler(Category.DATA, I18N.DISPLAY.fileUpload(), event.getMessage(),
                        dataContextBuilder.build(event.getPayload()));
            }
        });

        // handle analysis events
        eventbus.addHandler(AnalysisPayloadEvent.TYPE, new AnalysisPayloadEventHandler() {
            @Override
            public void onFire(AnalysisPayloadEvent event) {
                addFromEventHandler(Category.ANALYSIS, I18N.CONSTANT.analysis(), event.getMessage(),
                        analysisContextBuilder.build(event.getPayload()));

            }
        });
    }

    /** View a notification */
    private void view(Notification notification) {
        if (notification != null) {
            NotificationManager.Category category = notification.getCategory();

            // did we get a category?
            if (category != null) {
                String context = notification.getContext();
                System.out.println("context -->" + context);
                // did we get a context to execute?
                if (context != null) {
                    if (category == NotificationManager.Category.DATA) {
                        // execute data context
                        dataContextExecutor.execute(context);
                    } else if (category == NotificationManager.Category.ANALYSIS) {
                        analysisContextExecutor.execute(context);
                    }
                }
            }
        }
    }

    private void initContextBuilders() {
        dataContextBuilder = new DataContextBuilder();
        analysisContextBuilder = new AnalysisContextBuilder();
    }

    private void initContextExecuters() {
        dataContextExecutor = new DataViewContextExecutor();
        analysisContextExecutor = new AnalysisViewContextExecutor();
    }

    private Notification addItemToStore(final Category category, final JSONObject objMessage,
            final String context) {
        Notification ret = null; // assume failure

        if (objMessage != null) {
            ret = new Notification(objMessage, context);
            add(category, ret);
        }

        return ret;
    }

    private void addFromEventHandler(final Category category, final String header,
            final JSONObject objMessage, final String context) {
        Notification notification = addItemToStore(category, objMessage, context);

        if (notification != null) {
            NotifyInfo.display(header, notification.getMessage());
        }
    }

    public void add(final Category category, final Notification notification) {
        // did we get a valid notification?
        if (category != Category.ALL && notification != null) {
            notification.setCategory(category);

            store.add(notification);
        }
    }

    private String getTemplate() {
        StringBuilder template = new StringBuilder();
        template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$
        template.append("<tpl if=\"context\"> <div class='context-notify'> </tpl>");
        template.append("{message} <tpl if=\"context\"> </div> </tpl></div></tpl>");
        System.out.println(template.toString());
        return template.toString();
    }

    private class CustomListView<M extends ModelData> extends ListView<M> {
        private String emptyText;

        @Override
        protected void afterRender() {
            super.afterRender();

            applyEmptyText();
        }

        @Override
        public void refresh() {
            super.refresh();

            applyEmptyText();
        }

        protected void applyEmptyText() {
            if (emptyText == null) {
                emptyText = "&nbsp;";
            }
            if (store.getModels().size() == 0) {
                el().setInnerHtml("<div class='x-grid-empty'>" + emptyText + "</div>");
            }
        }

        public void setEmptyText(String emptyText) {
            this.emptyText = emptyText;
        }

        @SuppressWarnings("unused")
        public String getEmptyText() {
            return emptyText;
        }
    }

}
