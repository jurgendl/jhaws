package org.jhaws.common.ldap.filters;

/**
 * filter: key=value (equality) met mogelijkheid tot invers (not)
 * 
 * @author Jurgen De Landsheer
 */
public class Equal implements Filter {
    /** key */
    private String key;

    /** value */
    private String value;

    /** negate (inverteer) */
    private boolean negate;

    /**
     * Creates a new Equal object.
     */
    public Equal() {
        super();
    }

    /**
     * Creates a new Equal object.
     * 
     * @param key key
     * @param value value
     */
    public Equal(String key, String value) {
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
     * gets negate
     * 
     * @return Returns the negate.
     */
    public boolean isNegate() {
        return this.negate;
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
     * sets negate
     * 
     * @param negate The negate to set.
     */
    public void setNegate(boolean negate) {
        this.negate = negate;
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
        return this.negate ? ("(!" + this.key + "=" + this.value + ")") : ("(" + this.key + "=" + this.value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    }
}
