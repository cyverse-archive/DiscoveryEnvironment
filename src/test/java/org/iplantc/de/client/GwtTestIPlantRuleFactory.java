package org.iplantc.de.client;

import org.iplantc.core.client.widgets.factory.IPlantRuleFactory;
import org.iplantc.core.client.widgets.validator.rules.DoubleRangeRule;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.client.widgets.validator.rules.IntAboveRule;
import org.iplantc.core.client.widgets.validator.rules.IntRangeRule;
import org.iplantc.core.client.widgets.validator.rules.NonEmptyArrayRule;
import org.iplantc.core.client.widgets.validator.rules.NonEmptyClassRule;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.validation.MetaDataRule;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestIPlantRuleFactory extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    private MetaDataRule allocateMetaDataRule(final String json) {
        JSONObject objJson = JsonUtil.getObject(json);

        return new MetaDataRule(objJson);
    }

    public void testBuildNullRule() {
        IPlantRule rule = IPlantRuleFactory.build(null);
        assertNull(rule);
    }

    public void testBuildIntRange() {
        MetaDataRule mr = allocateMetaDataRule("{\"IntRange\" : [1,10]}"); //$NON-NLS-1$

        IPlantRule rule = IPlantRuleFactory.build(mr);

        assertNotNull(rule);
        assertTrue(rule instanceof IntRangeRule);
    }

    public void testBuildIntAbove() {
        MetaDataRule mr = allocateMetaDataRule("{\"IntAbove\" : [-1]}"); //$NON-NLS-1$

        IPlantRule rule = IPlantRuleFactory.build(mr);

        assertNotNull(rule);
        assertTrue(rule instanceof IntAboveRule);
    }

    public void testBuildDoubleRange() {
        MetaDataRule mr = allocateMetaDataRule("{\"DoubleRange\" : [0.0,1.0]}"); //$NON-NLS-1$

        IPlantRule rule = IPlantRuleFactory.build(mr);

        assertNotNull(rule);
        assertTrue(rule instanceof DoubleRangeRule);
    }

    public void testBuildNonEmptyArray() {
        MetaDataRule mr = allocateMetaDataRule("{\"NonEmptyArray\" : []}"); //$NON-NLS-1$

        IPlantRule rule = IPlantRuleFactory.build(mr);

        assertNotNull(rule);
        assertTrue(rule instanceof NonEmptyArrayRule);
    }

    public void testBuildNonEmptyClass() {
        MetaDataRule mr = allocateMetaDataRule("{\"NonEmptyClass\": []}"); //$NON-NLS-1$

        IPlantRule rule = IPlantRuleFactory.build(mr);

        assertNotNull(rule);
        assertTrue(rule instanceof NonEmptyClassRule);
    }
}
