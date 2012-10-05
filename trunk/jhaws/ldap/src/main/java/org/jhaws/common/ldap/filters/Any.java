package org.jhaws.common.ldap.filters;

/**
 * filter: key= (any)
 *
 * @author Jurgen De Landsheer
 */
public class Any implements Filter {
    /** key */
    private String key;

/**
     * Creates a new Any object.
     */
    public Any() {
        super();
    }

/**
     * Creates a new Any object.
     *
     * @param key key
     */
    public Any(String key) {
        super();
        setKey(key);
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
     * wordt gebruikt om filter op te bouwen
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + key + "=*)"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
