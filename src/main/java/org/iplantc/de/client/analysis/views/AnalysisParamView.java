package org.iplantc.de.client.analysis.views;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog.DialogOkClickHandler;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.analysis.models.AnalysisParameter;
import org.iplantc.de.client.analysis.views.AnalysesView.Presenter;
import org.iplantc.de.client.events.DefaultUploadCompleteHandler;
import org.iplantc.de.client.events.UploadCompleteHandler;
import org.iplantc.de.client.services.callbacks.DiskResourceServiceCallback;
import org.iplantc.de.client.services.callbacks.FileEditorServiceFacade;
import org.iplantc.de.client.views.dialogs.SaveAsDialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AnalysisParamView implements IsWidget {

    private static AnalysisParamViewUiBinder uiBinder = GWT.create(AnalysisParamViewUiBinder.class);

    interface AnalysisParamViewUiBinder extends UiBinder<Widget, AnalysisParamView> {
    }

    @UiField(provided = true)
    final ListStore<AnalysisParameter> listStore;

    @UiField(provided = true)
    final ColumnModel<AnalysisParameter> cm;

    @UiField
    Grid<AnalysisParameter> grid;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ToolBar menuToolBar;

    @UiField
    BorderLayoutData northData;

    @UiField
    Dialog dialog;

    @UiField
    TextButton btnSave;

    private final Widget widget;
    private Presenter presenter;

    public AnalysisParamView(ListStore<AnalysisParameter> listStore, ColumnModel<AnalysisParameter> cm) {
        this.cm = cm;
        this.listStore = listStore;
        this.widget = uiBinder.createAndBindUi(this);
        grid.getView().setEmptyText(I18N.DISPLAY.noParameters());
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void loadParameters(List<AnalysisParameter> items) {
        listStore.addAll(items);
    }

    public void show() {
        dialog.show();
    }

    public void setHeading(String heading) {
        dialog.setHeadingText(heading);
    }

    @UiHandler("btnSave")
    void onSaveClick(SelectEvent event) {
        final SaveAsDialog saveDialog = new SaveAsDialog(I18N.DISPLAY.saveAs(), I18N.DISPLAY.saveAs(),
                null, null);
        saveDialog.addOkClickHandler(new DialogOkClickHandler() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                String fileContents = writeTabFile();
                saveFile(saveDialog.getSelectedFolder().getId() + "/" + saveDialog.getNewName(),
                        fileContents);
            }
        });
        saveDialog.show();
        saveDialog.toFront();
    }

    private void saveFile(final String path, String fileContents) {
        FileEditorServiceFacade facade = new FileEditorServiceFacade();
        facade.uploadTextAsFile(path, fileContents, new SaveasServiceCallbackHandler(path));
    }

    private String writeTabFile() {
        StringBuilder sw = new StringBuilder();
        sw.append(I18N.DISPLAY.paramName() + "\t" + I18N.DISPLAY.paramType() + "\t"
                + I18N.DISPLAY.paramValue() + "\n");
        List<AnalysisParameter> params = grid.getStore().getAll();
        for (AnalysisParameter ap : params) {
            sw.append(ap.getName() + "\t" + ap.getType() + "\t" + ap.getValue() + "\n");
        }

        return sw.toString();
    }

    private class SaveasServiceCallbackHandler extends DiskResourceServiceCallback {

        private final String parentFolder;
        private final String fileName;

        public SaveasServiceCallbackHandler(String path) {
            this.fileName = DiskResourceUtil.parseNameFromPath(path);
            this.parentFolder = DiskResourceUtil.parseParent(path);
        }

        @Override
        public void onSuccess(String result) {
            JSONObject obj = JSONParser.parseStrict(result).isObject();
            UploadCompleteHandler uch = new DefaultUploadCompleteHandler(parentFolder);
            uch.onCompletion(fileName, JsonUtil.getObject(obj, "file").toString());
        }

        @Override
        protected String getErrorMessageDefault() {
            return I18N.ERROR.saveParamFailed();
        }

        @Override
        protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
            return getErrorMessageForFiles(code, fileName);
        }
    }

}
