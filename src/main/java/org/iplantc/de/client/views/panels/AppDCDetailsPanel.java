/**
 * 
 */
package org.iplantc.de.client.views.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.JsDeployedComponent;
import org.iplantc.core.uicommons.client.views.panels.IPlantDialogPanel;
import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author sriram
 * 
 */
public class AppDCDetailsPanel extends IPlantDialogPanel {
    private final ContentPanel contents = new ContentPanel();

    private final AppTemplateUserServiceFacade templateService = Services.USER_TEMPLATE_SERVICE;

    public AppDCDetailsPanel(Analysis app) {
        contents.setLayout(new AccordionLayout());
        contents.setHeaderVisible(false);

        getDCDetails(app.getId());
    }

    private void getDCDetails(final String appId) {
        contents.mask(I18N.DISPLAY.loadingMask());

        templateService.getDCDetails(appId, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JsArray<JsDeployedComponent> list = parseResults(result);
                addToAccordion(list);

                contents.unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                contents.unmask();
                ErrorHandler.post(I18N.ERROR.deployedComponentRetrievalFailure(), caught);
            }
        });
    }

    private void addToAccordion(JsArray<JsDeployedComponent> list) {
        contents.removeAll();

        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                JsDeployedComponent deployedComponent = list.get(i);

                XTemplate tpl = XTemplate.create(getTemplate());
                ContentPanel panel = new ContentPanel();

                panel.setScrollMode(Scroll.AUTO);
                panel.setSize(160, 180);
                panel.setHeading("<b>" + deployedComponent.getName() + "</b>");
                panel.addText(tpl.applyTemplate(deployedComponent));

                contents.add(panel);
            }

            contents.layout();
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
            return JsonUtil.asArrayOf(jsonComponents.toString());
        }

        return null;
    }

    @Override
    public void handleOkClick() {
        // do nothing intentionally
    }

    @Override
    public Widget getDisplayWidget() {
        return contents;
    }
}
