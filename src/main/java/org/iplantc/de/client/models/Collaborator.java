/**
 * 
 */
package org.iplantc.de.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * A model class to store collaborators attributes
 * 
 * @author sriram
 * 
 */
public class Collaborator extends BaseModelData {

    /**
     * 
     */
    private static final long serialVersionUID = 3956502462940674969L;

    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String USERNAME = "userName";
    public static final String NAME = "name";

    public Collaborator() {

    }

    public String getId() {
        return get(ID);
    }

    /**
     * Get users full name formatted
     * 
     * @return get first name + last name
     */
    public String getName() {
        return get(NAME);
    }

    public String getEmail() {
        return get(EMAIL);
    }

    public String getUserName() {
        return get(USERNAME);
    }
}
