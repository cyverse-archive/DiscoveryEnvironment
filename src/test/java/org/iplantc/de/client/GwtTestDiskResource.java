package org.iplantc.de.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.core.uidiskresource.client.models.Permissions;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestDiskResource extends GWTTestCase {

    private String folder = "  {\n"
            + "            \"id\": \"/iplant/home/ipctest/analyses/johnw7-2011-11-07-22-00-50.632/logs\",\n"
            + "            \"label\": \"logs\",\n" + "            \"permissions\": {\n"
            + "                \"read\": true,\n" + "                \"write\": false\n"
            + "            },\n" + "            \"hasSubDirs\": false,\n"
            + "            \"date-created\": \"1320703410000\",\n"
            + "            \"date-modified\": \"1320703410000\"\n" + "        }";

    private String file = "  {\n"
            + "            \"id\": \"/iplant/home/ipctest/analyses/johnw7-2011-11-07-22-00-50.632/accepted_hits.sam.txt.sorted.bam\",\n"
            + "            \"label\": \"accepted_hits.sam.txt.sorted.bam\",\n"
            + "            \"permissions\": {\n" + "                \"read\": true,\n"
            + "                \"write\": true\n" + "            },\n"
            + "            \"date-created\": \"1320703411000\",\n"
            + "            \"date-modified\": \"1320703411000\",\n"
            + "            \"file-size\": \"278\"\n"
            + "        }";



        @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    
    public void testFileWithPermissions() {
        JSONObject obj = JSONParser.parseStrict(file).isObject();
        File f_json = new File(obj);
        File f_obj = new File(JsonUtil.getString(obj, "id"), JsonUtil.getString(obj, "label"),
                new Permissions(JsonUtil.getObject(obj, "permissions")));

        assertTrue("accepted_hits.sam.txt.sorted.bam".equals(f_json.getName()));
        assertTrue("/iplant/home/ipctest/analyses/johnw7-2011-11-07-22-00-50.632/accepted_hits.sam.txt.sorted.bam"
                .equals(f_json
                .getId()));
        assertTrue(f_json.getSize().equals(278l));
        assertTrue(f_json.getId().equals(f_obj.getId()));
        assertTrue(f_json.getName().equals(f_obj.getName()));
        assertNotNull(f_obj.getPermissions());
        assertNotNull(f_json.getPermissions());
        assertEquals(true, f_obj.getPermissions().isReadable());
        assertEquals(true, f_obj.getPermissions().isWritable());
        assertEquals(true, f_json.getPermissions().isReadable());
        assertEquals(true, f_json.getPermissions().isWritable());
    }

    public void testFolderWithPermissions() {
        JSONObject obj = JSONParser.parseStrict(folder).isObject();
        Folder f_json = new Folder(obj);
        Folder f_obj = new Folder(JsonUtil.getString(obj, "id"), JsonUtil.getString(obj, "label"),
                false, new Permissions(JsonUtil.getObject(obj, "permissions")));

        assertTrue("logs".equals(f_json.getName()));
        assertTrue("/iplant/home/ipctest/analyses/johnw7-2011-11-07-22-00-50.632/logs"
                .equals(f_json.getId()));

        assertTrue(f_json.getId().equals(f_obj.getId()));
        assertTrue(f_json.getName().equals(f_obj.getName()));
        assertNotNull(f_obj.getPermissions());
        assertNotNull(f_json.getPermissions());
        assertEquals(true, f_obj.getPermissions().isReadable());
        assertEquals(false, f_obj.getPermissions().isWritable());
        assertEquals(true, f_json.getPermissions().isReadable());
        assertEquals(false, f_json.getPermissions().isWritable());
    }

}
