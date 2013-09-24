package org.jhaws.common.ldap.interfaces;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * intern gebruik
 * 
 * @author Jurgen
 */
public class KeyValue {
    /** key */
    private String key;

    /** value */
    private String value;

    /**
     * Creates a new KeyValue object.
     * 
     * @param key
     * @param value
     */
    public KeyValue(final String key, final String value) {
        this.key = key;
        this.value = value;
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
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("value", this.value).append("key", this.key).toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
