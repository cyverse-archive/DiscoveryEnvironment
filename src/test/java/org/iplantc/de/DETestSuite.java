package org.iplantc.de;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.iplantc.de.client.GwtTestComponentValueTable;
import org.iplantc.de.client.GwtTestDataUtils;
import org.iplantc.de.client.GwtTestDiskResource;
import org.iplantc.de.client.GwtTestDoubleRangeRule;
import org.iplantc.de.client.GwtTestEventFactory;
import org.iplantc.de.client.GwtTestEventJSONFactory;
import org.iplantc.de.client.GwtTestGenotypeNameRule;
import org.iplantc.de.client.GwtTestIPlantRuleFactory;
import org.iplantc.de.client.GwtTestIntAboveRule;
import org.iplantc.de.client.GwtTestIntRangeRule;
import org.iplantc.de.client.GwtTestJsEvaluate;
import org.iplantc.de.client.GwtTestMessageDispatcher;
import org.iplantc.de.client.GwtTestMetaDataContract;
import org.iplantc.de.client.GwtTestMultiPartServiceWrapper;
import org.iplantc.de.client.GwtTestNonEmptyArrayRule;
import org.iplantc.de.client.GwtTestNonEmptyClassRule;
import org.iplantc.de.client.GwtTestNotification;
import org.iplantc.de.client.GwtTestNotificationWindowConfig;
import org.iplantc.de.client.GwtTestUserInfo;
import org.iplantc.de.client.GwtTestValidatorHelper;
import org.iplantc.de.client.GwtTestWidgetFactory;
import org.iplantc.de.client.GwtTestWizardNotificationFactory;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * A class that builds a test suite for Discovery Environment unit tests
 * 
 * @author sriram
 * 
 */
public class DETestSuite extends GWTTestSuite {
    /**
     * Build test suite
     * 
     * @return
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for Discovery Environment"); //$NON-NLS-1$
        suite.addTestSuite(GwtTestDiskResource.class);
        suite.addTestSuite(GwtTestWizardNotificationFactory.class);
        suite.addTestSuite(GwtTestComponentValueTable.class);
        suite.addTestSuite(GwtTestDoubleRangeRule.class);
        suite.addTestSuite(GwtTestGenotypeNameRule.class);
        suite.addTestSuite(GwtTestIntAboveRule.class);
        suite.addTestSuite(GwtTestIntRangeRule.class);
        suite.addTestSuite(GwtTestIPlantRuleFactory.class);
        suite.addTestSuite(GwtTestMultiPartServiceWrapper.class);
        suite.addTestSuite(GwtTestNonEmptyArrayRule.class);
        suite.addTestSuite(GwtTestNonEmptyClassRule.class);
        suite.addTestSuite(GwtTestUserInfo.class);
        suite.addTestSuite(GwtTestValidatorHelper.class);
        suite.addTestSuite(GwtTestWidgetFactory.class);
        suite.addTestSuite(GwtTestMessageDispatcher.class);
        suite.addTestSuite(GwtTestNotification.class);
        suite.addTestSuite(GwtTestEventJSONFactory.class);
        suite.addTestSuite(GwtTestNotificationWindowConfig.class);
        suite.addTestSuite(GwtTestDataUtils.class);
        suite.addTestSuite(GwtTestEventFactory.class);
        suite.addTestSuite(GwtTestJsEvaluate.class);
        suite.addTestSuite(GwtTestMetaDataContract.class);

        return suite;
    }
}
