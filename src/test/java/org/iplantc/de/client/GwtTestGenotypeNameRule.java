package org.iplantc.de.client;

import java.util.ArrayList;

import org.iplantc.core.client.widgets.validator.rules.GenotypeNameRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestGenotypeNameRule extends GWTTestCase {
    private GenotypeNameRule buildRule() {
        return new GenotypeNameRule(new ArrayList<String>());
    }

    public void testRuleNull() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", null); //$NON-NLS-1$

        assertNotNull(result);
    }

    public void testRuleSpaceBegin() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", " foo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleSpaceMiddle() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "space middle"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleSpaceEnd() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "foo "); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleNumber() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "46"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleUnderscore() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "has_"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleHyphen() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "has-"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleHash() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "has#"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleValid() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "fooo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleInvalidShort() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "foo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalidDollarSign() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "foo$"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalidParen() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "foo("); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalidPrecent() {
        GenotypeNameRule rule = buildRule();

        String result = rule.validate(null, "test", "foo%"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
