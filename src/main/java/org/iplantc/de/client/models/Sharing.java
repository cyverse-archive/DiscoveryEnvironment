package org.iplantc.de.client.models;

import org.iplantc.core.uidiskresource.client.models.Permissions;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * 
 * A class that models sharing
 * 
 * @author sriram
 * 
 */
public class Sharing extends BaseModelData {

    public static final String NAME = "name";
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String OWN = "own";

    /**
     * 
     */
    private static final long serialVersionUID = -2830576775118848275L;
    private Collaborator collaborator;
    private Permissions permission;

    public Sharing(Collaborator c, Permissions p) {
        this.collaborator = c;
        this.permission = p;

        set(NAME, collaborator.getName());
        set(READ, permission.isReadable());
        set(WRITE, permission.isWritable());
        set(OWN, permission.isOwner());
    }

    public boolean isReadable() {
        return Boolean.parseBoolean(get(READ).toString());
    }

    public boolean isWritable() {
        return Boolean.parseBoolean(get(WRITE).toString());
    }

    public boolean isOwner() {
        return Boolean.parseBoolean(get(OWN).toString());
    }

    public void setReadable(boolean read) {
        set(READ, read);
        set(WRITE, false);
        set(OWN, false);
    }

    public void setWritable(boolean write) {
        if (write) {
            set(READ, true);
        }
        set(WRITE, write);
        set(OWN, false);
    }

    public void setOwner(boolean own) {
        if (own) {
            set(READ, true);
            set(WRITE, true);
        }
        set(OWN, own);
    }

    public String getUserName() {
        return collaborator.getUserName();
    }

    public String getName() {
        return collaborator.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Sharing)) {
            return false;
        }
        Sharing s = (Sharing)o;
        return getUserName().equals(s.getUserName());
    }

}
