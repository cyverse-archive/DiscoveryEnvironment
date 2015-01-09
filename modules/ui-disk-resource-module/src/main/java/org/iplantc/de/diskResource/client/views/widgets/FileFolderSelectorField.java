package org.iplantc.de.diskResource.client.views.widgets;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.commons.client.events.LastSelectedPathChangedEvent;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorDialogFactory;
import org.iplantc.de.diskResource.client.views.dialogs.FileFolderSelectDialog;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.TakesValue;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.widget.core.client.event.HideEvent;

import java.util.List;
import java.util.Set;

/**
 * @author jstroot
 */
public class FileFolderSelectorField extends AbstractDiskResourceSelector<DiskResource> {

    public interface FileFolderSelectorFieldAppearance {

        String dataDragDropStatusText(int size);

        String emptyText();
    }

    private class HideHandler implements HideEvent.HideHandler {
        private final TakesValue<DiskResource> dlg;
        private final HasValueChangeHandlers<DiskResource> hasValueChangeHandlers;
        private final UserSettings userSettings;
        private final EventBus eventBus;

        public HideHandler(final TakesValue<DiskResource> dlg,
                           final HasValueChangeHandlers<DiskResource> hasValueChangeHandlers,
                           final UserSettings userSettings,
                           final EventBus eventBus) {
            this.dlg = dlg;
            this.hasValueChangeHandlers = hasValueChangeHandlers;
            this.userSettings = userSettings;
            this.eventBus = eventBus;
        }

        @Override
        public void onHide(HideEvent event) {
            final DiskResource value = dlg.getValue();
            if(value == null){
                return;
            }

            setSelectedResource(value);
            if((value instanceof Folder)
                   && userSettings.isRememberLastPath()){
                userSettings.setLastPath(value.getPath());
                eventBus.fireEvent(new LastSelectedPathChangedEvent(true));
            }
            ValueChangeEvent.fire(hasValueChangeHandlers, value);

        }
    }

    @Inject DiskResourceSelectorDialogFactory selectorDialogFactory;
    @Inject UserSettings userSettings;
    @Inject EventBus eventBus;
    private final FileFolderSelectorFieldAppearance appearance;
    private final List<InfoType> infoTypeFilters;

    @Inject
    FileFolderSelectorField(final FileFolderSelectorFieldAppearance appearance,
                            @Assisted final List<InfoType> infoTypeFilters){
        this.appearance = appearance;
        this.infoTypeFilters = infoTypeFilters;
        setEmptyText(appearance.emptyText());
    }

    @Override
    public void onDrop(DndDropEvent event) {
        Set<DiskResource> dropData = getDropData(event.getData());

        if (!validateDropStatus(dropData, event.getStatusProxy())) {
            return;
        }

        DiskResource diskResource = dropData.iterator().next();
        setSelectedResource(diskResource);
        ValueChangeEvent.fire(this, diskResource);
    }

    @Override
    protected void onBrowseSelected() {
        final DiskResource value = getValue();
        HasPath folderToSelect = null;
        List<DiskResource> diskResourcesToSelect = Lists.newArrayList();
        if ((value == null || Strings.isNullOrEmpty(value.getPath()))
                && userSettings.isRememberLastPath()) {
            String path = userSettings.getLastPath();
            if(path != null) {
                folderToSelect = CommonModelUtils.getInstance().createHasPathFromString(path);
                // get dialog from factory
            }
        }else if(value instanceof Folder){
            folderToSelect = value;
        }else {
            diskResourcesToSelect.add(value);
        }
        FileFolderSelectDialog dlg = selectorDialogFactory.createFileFolderSelectDialog(folderToSelect,
                                                                                        diskResourcesToSelect,
                                                                                        infoTypeFilters);
        dlg.addHideHandler(new HideHandler(dlg, this, userSettings, eventBus));
        dlg.show();
    }

    @Override
    protected boolean validateDropStatus(Set<DiskResource> dropData, StatusProxy status) {
        boolean isValid = false;
        // Only allow 1 folder to be dropped in this field.
        if ((dropData == null)
                || dropData.size() != 1) {
            isValid = false;
        } else if(!infoTypeFilters.isEmpty()) {
            DiskResource droppedResource = dropData.iterator().next();
            InfoType infoType = InfoType.fromTypeString(droppedResource.getInfoType());
            for (InfoType it : infoTypeFilters) {
                // If the filter list is empty, this code will not be executed
                if (it.equals(infoType)) {
                    // Reset status message
                    status.setStatus(true);
                    status.update(appearance.dataDragDropStatusText(dropData.size()));
                    return true;
                }
            }
        } else {
            isValid = true;
        }

        // Reset status message
        status.setStatus(isValid);
        if(isValid){
            status.update(appearance.dataDragDropStatusText(dropData.size()));
        }

        return isValid;
    }
}
