package org.iplantc.de.client;

import java.util.List;

import org.iplantc.de.shared.services.BaseServiceCallWrapper;
import org.iplantc.de.shared.services.HTTPPart;
import org.iplantc.de.shared.services.MultiPartServiceWrapper;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestMultiPartServiceWrapper extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    private void validateBase(MultiPartServiceWrapper wrapper, BaseServiceCallWrapper.Type type,
            String address) {
        assertNotNull(wrapper);

        assertTrue(wrapper.getType() == type);

        String testAddress = wrapper.getAddress();
        assertNotNull(testAddress);
        assertTrue(testAddress.equals(address));
    }

    private void validatePartCount(MultiPartServiceWrapper wrapper, int numParts) {
        assertNotNull(wrapper);

        assertEquals(wrapper.getNumParts(), numParts);

        List<HTTPPart> parts = wrapper.getParts();
        assertNotNull(parts);
        assertEquals(parts.size(), parts.size());
    }

    public void testPortSubstitution() {
        String actual = null;
        String expected = "http://localhost:14444/"; //$NON-NLS-1$
        String localhost = "http://localhost:8080/"; //$NON-NLS-1$
        actual = localhost.replaceAll(":[0-9]+/", ":14444/"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expected, actual);

        expected = "http://foo.org:14444/"; //$NON-NLS-1$
        localhost = "http://foo.org:8888/"; //$NON-NLS-1$
        actual = localhost.replaceAll(":[0-9]+/", ":14444/"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expected, actual);
        actual = null;

        expected = "http://foo.org:14444/"; //$NON-NLS-1$
        localhost = "http://foo.org:8888/de-web/"; //$NON-NLS-1$
        actual = localhost.replaceAll(":[0-9]+/(.+)/", ":14444/"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expected, actual);
    }

    public void testValidMultipartServiceWrapperDefaultConstructor() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper();

        validateBase(wrapper, MultiPartServiceWrapper.Type.GET, ""); //$NON-NLS-1$
        validatePartCount(wrapper, 0);
    }

    public void testValidMultipartServiceWrapperAltConstructor() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper(MultiPartServiceWrapper.Type.POST,
                "some address"); //$NON-NLS-1$

        validateBase(wrapper, MultiPartServiceWrapper.Type.POST, "some address"); //$NON-NLS-1$
        validatePartCount(wrapper, 0);
    }

    public void testValidMultipartServiceWrapperAltConstructorPut() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper(MultiPartServiceWrapper.Type.PUT,
                "some address"); //$NON-NLS-1$

        validateBase(wrapper, MultiPartServiceWrapper.Type.PUT, "some address"); //$NON-NLS-1$
        validatePartCount(wrapper, 0);
    }

    public void testValidMultipartServiceWrapperAltConstructorDelete() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper(
                MultiPartServiceWrapper.Type.DELETE, "some address"); //$NON-NLS-1$

        validateBase(wrapper, MultiPartServiceWrapper.Type.DELETE, "some address"); //$NON-NLS-1$
        validatePartCount(wrapper, 0);
    }

    public void testValidMultipartServiceWrapperSinglePart() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper();

        wrapper.addPart("some part", "some disposition"); //$NON-NLS-1$ //$NON-NLS-2$

        validateBase(wrapper, MultiPartServiceWrapper.Type.GET, ""); //$NON-NLS-1$
        validatePartCount(wrapper, 1);
    }

    public void testValidMultipartServiceWrapperMultiPart() {
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper();

        wrapper.addPart("some part", "some disposition"); //$NON-NLS-1$ //$NON-NLS-2$
        wrapper.addPart("another part", "another disposition"); //$NON-NLS-1$ //$NON-NLS-2$
        wrapper.addPart("yet another part", "yet another disposition"); //$NON-NLS-1$ //$NON-NLS-2$

        validateBase(wrapper, MultiPartServiceWrapper.Type.GET, ""); //$NON-NLS-1$
        validatePartCount(wrapper, 3);
    }
}
