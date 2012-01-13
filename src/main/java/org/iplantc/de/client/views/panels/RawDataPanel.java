package org.iplantc.de.client.views.panels;

import java.util.List;
import java.util.Map;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.dialogs.IPlantDialog;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.controllers.DataMonitor;
import org.iplantc.de.client.events.FileEditorWindowDirtyEvent;
import org.iplantc.de.client.services.RawDataServices;
import org.iplantc.de.client.utils.NotifyInfo;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Provides a user interface for presenting raw data.
 * 
 * @author amuir
 * 
 */
public class RawDataPanel extends ProvenanceContentPanel implements DataMonitor {
    private final String data;
    private TextArea areaData;
    private String textOrig = new String();
    private final boolean editable;
    private final MessageBox wait;
    private int tabIndex;

    /**
     * Instantiate from a file identifier, data and editable flag.
     * 
     * @param fileIdentifier file associated with this panel.
     * @param data data to display.
     * @param editable true if the user can edit this data.
     */
    public RawDataPanel(FileIdentifier fileIdentifier, String data, boolean editable) {
        super(fileIdentifier);
        this.data = data;
        this.editable = editable;

        wait = MessageBox.wait(I18N.DISPLAY.progress(), I18N.DISPLAY.fileSaveProgress(),
                I18N.DISPLAY.saving() + "..."); //$NON-NLS-1$
        wait.close();

        buildTextArea();

        setTabIndex(0);
    }

    private void buildTextArea() {
        areaData = buildTextArea(editable);
        areaData.setId("idRawDataField"); //$NON-NLS-1$

        // we don't need to listen for changes if we are not editable
        if (editable) {
            areaData.addListener(Events.OnKeyUp, new Listener<FieldEvent>() {
                @Override
                public void handleEvent(FieldEvent be) {
                    String text = areaData.getValue();

                    if (!text.equals(textOrig)) {
                        textOrig = text;

                        // don't fire event if we are already dirty
                        if (!dirty) {
                            dirty = true;
                            EventBus eventbus = EventBus.getInstance();
                            FileEditorWindowDirtyEvent event = new FileEditorWindowDirtyEvent(
                                    fileIdentifier.getFileId(), true);
                            eventbus.fireEvent(event);
                        }
                    }
                }
            });
        }
    }

    private void doSave() {
        if (areaData != null) {
            String body = areaData.getValue();

            if (fileIdentifier != null) {
                wait.show();
                RawDataServices.saveRawData(fileIdentifier.getFileId(), fileIdentifier.getFilename(),
                        body, new AsyncCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                EventBus eventbus = EventBus.getInstance();
                                FileEditorWindowDirtyEvent event = new FileEditorWindowDirtyEvent(
                                        fileIdentifier.getFileId(), false);
                                eventbus.fireEvent(event);
                                wait.close();
                                NotifyInfo.display(I18N.DISPLAY.save(), I18N.DISPLAY.fileSave());
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                ErrorHandler.post(I18N.ERROR.rawDataSaveFailed(), caught);
                                wait.close();
                            }
                        });
            }
        }
    }

    private void promptSaveAs() {
        IPlantDialog dlg = new IPlantDialog(I18N.DISPLAY.saveAs(), 340, new RawDataSaveAsDialogPanel(
                fileIdentifier, areaData.getValue(), wait));
        dlg.show();
    }

    private ToolBar buildToolbar() {
        ToolBar ret = new ToolBar();
        final int TOOLBAR_HEIGHT = 24;

        ret.setWidth(getWidth());
        ret.setHeight(TOOLBAR_HEIGHT);

        ret.add(new Button(I18N.DISPLAY.save(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                doSave();
            }
        }));

        // add our Save As button
        ret.add(new Button(I18N.DISPLAY.saveAs(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                promptSaveAs();
            }
        }));

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        if (data != null) {
            textOrig = data;
            areaData.setValue(data);
            areaData.setWidth(getWidth());

            ContentPanel panel = new ContentPanel();
            panel.setHeaderVisible(false);
            panel.setLayout(new FitLayout());
            panel.setWidth(getWidth());
            panel.add(areaData);

            if (editable) {
                panel.setTopComponent(buildToolbar());
            }

            add(panel, centerData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        areaData.el().setElementAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabHeader() {
        return I18N.DISPLAY.raw();
    }

    /**
     * {@inheritDoc}
     */
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
    public void addFile(String idParentFolder, File info) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileSavedAs(String idOrig, String idParent, File info) {
        wait.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void folderCreated(String idParentFolder, JSONObject jsonFolder) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fileRename(String id, String name) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void folderRename(String id, String name) {
        // intentionally do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteResources(List<String> folders, List<String> files) {
        // intentionally do nothing
    }

    @Override
    public void fileMove(Map<String, String> files) {
        // intentionally do nothing
    }

    @Override
    public void folderMove(Map<String, String> folders) {
        // intentionally do nothing
    }
}
