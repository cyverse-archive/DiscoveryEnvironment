/**
 * 
 */
package org.iplantc.admin.belphegor.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * @author sriram
 * 
 */
public class ReferenceGenome extends BaseModelData {

    /**
     * 
     */
    private static final long serialVersionUID = 7154290998256966774L;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_ON = "created_on";
    public static final String PATH = "path";
    public static final String DELETED = "deleted";
    public static final String LAST_MODIFIED_ON = "last_modified";
    public static final String LAST_MODIFIED_BY = "last_modified_by";
    public static final String UUID = "uuid";

    public ReferenceGenome(JsReferenceGenome genome) {
        set(ID, genome.getId());
        set(UUID, genome.getUUID());
        set(NAME, genome.getName());
        set(CREATED_BY, genome.getUserName());
        set(CREATED_ON, genome.getCreatedOn());
        set(PATH, genome.getPath());
        set(DELETED, genome.getDeleted());
        set(LAST_MODIFIED_BY, genome.getLastModifiedBy());
        set(LAST_MODIFIED_ON, genome.getLastModifiedOn());
    }
}
