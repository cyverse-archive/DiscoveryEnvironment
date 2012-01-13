package org.iplantc.de.client.utils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * Defines formatting for handling JSON to String list changes.
 * 
 * @author amuir
 * 
 */
public class ProvenanceFormatter {
    // remove quotes from beginning and end of a string
    private static String removeQuotes(String in) {
        String ret = new String();

        if (in != null) {
            ret = in;

            if (ret.length() > 2) {
                if (ret.charAt(0) == '\"') {
                    ret = ret.substring(1);
                }

                if (ret.charAt(ret.length() - 1) == '\"') {
                    ret = ret.substring(0, ret.length() - 1);
                }
            }
        }

        return ret;
    }

    /**
     * Format provenance information passed as JSON.
     * 
     * @param json string to format.
     * @return properly formatted provenance.
     */
    public static String format(String json) {
        String ret = null; // assume failure

        if (json != null) {
            StringBuffer buf = new StringBuffer();

            JSONValue value = JSONParser.parseStrict(json);
            JSONArray array = value.isArray();

            int size = array.size();
            for (int i = 0; i < size; i++) {
                // fill our output buffer
                JSONValue item = array.get(i);

                String out = removeQuotes(item.isString().toString());
                buf.append(out + "\n"); //$NON-NLS-1$
            }

            ret = buf.toString();
        }

        return ret;
    }
}
