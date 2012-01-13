package org.iplantc.de.client;

import java.util.Map;

import org.iplantc.core.client.widgets.metadata.WizardPropertyGroupContainer;
import org.iplantc.core.client.widgets.utils.ComponentValue;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestComponentValueTable extends GWTTestCase {
    private String buildJson() {
        StringBuffer ret = new StringBuffer();

        ret.append("{\"name\":\"Variant Detection Parameters\", \"groups\":["); //$NON-NLS-1$

        ret.append("{\"name\":\"Base Calling\", \"id\":\"idPanel0\", \"type\":\"step\",\"properties\":[" //$NON-NLS-1$
                + "{\"name\":\"Theta parameter (error dependency coefficient).<br> Enter a number between 0 and 1:\", \"id\":\"idThetaParam\", \"type\":\"Text\", \"value\":\".85\", \"propertytype\":\"double\", " //$NON-NLS-1$
                + "\"validator\" : {\"name\" : \"Theta parameter field\", \"required\" : true, \"rules\" : [{\"DoubleRange\" : [0.0,1.0]}]}}, " //$NON-NLS-1$
                + "{\"name\":\"Number of haplotypes in sample:\", \"type\":\"Text\", \"value\":\"2\", \"id\":\"idNumHaplotypes\", \"propertytype\":\"integer\", " //$NON-NLS-1$
                + "\"validator\" : {\"name\" : \"Number of haplotypes field\", \"required\" : true, \"rules\" : [{\"IntRange\" : [1,10]}]}}, " //$NON-NLS-1$
                + "]}"); //$NON-NLS-1$

        ret.append("]}"); //$NON-NLS-1$

        return ret.toString();
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    public void testTableBuild() {
        // setup
        WizardPropertyGroupContainer container = new WizardPropertyGroupContainer(buildJson());

        ComponentValueTable table = new ComponentValueTable(null);

        // seed
        table.seed(container);

        // test
        assertEquals(table.getNumValues(), 2);

        Map<String, ComponentValue> values = table.getValues();
        assertEquals(values.size(), 2);

        ComponentValue val = values.get("idThetaParam"); //$NON-NLS-1$
        assertTrue(val.getValue().equals(".85")); //$NON-NLS-1$

        val = values.get("idNumHaplotypes"); //$NON-NLS-1$
        assertTrue(val.getValue().equals("2")); //$NON-NLS-1$
    }

    public void testTableSet() {
        // setup
        WizardPropertyGroupContainer container = new WizardPropertyGroupContainer(buildJson());

        ComponentValueTable table = new ComponentValueTable(null);

        // seed
        table.seed(container);

        // test
        table.setValue("idThetaParam", ".75"); //$NON-NLS-1$ //$NON-NLS-2$

        Map<String, ComponentValue> values = table.getValues();

        ComponentValue val = values.get("idThetaParam"); //$NON-NLS-1$
        assertTrue(val.getValue().equals(".75")); //$NON-NLS-1$
    }
}
