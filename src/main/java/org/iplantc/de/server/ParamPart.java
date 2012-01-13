package org.iplantc.de.server;

/**
 * A ParamPart represents one parameter (name/value pair) in an HTTP multipart body.
 * 
 * @author Donald A. Barre
 */
public class ParamPart extends Part {

    /**
     * Constructs an instance of the object.
     * 
     * @param name name of the paramter for a multi-part body.
     * @param contents the value of that data.
     */
    public ParamPart(String name, String contents) {
        super(name, contents.trim());
    }
}
