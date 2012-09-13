/**
 * 
 */
package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.AnalysisPayloadEventHandler;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.gxt3.model.NotificationAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.NotificationMessage;
import org.iplantc.de.client.utils.NotificationHelper;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author sriram
 * 
 */
public class NotificationListView implements IsWidget {

    private ListView<NotificationMessage, NotificationMessage> view;
    private ListStore<NotificationMessage> store;
    private final NotificationAutoBeanFactory factory = GWT.create(NotificationAutoBeanFactory.class);

    final Resources resources = GWT.create(Resources.class);
    final Style style = resources.css();
    final Renderer r = GWT.create(Renderer.class);

    interface Renderer extends XTemplates {
        @XTemplate("<div class=\"{style.thumb}\"> {msg.message}</div>")
        public SafeHtml renderItem(NotificationMessage msg, Style style);
    }

    interface Style extends CssResource {
        String over();

        String select();

        String thumb();

        String thumbWrap();
    }

    interface Resources extends ClientBundle {
        @Source("NotificationListView.css")
        Style css();
    }

    ModelKeyProvider<NotificationMessage> kp = new ModelKeyProvider<NotificationMessage>() {
        @Override
        public String getKey(NotificationMessage item) {
            return item.getTimestamp() + "";
        }
    };

    ListViewCustomAppearance<NotificationMessage> appearance = new ListViewCustomAppearance<NotificationMessage>(
            "." + style.thumbWrap(), style.over(), style.select()) {

        @Override
        public void renderEnd(SafeHtmlBuilder builder) {
            String markup = new StringBuilder("<div class=\"").append(CommonStyles.get().clear())
                    .append("\"></div>").toString();
            builder.appendHtmlConstant(markup);
        }

        @Override
        public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
            builder.appendHtmlConstant("<div class='" + style.thumbWrap()
                    + "' style='border: 1px solid white'>");
            builder.append(content);
            builder.appendHtmlConstant("</div>");
        }

    };

    public NotificationListView() {
        registerEventHandlers();
        resources.css().ensureInjected();
    }

    private void registerEventHandlers() {
        final EventBus eventbus = EventBus.getInstance();

        // handle data events
        eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandler() {
            @Override
            public void onFire(DataPayloadEvent event) {
                addFromEventHandler(Category.DATA, I18N.DISPLAY.fileUpload(), event.getMessage(),
                        NotificationHelper.getInstance().buildDataContext(event.getPayload()),
                        event.getPayload());
                // highlightNewNotifications();
            }
        });

        // handle analysis events
        eventbus.addHandler(AnalysisPayloadEvent.TYPE, new AnalysisPayloadEventHandler() {
            @Override
            public void onFire(AnalysisPayloadEvent event) {
                addFromEventHandler(Category.ANALYSIS, I18N.CONSTANT.analysis(), event.getMessage(),
                        NotificationHelper.getInstance().buildAnalysisContext(event.getPayload()),
                        event.getPayload());
                // highlightNewNotifications();
            }
        });
    }

    private void addFromEventHandler(final Category category, final String header,
            final JSONObject objMessage, final String context, final JSONObject payload) {

        AutoBean<NotificationMessage> bean = AutoBeanCodex.decode(factory, NotificationMessage.class,
                objMessage.toString());

        NotificationMessage nm = bean.as();

        NotificationMessage existing = view.getStore().findModelWithKey(nm.getTimestamp() + "");

        if (existing == null) {
            view.getStore().add(bean.as());
        }
        // Notification notification = addItemToStore(category, objMessage, context, payload);
        //
        // if (notification != null) {
        // NotifyInfo.display(header, notification.getMessage());
        // }
    }

    @Override
    public Widget asWidget() {
        store = new ListStore<NotificationMessage>(kp);
        view = new ListView<NotificationMessage, NotificationMessage>(store,
                new IdentityValueProvider<NotificationMessage>(), appearance);

        view.getSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<NotificationMessage>() {

                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<NotificationMessage> event) {
                        // TODO Auto-generated method stub

                    }

                });

        view.setCell(new SimpleSafeHtmlCell<NotificationMessage>(
                new AbstractSafeHtmlRenderer<NotificationMessage>() {

                    @Override
                    public SafeHtml render(NotificationMessage object) {
                        return r.renderItem(object, style);
                    }
                }));

        view.setSize("250px", "200px");
        return view;
    }

}
