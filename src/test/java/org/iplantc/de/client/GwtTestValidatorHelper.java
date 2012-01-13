package org.iplantc.de.client;

import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestValidatorHelper extends GWTTestCase {
    public void testIsIntegerNull() {
        boolean result = ValidatorHelper.isInteger(null);

        assertFalse(result);
    }

    public void testIsIntegerValid() {
        boolean result = ValidatorHelper.isInteger("14"); //$NON-NLS-1$

        assertTrue(result);
    }

    public void testIsIntegerInvalid() {
        boolean result = ValidatorHelper.isInteger("foo"); //$NON-NLS-1$

        assertFalse(result);
    }

    public void testIsIntegerWithDouble() {
        boolean result = ValidatorHelper.isInteger("1.6"); //$NON-NLS-1$

        assertFalse(result);
    }

    public void testIsDoubleNull() {
        boolean result = ValidatorHelper.isDouble(null);

        assertFalse(result);
    }

    public void testIsDoubleValid() {
        boolean result = ValidatorHelper.isDouble("14.0"); //$NON-NLS-1$

        assertTrue(result);
    }

    public void testIsDoubleValidWithInteger() {
        boolean result = ValidatorHelper.isDouble("14"); //$NON-NLS-1$

        assertTrue(result);
    }

    public void testIsDoubleInvalid() {
        boolean result = ValidatorHelper.isDouble("foo"); //$NON-NLS-1$

        assertFalse(result);
    }

    public void testBuildValidatorNull() {
        MetaDataValidator result = ValidatorHelper.buildValidator(null);

        assertNull(result);
    }

    public void testBuildValidatorValid() {
        String json = "{\"name\": \"\", \"required\": true, \"rules\": [{\"NonEmptyArray\": []}]}"; //$NON-NLS-1$
        MetaDataValidator result = ValidatorHelper.buildValidator(json);

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
