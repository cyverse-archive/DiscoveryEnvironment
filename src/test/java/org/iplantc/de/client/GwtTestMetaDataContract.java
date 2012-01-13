package org.iplantc.de.client;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.contracts.MetaDataContract;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestMetaDataContract extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    private MetaDataContract allocateContract(String json) {
        JSONObject objJson = JsonUtil.getObject(json);

        return new MetaDataContract(objJson);
    }

    public void testMetadataContractValid() {
        String json = "{\"id\": \"idCheckbox\", \"type\": \"disableOnSelection\", \"params\": [\"idField0\",\"idField1\",\"idField2\"]}"; //$NON-NLS-1$

        MetaDataContract test = allocateContract(json);

        assertNotNull(test);
        assertTrue(test.getId().equals("idCheckbox")); //$NON-NLS-1$
        assertTrue(test.getType().equals("disableOnSelection")); //$NON-NLS-1$
        assertEquals(test.getNumParams(), 3);

        List<String> params = test.getParams();
        assertNotNull(params);

        String param = params.get(0);
        assertTrue(param.equals("idField0")); //$NON-NLS-1$

        param = params.get(1);
        assertTrue(param.equals("idField1")); //$NON-NLS-1$

        param = params.get(2);
        assertTrue(param.equals("idField2")); //$NON-NLS-1$
    }

    public void testMetadataContractNoParams() {
        String json = "{\"id\": \"idCheckbox\", \"type\": \"disableOnSelection\"}"; //$NON-NLS-1$

        MetaDataContract test = allocateContract(json);

        assertNotNull(test);
        assertTrue(test.getId().equals("idCheckbox")); //$NON-NLS-1$
        assertTrue(test.getType().equals("disableOnSelection")); //$NON-NLS-1$
        assertEquals(test.getNumParams(), 0);

        List<String> params = test.getParams();
        assertNotNull(params);
        assertEquals(params.size(), 0);
    }

    public void testMetadataContractEmptyParams() {
        String json = "{\"id\": \"idCheckbox\", \"type\": \"disableOnSelection\", \"params\": []}"; //$NON-NLS-1$

        MetaDataContract test = allocateContract(json);

        assertNotNull(test);
        assertTrue(test.getId().equals("idCheckbox")); //$NON-NLS-1$
        assertTrue(test.getType().equals("disableOnSelection")); //$NON-NLS-1$
        assertEquals(test.getNumParams(), 0);

        List<String> params = test.getParams();
        assertNotNull(params);
        assertEquals(params.size(), 0);
    }
}
