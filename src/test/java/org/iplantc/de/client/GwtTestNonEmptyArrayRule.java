package org.iplantc.de.client;

import java.util.ArrayList;

import org.iplantc.core.client.widgets.validator.rules.NonEmptyArrayRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestNonEmptyArrayRule extends GWTTestCase {
    private NonEmptyArrayRule buildRule() {
        return new NonEmptyArrayRule(new ArrayList<String>());
    }

    public void testValidRule() {
        NonEmptyArrayRule rule = buildRule();

        String result = rule.validate(null, "test", "[\"foo\", \"bar\"]"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleOutOfRange() {
        NonEmptyArrayRule rule = buildRule();

        String result = rule.validate(null, "test", "[]"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleNull() {
        NonEmptyArrayRule rule = buildRule();

        String result = rule.validate(null, "test", null); //$NON-NLS-1$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
