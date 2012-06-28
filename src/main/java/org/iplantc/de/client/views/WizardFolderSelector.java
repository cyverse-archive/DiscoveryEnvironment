package org.iplantc.de.client.views;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.Permissions;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.google.gwt.event.dom.client.KeyCodes;

public class WizardFolderSelector extends FolderSelector {
    private final ComponentValueTable tblComponentVals;

    /**
     * Instantiate from a property and component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardFolderSelector(final Property property, final ComponentValueTable tblComponentVals,
            String tag) {
        super(tag);
        this.tblComponentVals = tblComponentVals;
        setId(property.getId());
        initValidator(property);
        tblComponentVals.setFormatter(getId(), new GeneralTextFormatter());
    }

    private void initValidator(final Property property) {
        IPlantValidator validator = buildValidator(property);

        if (validator != null) {
            setValidator(validator);

            tblComponentVals.setValidator(getId(), validator);
        }
    }

    private IPlantValidator buildValidator(final Property property) {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator validator = property.getValidator();

        if (validator != null) {
            ret = new IPlantValidator(tblComponentVals, validator);
        }

        return ret;
    }

    private void handleSelectedFolderChange() {
        String idFile = (getSelectedFolderId() == null) ? "" : getSelectedFolderId(); //$NON-NLS-1$
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

                    setSelectedFolder(new Folder("", "", false, new Permissions(true, true, true)));
                    txtResourceName.setValue(getSelectedResourceName());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedFolder(Folder folder) {
        super.setSelectedFolder(folder);

        handleSelectedFolderChange();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayFolderName(String name) {
        super.displayFolderName(name);

        tblComponentVals.setValue(getId(), name);

        tblComponentVals.validate();
    }
}
