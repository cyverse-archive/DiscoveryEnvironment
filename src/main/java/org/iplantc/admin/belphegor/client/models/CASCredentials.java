package org.iplantc.admin.belphegor.client.models;

public class CASCredentials {

    /**
     * Defines an attribute for users email
     */
    public static String ATTR_EMAIL = "email";
    /**
     * Defines an attribute for users first name
     */
    public static String ATTR_USERFIRSTNAME = "firstName";

    /**
     * Defines an attribute for users last name
     */
    public static String ATTR_USERLASTNAME = "lastName";

    private static CASCredentials instance;

    private String email;
    private String firstName;
    private String lastName;

    /**
     * Constructs a default instance of the object with all fields being set to null.
     */
    private CASCredentials() {
    }

    public static CASCredentials getInstance() {
        if (instance == null) {
            instance = new CASCredentials();
        }
        return instance;
    }

    /**
     * Get user's email address.
     * 
     * @return email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user's email address.
     * 
     * @param email email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

}
