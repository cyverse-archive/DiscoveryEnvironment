package org.iplantc.de.client;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.validator.rules.IntRangeRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestIntRangeRule extends GWTTestCase {
    private IntRangeRule buildRule(String top, String bottom) {
        List<String> params = new ArrayList<String>();

        if (top != null) {
            params.add(top);
        }

        if (bottom != null) {
            params.add(bottom);
        }

        return new IntRangeRule(params);
    }

    public void testValidRule() {
        IntRangeRule rule = buildRule("1", "10"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "6"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleOutOfRange() {
        IntRangeRule rule = buildRule("1", "10"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "-1"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalid() {
        IntRangeRule rule = buildRule("1", "10"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "foo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleNullParamsValid() {
        IntRangeRule rule = buildRule(null, null);

        String result = rule.validate(null, "test", "0"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleNullParamsInvalid() {
        IntRangeRule rule = buildRule(null, null);

        String result = rule.validate(null, "test", "1"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
