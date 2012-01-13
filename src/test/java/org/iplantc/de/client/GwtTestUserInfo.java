package org.iplantc.de.client;

import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * A test class for UserInfo
 * 
 * @author sriram
 * 
 */
public class GwtTestUserInfo extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    public void testGetInstance() {
        UserInfo userinfo = UserInfo.getInstance();
        assertNotNull(userinfo);
    }

    public void testInitNull() {
        UserInfo userinfo = UserInfo.getInstance();
        userinfo.init(null);
        assertNull(userinfo.getWorkspaceId());
    }

    public void testInitEmpty() {
        UserInfo userinfo = UserInfo.getInstance();
        userinfo.init(""); //$NON-NLS-1$
        assertNull(userinfo.getWorkspaceId());
    }

    public void testInit() {
        UserInfo userinfo = UserInfo.getInstance();
        userinfo.init("{\"workspaceId\":\"1\"}"); //$NON-NLS-1$
        assertNotNull(userinfo.getWorkspaceId());
        assertEquals("1", userinfo.getWorkspaceId()); //$NON-NLS-1$
    }

    public void testInitJsonChange() {
        UserInfo userinfo = UserInfo.getInstance();
        userinfo.init("{\"workspaceId\":\"1\", \"userid\":\"testuser\"}"); //$NON-NLS-1$
        assertNotNull(userinfo.getWorkspaceId());
        assertEquals("1", userinfo.getWorkspaceId()); //$NON-NLS-1$
    }

}
