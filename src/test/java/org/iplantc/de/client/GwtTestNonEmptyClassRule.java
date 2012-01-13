package org.iplantc.de.client;

import java.util.ArrayList;

import org.iplantc.core.client.widgets.validator.rules.NonEmptyClassRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestNonEmptyClassRule extends GWTTestCase {
    private NonEmptyClassRule buildRule() {
        return new NonEmptyClassRule(new ArrayList<String>());
    }

    public void testValidRule() {
        NonEmptyClassRule rule = buildRule();

        String result = rule.validate(null, "test", "{\"name\":\"Test Name\", \"type\":\"Test\"}"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleInvalid() {
        NonEmptyClassRule rule = buildRule();

        String result = rule.validate(null, "test", "{}"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleNull() {
        NonEmptyClassRule rule = buildRule();

        String result = rule.validate(null, "test", null); //$NON-NLS-1$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
