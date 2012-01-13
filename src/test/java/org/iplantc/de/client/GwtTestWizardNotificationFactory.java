package org.iplantc.de.client;

import org.iplantc.core.client.widgets.factory.WizardNotificationFactory;
import org.iplantc.core.client.widgets.metadata.ToggleStateWizardNotification;
import org.iplantc.core.client.widgets.metadata.WizardNotification;
import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestWizardNotificationFactory extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    public void testBuildNotificationNull() {
        String json = null;
        WizardNotificationFactory factory = new WizardNotificationFactory();
        WizardNotification wn = factory.buildNotification(json);
        assertNull(wn);
    }

    public void testBuildNotificationEmpty() {
        String json = ""; //$NON-NLS-1$
        WizardNotificationFactory factory = new WizardNotificationFactory();
        WizardNotification wn = factory.buildNotification(json);
        assertNull(wn);
    }

    public void testBuildNotification() {
        String json = "{\"sender\":\"skipBarcodeSplitter\", \"type\":\"disableOnSelection\", \"receivers\":[\"barcodeEntryOption\",\"numberOfAllowedMismatches\"]}"; //$NON-NLS-1$
        WizardNotificationFactory factory = new WizardNotificationFactory();
        WizardNotification wn = factory.buildNotification(json);
        assertNotNull(wn);
        assertEquals("skipBarcodeSplitter", JsonUtil.trim(wn.getNotificationSender())); //$NON-NLS-1$
        assertTrue(wn instanceof ToggleStateWizardNotification);
        assertTrue(wn.getNotificationReceviers().contains("barcodeEntryOption")); //$NON-NLS-1$
        assertTrue(wn.getNotificationReceviers().contains("numberOfAllowedMismatches")); //$NON-NLS-1$
    }

}
