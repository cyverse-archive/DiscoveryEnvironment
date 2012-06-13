/**
 * 
 */
package org.iplantc.admin.belphegor.client.models;

import java.util.Date;

import org.iplantc.core.uicommons.client.util.DateParser;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

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
        set(CREATED_ON, DateParser.parseDate(genome.getCreatedOn()));
        set(PATH, genome.getPath());
        set(DELETED, genome.getDeleted());
        set(LAST_MODIFIED_BY, genome.getLastModifiedBy());
        set(LAST_MODIFIED_ON, DateParser.parseDate(genome.getLastModifiedOn()));
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put(ID, (get(ID) == null) ? new JSONString("") : new JSONString(get(ID).toString()));
        obj.put(UUID, (get(UUID) == null) ? new JSONString("") : new JSONString(get(UUID).toString()));
        obj.put(NAME, (get(NAME) == null) ? new JSONString("") : new JSONString(get(NAME).toString()));
        obj.put(CREATED_ON, (get(CREATED_ON) == null || get(CREATED_ON) == "0") ? new JSONString("")
                : new JSONString(((Date)get(CREATED_ON)).getTime() + ""));
        obj.put(CREATED_BY,
                (get(CREATED_BY) == null) ? new JSONString("") : new JSONString(get(CREATED_BY)
                        .toString()));
        obj.put(PATH, (get(PATH) == null) ? new JSONString("") : new JSONString(get(PATH).toString()));
        obj.put(DELETED,
                (get(DELETED) == null) ? JSONBoolean.getInstance(false) : JSONBoolean
                        .getInstance(Boolean.parseBoolean(get(DELETED).toString())));
        obj.put(LAST_MODIFIED_BY, (get(LAST_MODIFIED_BY) == null) ? new JSONString("") : new JSONString(
                get(LAST_MODIFIED_BY).toString()));
        obj.put(LAST_MODIFIED_ON,
                (get(LAST_MODIFIED_ON) == null || get(LAST_MODIFIED_ON) == "0") ? new JSONString("")
                        : new JSONString(((Date)get(LAST_MODIFIED_ON)).getTime() + ""));
        return obj;
    }
}
