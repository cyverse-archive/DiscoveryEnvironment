package org.iplantc.de.server;

import junit.framework.TestCase;

public class TestMultiPartParser extends TestCase {
    public void testSimpleFile() {
        String contents = "-----My_Boundary\r\n" //$NON-NLS-1$
                + "Content-Disposition: form-data; name=\"foo\"; filename=\"foo.txt\"\r\n" //$NON-NLS-1$
                + "Content-Type: text/plain\r\n" + "\r\n" + "This is my file\n" + "\r\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "-----My_Boundary--\r\n"; //$NON-NLS-1$
        MultiPartParser mpp = new MultiPartParser("multipart/form-data; boundary=---My_Boundary", //$NON-NLS-1$
                contents);
        FilePart filePart = (FilePart)mpp.readNextPart();
        assertEquals("This is my file\n", filePart.getContents()); //$NON-NLS-1$
    }
}
