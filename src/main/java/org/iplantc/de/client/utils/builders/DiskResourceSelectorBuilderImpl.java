package org.iplantc.de.client.utils.builders;

import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.de.client.views.FileSelector;
import org.iplantc.de.client.views.FolderSelector;
import org.iplantc.de.client.views.WizardFileSelector;
import org.iplantc.de.client.views.WizardFolderSelector;
import org.iplantc.de.client.views.dialogs.FileSelectDialog;

import com.google.gwt.user.client.Command;

/**
 * 
 * A file selector builder that knows to build a right Implementation of IFileSelector implementation
 * 
 * @author sriram
 * 
 */
public class DiskResourceSelectorBuilderImpl implements IDiskResourceSelectorBuilder {
    /**
     * {@inheritDoc}
     */
    @Override
    public IFileSelector buildFileSelector(Command cmdChange) {
        return new FileSelector(cmdChange, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFileSelector buildFileSelector(Property property, ComponentValueTable tblComponentVals) {
        return new WizardFileSelector(property, tblComponentVals, property.getId());
    }

    /**
     * {@inheritDoc}
     */
    public IFolderSelector buildFolderSelector(Command cmdChange) {
        return new FolderSelector(cmdChange, null);
    }

    /**
     * {@inheritDoc}
     * 
     */
    public IFolderSelector buildFolderSelector(final Property property,
            final ComponentValueTable tblComponentVals) {
        return new WizardFolderSelector(property, tblComponentVals, property.getId());
    }

    @Override
    public IFileSelectDialog buildFileSelectorDialog(String tag, String caption, File file,
            String currentFolderId) {
        return new FileSelectDialog(tag, caption, file, currentFolderId);
    }
}
