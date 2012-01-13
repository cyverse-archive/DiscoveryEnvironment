package org.iplantc.de.client;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.validator.rules.DoubleRangeRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestDoubleRangeRule extends GWTTestCase {
    private DoubleRangeRule buildRule(String top, String bottom) {
        List<String> params = new ArrayList<String>();

        if (top != null) {
            params.add(top);
        }

        if (bottom != null) {
            params.add(bottom);
        }

        return new DoubleRangeRule(params);
    }

    public void testValidRule() {
        DoubleRangeRule rule = buildRule("0.0", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "0.5"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleOutOfRange() {
        DoubleRangeRule rule = buildRule("0.0", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "-1.0"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalid() {
        DoubleRangeRule rule = buildRule("0.0", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

        String result = rule.validate(null, "test", "foo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleNullParamsValid() {
        DoubleRangeRule rule = buildRule(null, null);

        String result = rule.validate(null, "test", "0.0"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleNullParamsInvalid() {
        DoubleRangeRule rule = buildRule(null, null);

        String result = rule.validate(null, "test", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
