package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Folder;

public interface IDiskResourcePresenter {

    public Folder findFolder(final String folderId);

    public void selectFolder(final String folderPath);

    public void addDiskResource(final String idParentFolder, final DiskResource resource);

    public void expandFolder(final Folder target);

    public void renameFolder(final String folderId, final String newFolderName);

    public void renameFile(final String fileId, final String newFileName);

    public String getCurrentPath();

    public Folder getRootFolder();

    public String getRootFolderId();

}
