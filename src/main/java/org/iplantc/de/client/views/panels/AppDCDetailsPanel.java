/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.events.AppSelectedEvent;
import org.iplantc.core.uiapplications.client.events.AppSelectedEventHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.JsDeployedComponent;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author sriram
 * 
 */
public class AppDCDetailsPanel extends ContentPanel {

    public AppDCDetailsPanel() {
        initListener();
        setHeading("App Details");
        setLayout(new AccordionLayout());
    }

    private void initListener() {
        EventBus.getInstance().addHandler(AppSelectedEvent.TYPE, new AppSelectedEventHandler() {

            @Override
            public void onSelection(AppSelectedEvent event) {
                getDCDetails(event.getAppId());
            }
        });
    }

    private void getDCDetails(final String appId) {
        TemplateServiceFacade facade = new TemplateServiceFacade();
        facade.getDCDetails(appId, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JsArray<JsDeployedComponent> list = parseResults(result);
                addToAccordion(list);

            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println(caught.toString());
            }
        });
    }

    private void addToAccordion(JsArray<JsDeployedComponent> list) {
        ContentPanel panel = null;
        removeAll();
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                panel = new ContentPanel();
                panel.setScrollMode(Scroll.AUTO);
                panel.setSize(160, 180);
                panel.setHeading("<b>" + list.get(i).getName() + "</b>");
                XTemplate tpl = XTemplate.create(getTemplate());
                panel.removeAll();
                panel.addText(tpl.applyTemplate(list.get(i)));
                panel.layout();
                add(panel);
            }
            layout();
        }

    }

    private String getTemplate() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p style= 'font-size:11px;'><b>Description</b>: {description}<br/>");
        sb.append("<b>Location</b>: {location}<br/>");
        sb.append("<b>Type</b>: {type}<br/>");
        sb.append("<b>Version</b>: {version}<br/>");
        sb.append("<b>Attribution</b>: {attribution}</p>");
        return sb.toString();
    }

    private JsArray<JsDeployedComponent> parseResults(String result) {
        JSONArray jsonComponents = JsonUtil.getArray(JsonUtil.getObject(result), "deployed_components"); //$NON-NLS-1$
        if (jsonComponents != null) {
            JsArray<JsDeployedComponent> jscomps = JsonUtil.asArrayOf(jsonComponents.toString());
            return jscomps;
        }

        return null;
    }
}
