package org.iplantc.de.server;

/**
 * A FilePart represents one file that comes from an HTTP multipart body.
 * 
 * @author Donald A. Barre
 */
public class Part {

    private String name;
    private String contents;

    /**
     * Constructs an instance of the object.
     * 
     * @param name name associated with the data.
     * @param contents the actual data.
     */
    public Part(String name, String contents) {
        this.name = name;
        this.contents = contents;
    }

    /**
     * Gets the name.
     * 
     * @return a string representing the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the string to set as the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the data associated with the part.
     * 
     * @return a string representing the data contents.
     */
    public String getContents() {
        return contents;
    }

    /**
     * Sets the data associated with the part.
     * 
     * @param contents the data contents.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }
}