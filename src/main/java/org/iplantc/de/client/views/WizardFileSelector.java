package org.iplantc.de.client.views;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.dnd.StatusProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.google.gwt.event.dom.client.KeyCodes;

/**
 * Custom file selection widget for wizards.
 * 
 * @author amuir
 * 
 */
public class WizardFileSelector extends FileSelector {
    private final ComponentValueTable tblComponentVals;
    private final Property property;

    /**
     * Instantiate from a property and component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardFileSelector(final Property property, final ComponentValueTable tblComponentVals,
            String tag) {
        super(tag);
        this.tblComponentVals = tblComponentVals;
        this.property = property;
        setId(property.getId());
        initValidator();
        tblComponentVals.setFormatter(getId(), new GeneralTextFormatter());
        addDragDop();
        addTextFieldPlugIn();
    }

    private void addTextFieldPlugIn() {
        txtResourceName.setData("text", I18N.DISPLAY.dragAndDropPrompt());
        txtResourceName.addPlugin(getFileSelectorPlugin());
    }

    private void addDragDop() {
        new DropTarget(getWidget()) {

            @Override
            protected void onDragMove(DNDEvent event) {
                super.onDragMove(event);
                List<ModelData> files = event.getData();

                // do not allow drop anything other than file and not more than 1 file
                if (files == null || files.size() > 1 || !(files.get(0) instanceof File)) {
                    event.setCancelled(true);
                    StatusProxy eventStatus = event.getStatus();
                    eventStatus.setStatus(false);
                    return;
                }

            }

            @Override
            protected void onDragDrop(DNDEvent event) {
                super.onDragDrop(event);
                List<ModelData> files = event.getData();
                if (files != null && files.get(0) instanceof File) {
                    File f = (File)files.get(0);
                    doSelection(f, DiskResourceUtil.parseParent(f.getId()));
                }
            }

        };
    }

    private ComponentPlugin getFileSelectorPlugin() {
        ComponentPlugin plugin = new ComponentPlugin() {
            @Override
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {
                    @Override
                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-component", 3);
                        // should style in external CSS rather than directly
                        elem.appendChild(XDOM
                                .create("<div style='color: #615f5f;padding: 1 0 2 0px; font-style:italic;' >"
                                        + be.getComponent().getData("text") + "</div>"));
                    }
                });
            }
        };
        return plugin;
    }

    private void initValidator() {
        IPlantValidator validator = buildValidator();

        if (validator != null) {
            setValidator(validator);

            tblComponentVals.setValidator(getId(), validator);
        }
    }

    private IPlantValidator buildValidator() {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator validator = property.getValidator();

        if (validator != null) {
            ret = new IPlantValidator(tblComponentVals, validator);
        }

        return ret;
    }

    private void handleSelectedFileChange() {
        String idFile = (getSelectedFileId() == null) ? "" : getSelectedFileId(); //$NON-NLS-1$
        tblComponentVals.setValue(getId(), idFile);

        // after we update the table, we need to validate the entire table
        tblComponentVals.validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initWidgets() {
        super.initWidgets();

        txtResourceName.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if ((event.getKeyCode() == KeyCodes.KEY_BACKSPACE)
                        || (event.getKeyCode() == KeyCodes.KEY_DELETE)) {

                    setSelectedFile(new File("", "", new Permissions(true, true, true)));
                    txtResourceName.setValue(getSelectedResourceName());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedFile(File file) {
        super.setSelectedFile(file);

        handleSelectedFileChange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayFilename(String name) {
        super.displayFilename(name);

        tblComponentVals.setValue(getId(), name);

        tblComponentVals.validate();
    }

    private void doSelection(File f, String currentFolderId) {
        setSelectedFile(f);
        setCurrentFolderId(currentFolderId);
        txtResourceName.setValue(getSelectedResourceName());

        if (cmdChange != null) {
            cmdChange.execute();
        }
    }

}