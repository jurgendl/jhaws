package org.jhaws.common.ldap.filters;

/**
 * filter: key=value (like)
 * 
 * @author Jurgen De Landsheer
 */
public class Like implements Filter {
    /** key */
    private String key;

    /** value */
    private String value;

    /**
     * Creates a new Like object.
     */
    public Like() {
        super();
    }

    /**
     * Creates a new Like object.
     */
    public Like(String key, String value) {
        super();
        this.setKey(key);
        this.setValue(value);
    }

    /**
     * gets key
     * 
     * @return Returns the key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * gets value
     * 
     * @return Returns the value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * sets key
     * 
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * sets value
     * 
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * wordt gebruikt om filter op te bouwen
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + this.key + "=*" + this.value + "*)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
