package org.iplantc.de.client;

import org.iplantc.core.client.widgets.factory.WizardWidgetFactory;
import org.iplantc.core.client.widgets.panels.BarcodeSelectorPanel;
import org.iplantc.core.client.widgets.panels.ClipperSelectorPanel;
import org.iplantc.core.client.widgets.panels.ComplexMatePanel;
import org.iplantc.core.client.widgets.panels.G2PPreProcessingFilePanel;
import org.iplantc.core.client.widgets.panels.MultiFileSelector;
import org.iplantc.core.client.widgets.panels.PercentEntryPanel;
import org.iplantc.core.client.widgets.panels.SimpleMatePanel;
import org.iplantc.core.client.widgets.panels.WizardCheckboxPanel;
import org.iplantc.core.client.widgets.panels.WizardFileSelectorPanel;
import org.iplantc.core.client.widgets.panels.WizardLabelPanel;
import org.iplantc.core.client.widgets.panels.WizardListBoxPanel;
import org.iplantc.core.client.widgets.panels.WizardTextField;
import org.iplantc.core.client.widgets.panels.XBasePairsTextField;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.de.client.utils.builders.DiskResourceSelectorBuilderImpl;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Widget;

public class GwtTestWidgetFactory extends GWTTestCase {
    private Property allocateProperty(final String json) {
        JSONObject objJson = JsonUtil.getObject(json);

        return new Property(objJson);
    }

    private Property allocateProperty(final String type, final String propertyType, final String value) {
        String json = "{\"name\":\"testproperty\", \"desc\":\"Some description\", \"id\":\"idTest\", \"propertytype\":\"" //$NON-NLS-1$
                + propertyType + "\", \"type\":\"" + type + "\", \"value\":\"" + value + "\"}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return allocateProperty(json);
    }

    private Widget doValidBuildTest(final String type, final String propertyType, final String value) {
        ComponentValueTable tblComponentVals = new ComponentValueTable(null);

        Property property = allocateProperty(type, propertyType, value);

        WizardWidgetFactory factory = new WizardWidgetFactory(new DiskResourceSelectorBuilderImpl());
        Widget ret = factory.build(property, tblComponentVals);

        assertNotNull(ret);

        return ret;
    }

    private void doInvalidBuildTest(final String type, final String value) {
        ComponentValueTable tblComponentVals = new ComponentValueTable(null);

        Property property = allocateProperty(type, "string", value); //$NON-NLS-1$

        WizardWidgetFactory factory = new WizardWidgetFactory(new DiskResourceSelectorBuilderImpl());
        Widget w = factory.build(property, tblComponentVals);

        assertNull(w);
    }

    public void testBuildNullProperty() {
        ComponentValueTable tblComponentVals = new ComponentValueTable(null);

        WizardWidgetFactory factory = new WizardWidgetFactory(new DiskResourceSelectorBuilderImpl());
        Widget w = factory.build(null, tblComponentVals);

        assertNull(w);
    }

    public void testBuildEmptyProperty() {
        doInvalidBuildTest("", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testBuildInvalidProperty() {
        doInvalidBuildTest("foo", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void testBuildFileInput() {
        Widget w = doValidBuildTest("FileInput", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardFileSelectorPanel);
    }

    public void testBuildInfo() {
        Widget w = doValidBuildTest("Info", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardLabelPanel);
    }

    public void testBuildFileText() {
        Widget w = doValidBuildTest("Text", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardTextField);
    }

    public void testBuildFileFlag() {
        Widget w = doValidBuildTest("Flag", "boolean", "true"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardCheckboxPanel);
    }

    public void testBuildFileSkipFlag() {
        Widget w = doValidBuildTest("SkipFlag", "boolean", "true"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardCheckboxPanel);
    }

    public void testBuildXBasePairs() {
        Widget w = doValidBuildTest("XBasePairs", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof XBasePairsTextField);
    }

    public void testBuildPreProcessingFiles() {
        Widget w = doValidBuildTest("PreProcessingFiles", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof G2PPreProcessingFilePanel);
    }

    public void testBuildSingleEndReadFiles() {
        Widget w = doValidBuildTest("MultiFileSelector", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof MultiFileSelector);
    }

    public void testBuildTophatMateFile() {
        Widget w = doValidBuildTest("TophatMateFile", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof SimpleMatePanel);
    }

    public void testBuildMateFile() {
        Widget w = doValidBuildTest("MateFile", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof ComplexMatePanel);
    }

    public void testBuildBarcodeSelector() {
        Widget w = doValidBuildTest("BarcodeSelector", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof BarcodeSelectorPanel);
    }

    public void testBuildClipperSelector() {
        Widget w = doValidBuildTest("ClipperSelector", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof ClipperSelectorPanel);
    }

    public void testBuildSelection() {
        Widget w = doValidBuildTest("Selection", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof WizardListBoxPanel);
    }

    public void testBuildPercentage() {
        Widget w = doValidBuildTest("Percentage", "string", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertTrue(w instanceof PercentEntryPanel);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
