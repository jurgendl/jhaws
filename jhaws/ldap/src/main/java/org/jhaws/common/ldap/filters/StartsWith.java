package org.jhaws.common.ldap.filters;

/**
 * startswith filter
 *
 * @author Jurgen
 */
public class StartsWith implements Filter {
	/** key */
	private String key;

	/** value */
	private String value;

	/**
	 * Instantieer een nieuwe StartsWith
	 * 
	 * @param key
	 * @param value
	 */
	public StartsWith(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Getter voor key
	 * 
	 * @return Returns the key.
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Getter voor value
	 * 
	 * @return Returns the value.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter voor key
	 * 
	 * @param key
	 *            The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Setter voor value
	 * 
	 * @param value
	 *            The value to set.
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
		return "(" + this.key + "=" + this.value + "*)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
