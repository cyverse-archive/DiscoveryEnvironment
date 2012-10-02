/**
 * 
 */
package org.iplantc.de.client.viewer.model;

/**
 * @author sriram
 * 
 */
public enum MimeType {

    PNG("png"),

    GIF("gif"),

    JPEG("jpeg"),

    PDF("pdf"),

    PLAIN("plain"),

    HTML("html"),

    XHTML_XML("xhtml+xml"),

    PREVIEW("preview"),

    X_SH("x-sh");

    private String type;

    private MimeType(String type) {
        this.type = type;
    }

    public String getTypeString() {
        return toString().toLowerCase();
    }

    /**
     * Null-safe and case insensitive variant of valueOf(String)
     * 
     * @param typeString name of an mime type constant
     * @return
     */
    public static MimeType fromTypeString(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }

        return valueOf(typeString.split("/")[1].toUpperCase().replaceAll("[-.+]", "_"));
    }

    @Override
    public String toString() {
        return type;
    }

}
