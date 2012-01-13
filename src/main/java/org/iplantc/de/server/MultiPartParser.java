package org.iplantc.de.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A simple HTTP Multipart Parser. It is intended only for extracting files.
 */
public class MultiPartParser {

    private static final Logger LOG = Logger.getLogger(MultiPartParser.class);

    /** MIME boundary that delimits parts */
    private String boundary;

    private String contents[];
    private int lineIndex = 0;

    /**
     * Constructs an instance of a multi-part file data parser.
     * 
     * @param type string indicating the type is of multi-part form-data.
     * @param contents the data of the multi-part http request.
     */
    public MultiPartParser(String type, String contents) {

        if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) { //$NON-NLS-1$
            throw new RuntimeException("Posted content type isn't multipart/form-data"); //$NON-NLS-1$
        }

        // Get the boundary string; it's included in the content type.
        // Should look something like "------------------------12012133613061"
        String tmpBoundary = extractBoundary(type);
        if (tmpBoundary == null) {
            throw new RuntimeException("Separation boundary was not specified"); //$NON-NLS-1$
        }

        // Save our values for later
        this.boundary = tmpBoundary;
        this.contents = contents.split("\n"); //$NON-NLS-1$

        // Read until we hit the boundary
        // Some clients send a preamble (per RFC 2046), so ignore that
        do {
            String line = readLine();
            if (line == null) {
                throw new RuntimeException("Corrupt form data: premature ending"); //$NON-NLS-1$
            }
            // See if this line is the boundary, and if so break
            if (line.startsWith(tmpBoundary)) {
                break; // success
            }
        } while (true);
    }

    /**
     * Read the next part arriving in the stream. Will be a <code>FilePart</code> or <code>null</code> to
     * indicate there are no more parts to read. The order of arrival corresponds to the order of the
     * form elements in the submitted form.
     * 
     * @return either a <code>FilePart</code>, a <code>ParamPart</code>, or <code>null</code> if there
     *         are no more parts to read.
     */
    public Part readNextPart() {

        // Read the headers; they look like this (not all may be present):
        // Content-Disposition: form-data; name="field1"; filename="file1.txt"
        // Content-Type: type/subtype
        // Content-Transfer-Encoding: binary
        List<String> headers = new ArrayList<String>();

        String line = readLine();
        if (line == null) {
            // No parts left, we're done
            return null;
        } else if (line.length() == 0) {
            // IE4 on Mac sends an empty line at the end; treat that as the end.
            return null;
        }

        // Read the following header lines we hit an empty line
        // A line starting with whitespace is considered a continuation;
        // that requires a little special logic.
        while (line != null && line.length() > 0) {
            String nextLine = null;
            boolean getNextLine = true;
            while (getNextLine) {
                nextLine = readLine();
                if (nextLine != null && (nextLine.startsWith(" ") || nextLine.startsWith("\t"))) { //$NON-NLS-1$ //$NON-NLS-2$
                    line = line + nextLine;
                } else {
                    getNextLine = false;
                }
            }
            // Add the line to the header list
            headers.add(line);
            line = nextLine;
        }

        // If we got a null above, it's the end
        if (line == null) {
            return null;
        }

        String name = null;
        String filename = null;
        // String origname = null;
        // String contentType = "text/plain"; // rfc1867 says this is the default

        for (String headerline : headers) {
            if (headerline.toLowerCase().startsWith("content-disposition:")) { //$NON-NLS-1$
                // Parse the content-disposition line
                String[] dispInfo = extractDispositionInfo(headerline);
                // String disposition = dispInfo[0]; // not currently used
                name = dispInfo[1];
                filename = dispInfo[2];
                // origname = dispInfo[3];
            } else if (headerline.toLowerCase().startsWith("content-type:")) { //$NON-NLS-1$
                // Get the content type, or null if none specified
                String type = extractContentType(headerline);
                if (type != null) {
                    // contentType = type;
                }
            }
        }

        /*
         * Read the contents of the multipart body
         */
        StringBuffer bodyContents = new StringBuffer();
        do {
            line = readLine();
            if (line == null) {
                throw new RuntimeException("Corrupt form data: premature ending"); //$NON-NLS-1$
            }

            // See if this line is the boundary, and if so break
            if (line.startsWith(boundary)) {
                break; // success
            }
            bodyContents.append(line + "\n"); //$NON-NLS-1$
        } while (true);

        // evan: You're going to have to trust me on this one. This is written
        // in between the lines of the standard
        // In order for the boundary to begin at the beginning of a line, there
        // is an additional CRLF added to the end of the file content
        // Nothing in the standard explicitly states it, but it makes sense,
        // because if a file doesn't end with a newline, one would need to be
        // inserted. By extension, it would make sense then to insert it for
        // all files (which it does).
        // But we need to strip it to get the correct contents of the file
        if (bodyContents.charAt(bodyContents.length() - 1) == '\n') {
            bodyContents.deleteCharAt(bodyContents.length() - 1);
        }

        // Now, finally, we read the content (end after reading the boundary)
        if (filename == null) {
            // This is a parameter, add it to the vector of values
            // The encoding is needed to help parse the value
            return new ParamPart(name, bodyContents.toString());
        } else {
            // This is a file
            if (filename.equals("")) { //$NON-NLS-1$
                filename = null; // empty filename, probably an "empty" file param
            }
            return new FilePart(name, filename, bodyContents.toString());
        }
    }

    /**
     * Get all of the parts in the HTTP request.
     * 
     * @return a collection of parts from the multi-part data.
     */
    public Parts getAllParts() {
        Parts parts = new Parts();
        Part part = readNextPart();
        while (part != null) {
            parts.add(part);
            part = readNextPart();
        }
        return parts;
    }

    /**
     * Extracts and returns the boundary token from a line.
     * 
     * @param the line containing the boundary
     * @return the boundary token.
     */
    private String extractBoundary(String line) {
        // Use lastIndexOf() because IE 4.01 on Win98 has been known to send the
        // "boundary=" string multiple times.
        int index = line.lastIndexOf("boundary="); //$NON-NLS-1$
        if (index == -1) {
            return null;
        }
        String tokenBoundary = line.substring(index + 9); // 9 for "boundary="
        if (tokenBoundary.charAt(0) == '"') {
            // The boundary is enclosed in quotes, strip them
            index = tokenBoundary.lastIndexOf('"');
            tokenBoundary = tokenBoundary.substring(1, index);
        }

        // The real boundary is always preceeded by an extra "--"
        tokenBoundary = "--" + tokenBoundary; //$NON-NLS-1$

        return tokenBoundary;
    }

    /**
     * Extracts and returns disposition info from a line, as a <code>String<code>
     * array with elements: disposition, name, filename.
     * 
     * @return String[] of elements: disposition, name, filename.
     */
    private String[] extractDispositionInfo(String line) {
        // Return the line's data as an array: disposition, name, filename
        String[] retval = new String[4];

        // Convert the line to a lowercase string without the ending \r\n
        // Keep the original line for error messages and for variable names.
        String origline = line;
        line = origline.toLowerCase();

        // Get the content disposition, should be "form-data"
        int start = line.indexOf("content-disposition: "); //$NON-NLS-1$
        int end = line.indexOf(";"); //$NON-NLS-1$
        if (start == -1 || end == -1) {
            throw new RuntimeException("Content disposition corrupt: " + origline); //$NON-NLS-1$
        }
        String disposition = line.substring(start + 21, end);
        if (!disposition.equals("form-data")) { //$NON-NLS-1$
            throw new RuntimeException("Invalid content disposition: " + disposition); //$NON-NLS-1$
        }

        // Get the field name
        start = line.indexOf("name=\"", end); // start at last semicolon //$NON-NLS-1$
        end = line.indexOf("\"", start + 7); // skip name=\" //$NON-NLS-1$
        int startOffset = 6;
        if (start == -1 || end == -1) {
            // Some browsers like lynx don't surround with ""
            start = line.indexOf("name=", end); //$NON-NLS-1$
            end = line.indexOf(";", start + 6); //$NON-NLS-1$
            if (start == -1) {
                throw new RuntimeException("Content disposition corrupt: " + origline); //$NON-NLS-1$
            } else if (end == -1) {
                end = line.length();
            }
            startOffset = 5; // without quotes we have one fewer char to skip
        }
        String name = origline.substring(start + startOffset, end);

        // Get the filename, if given
        String filename = null;
        String origname = null;
        start = line.indexOf("filename=\"", end + 2); // start after name //$NON-NLS-1$
        end = line.indexOf("\"", start + 10); // skip filename=\" //$NON-NLS-1$
        if (start != -1 && end != -1) { // note the !=
            filename = origline.substring(start + 10, end);
            origname = filename;
            // The filename may contain a full path. Cut to just the filename.
            int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (slash > -1) {
                filename = filename.substring(slash + 1); // past last slash
            }
        }

        // Return a String array: disposition, name, filename
        // empty filename denotes no file posted!
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        retval[3] = origname;
        return retval;
    }

    /**
     * Extracts and returns the content type from a line, or null if the line was empty.
     * 
     * @return content type, or null if line was empty.
     */
    private static String extractContentType(String line) {
        // Convert the line to a lowercase string
        line = line.toLowerCase();

        // Get the content type, if any
        // Note that Opera at least puts extra info after the type, so handle
        // that. For example: Content-Type: text/plain; name="foo"
        int end = line.indexOf(";"); //$NON-NLS-1$
        if (end == -1) {
            end = line.length();
        }

        return line.substring(13, end).trim(); // "content-type:" is 13
    }

    /**
     * Get the next line from the HTTP body.
     * 
     * @return
     */
    private String readLine() {
        if (lineIndex >= contents.length) {
            return null;
        }

        String line = contents[lineIndex++];

        // Cut off the trailing \r if it is there. Sometimes IE5 doesn't insert it.

        int len = line.length();
        if (len >= 1 && line.charAt(len - 1) == '\r') {
            line = line.substring(0, len - 1);
        }

        return line;
    }
}
