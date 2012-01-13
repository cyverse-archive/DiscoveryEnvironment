package org.iplantc.de.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.JsFile;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestJsEvaluate extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    /**
     * Test Data from the application for JsFile: [{"id":"36805","name":"foo.nex","status":
     * "","type":"NEXUS with trait data","uploaded":"2010-04-24 11:14:37.191"}, {"id":"37122"
     * ,"name":"AminoAcid.nex","status":"","type":"NEXUS with unrecognized data"
     * ,"uploaded":"2010-04-24 11:46:26.338"}, {"id":"19791","name":"geospizamissingtraits.fel.nx"
     * ,"status":"","type":"NEXUS with trait data","uploaded":"2010-04-24 11:02:07.818"},
     * {"id":"37129","name":"DNA.nex","status":"","type":"NEXUS with unrecognized data",
     * "uploaded":"2010-04-24 11:46:47.318"}, {"id":"36298","name":"avian_ovomucoids.nex","status"
     * :"","type":"NEXUS with unrecognized data","uploaded":"2010-04-24 11:14:08.161"}, {"id"
     * :"36717","name":"Complex.nex","status":"","type":"NEXUS with tree data","uploaded"
     * :"2010-04-24 11:14:25.726"}, {"id":"22391","name":"PDAP.fel.tre","status":"","type":
     * "Newick","uploaded":"2010-04-24 11:03:48.631"}, {"id":"18523","name":"aq.tree.fel","status"
     * :"","type":"Newick","uploaded":"2010-04-24 10:51:46.071"},
     * {"id":"36289","name":"MesquiteEquivocalExample.nex"
     * ,"status":"","type":"NEXUS with tree data","uploaded":"2010-04-24 11:13:08.815"},
     * {"id":"654135","name":"M111c2x3x96c16c46c54.NX","status":"","type":
     * "NEXUS with tree data","uploaded":"2010-04-26 15:26:48.346"},
     * {"id":"21609","name":"shorebirds.fel.trait.nex"
     * ,"status":"","type":"NEXUS with trait data","uploaded":"2010-04-24 11:03:30.923"},
     * {"id":"19188","name":"PDAP.fel.nex","status":"","type":"NEXUS with trait data",
     * "uploaded":"2010-04-24 10:52:02.406"}, {"id":"18041","name":"aq.fel.trait.nex","status"
     * :"","type":"NEXUS with trait data","uploaded":"2010-04-24 10:51:32.508"}, {"id":"671461"
     * ,"name":"excel file for upload.csv","status":"","type":"CSV trait data"
     * ,"uploaded":"2010-04-26 16:30:24.804"}, {"id":"22765","name":"geospiza_valid.csv","status"
     * :"","type":"CSV trait data","uploaded":"2010-04-24 11:04:58.159"}]
     */
    public void testArrayOfWithJsFileSingleQuote() {
        JsArray<JsFile> fileInfos = JsonUtil
                .asArrayOf("[{'name':'foo', 'label':'tree1', 'uploaded':'', 'type':'Foo', 'id':'650'}," //$NON-NLS-1$
                        + "{'name':'foo', 'label':'tree2', 'uploaded':'', 'type':'Bar', 'id':'344'}]"); //$NON-NLS-1$
        assertNotNull(fileInfos);
        assertTrue(fileInfos.length() == 2);

        JsFile fi1 = fileInfos.get(0);
        assertTrue(fi1.getId().equals("650")); //$NON-NLS-1$
        assertTrue(fi1.getName().equals("foo")); //$NON-NLS-1$
        assertTrue(fi1.getLabel().equals("tree1")); //$NON-NLS-1$

        JsFile fi2 = fileInfos.get(1);
        assertTrue(fi2.getId().equals("344")); //$NON-NLS-1$
        assertTrue(fi2.getName().equals("foo")); //$NON-NLS-1$
    }

    /**
     * JsFile [{"id":"330", "name":"iptol180_gnetales.new", "status":"", "type":"Newick",
     * "uploaded":"2010-04-29 11:16:43.453"}, {"id":"294", "name":"gnetales.nex", "status":"",
     * "type":"NEXUS with tree data", "uploaded":"2010-04-29 11:16:22.533"}]
     */

    public void testArrayOfWithJsFileDoubleQuote() {
        JsArray<JsFile> fileInfos = JsonUtil.asArrayOf("[{\"name\":\"foo\", \"label\":\"tree1\", " //$NON-NLS-1$
                + "\"uploaded\":\"\", \"type\":\"Foo\", \"id\":\"650\"}," //$NON-NLS-1$
                + "{\"name\":\"foo\", \"label\":\"tree2\", " //$NON-NLS-1$
                + "\"uploaded\":\"\", \"type\":\"Bar\", \"id\":\"344\"}]"); //$NON-NLS-1$
        assertNotNull(fileInfos);
        assertTrue(fileInfos.length() == 2);

        JsFile fi1 = fileInfos.get(0);
        assertTrue(fi1.getId().equals("650")); //$NON-NLS-1$
        assertTrue(fi1.getName().equals("foo")); //$NON-NLS-1$
        assertTrue(fi1.getLabel().equals("tree1")); //$NON-NLS-1$

        JsFile fi2 = fileInfos.get(1);
        assertTrue(fi2.getId().equals("344")); //$NON-NLS-1$
        assertTrue(fi2.getName().equals("foo")); //$NON-NLS-1$

        fileInfos = JsonUtil.asArrayOf("[{\"id\":\"330\", " //$NON-NLS-1$
                + "\"name\":\"iptol180_gnetales.new\", \"status\":\"\", " //$NON-NLS-1$
                + "\"type\":\"Newick\", \"uploaded\":\"2010-04-29 11:16:43.453\"}," //$NON-NLS-1$
                + "{\"id\":\"294\", \"name\":\"gnetales.nex\", \"status\":\"\", " //$NON-NLS-1$
                + "\"type\":\"NEXUS with tree data\", " + "\"uploaded\":\"2010-04-29 11:16:22.533\"}]"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(fileInfos);
        assertTrue(fileInfos.length() == 2);

        fi2 = fileInfos.get(1);
        assertTrue(fi2.getId().equals("294")); //$NON-NLS-1$
        assertTrue(fi2.getName().equals("gnetales.nex")); //$NON-NLS-1$

        // empty array
        fileInfos = JsonUtil.asArrayOf("[]"); //$NON-NLS-1$
        assertNotNull(fileInfos);
        assertTrue(fileInfos.length() == 0);
    }

}
