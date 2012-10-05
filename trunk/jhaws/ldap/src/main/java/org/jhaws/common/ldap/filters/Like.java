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
        setKey(key);
        setValue(value);
    }

    /**
     * wordt gebruikt om filter op te bouwen
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + key + "=*" + value + "*)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * gets key
     *
     * @return Returns the key.
     */
    public String getKey() {
        return key;
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
     * gets value
     *
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * sets value
     *
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
