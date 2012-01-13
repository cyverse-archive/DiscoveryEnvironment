package org.iplantc.de.client;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.validator.rules.IntAboveRule;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestIntAboveRule extends GWTTestCase {
    private IntAboveRule buildRule(String in) {
        List<String> params = new ArrayList<String>();

        if (in != null) {
            params.add(in);
        }

        return new IntAboveRule(params);
    }

    public void testValidRule() {
        IntAboveRule rule = buildRule("0"); //$NON-NLS-1$

        String result = rule.validate(null, "test", "1"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleOutOfRange() {
        IntAboveRule rule = buildRule("0"); //$NON-NLS-1$

        String result = rule.validate(null, "test", "-1"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleInvalid() {
        IntAboveRule rule = buildRule("0"); //$NON-NLS-1$

        String result = rule.validate(null, "test", "foo"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleEmpty() {
        IntAboveRule rule = buildRule("0"); //$NON-NLS-1$

        String result = rule.validate(null, "test", ""); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    public void testRuleNullValidateAboveZero() {
        IntAboveRule rule = buildRule(null);

        String result = rule.validate(null, "test", "1"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNull(result);
    }

    public void testRuleCausingIntOverflow() {
    	IntAboveRule rule = buildRule("0");
    	
    	String result = rule.validate(null, "test", "123456789");
    	assertNull(result);
    	result = rule.validate(null, "test", "1234567890");
    	assertNull(result);
    	result = rule.validate(null, "test", "1794567890");
    	assertNull(result);
    	result = rule.validate(null, "test", "1999999999");
    	assertNull(result);
    	// boundary test - MAX VALUE should be accepted: 2147483647 
    	result = rule.validate(null, "test", ""+Integer.MAX_VALUE);
    	assertNull(result);
    	// we expect this to fail as it is greater than the MAX VALUE
    	result = rule.validate(null, "test", ""+(Integer.MAX_VALUE+1));
    	assertNotNull(result);
    	
    	// the implementation uses the Integer classes parseInt()
    	// the commented region below will throw an exception
//        try {
//            Integer.parseInt("19999999999");
//        } catch (NumberFormatException nfe) {
//            //fail();
//        	System.out.println("failure?!?!  Wow!");
//        }
//    	
//    	result = rule.validate(null, "test", ""+(Integer.MAX_VALUE+1));
//    	assertNull(result);
//
//    	                       //             BA987654321
//    	result = rule.validate(null, "test", "19999999999");
//    	assertNull(result);
//
//    	result = rule.validate(null, "test", "199999999990");
//    	assertNull(result);
    }
    
    public void testRuleNullValidateZero() {
        IntAboveRule rule = buildRule(null);

        String result = rule.validate(null, "test", "0"); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(result);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
