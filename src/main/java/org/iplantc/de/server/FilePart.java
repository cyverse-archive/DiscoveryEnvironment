package org.iplantc.de.server;

/**
 * A FilePart represents one file that comes from an HTTP multipart body.
 * 
 * @author Donald A. Barre
 */
public class FilePart extends Part {

    private String filename;

    /**
     * Constructs an instance of the object given a name for the part, a filename, and the actual
     * contents.
     * 
     * @param name name of the file part.
     * @param filename name of the entire file.
     * @param contents data of the part.
     */
    public FilePart(String name, String filename, String contents) {
        super(name, contents);
        this.filename = filename;
    }

    /**
     * Gets the name of the file the part is associated with.
     * 
     * @return a string representing the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the name of the file the part is associated with.
     * 
     * @param filename a string representing the name of the file.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}