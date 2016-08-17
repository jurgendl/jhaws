package org.jhaws.common.io.filter;

/**
 * modifier
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 */
@Deprecated
public enum Modifier {
	/** < */
	SMALLER_THAN,
	/** > */
	LARGER_THAN,
	/** <= */
	SMALLER_OR_EQUAL,
	/** >= */
	LARGER_OR_EQUAL,
	/** == */
	EQUALS,
	/** != */
	DIFFERS;
	/**
	 * gets description
	 * 
	 * @return description
	 */
	public String getDescription() {
		switch (this) {
			case SMALLER_THAN:
				return "smaller than"; //$NON-NLS-1$

			case LARGER_THAN:
				return "less than"; //$NON-NLS-1$

			case SMALLER_OR_EQUAL:
				return "smaller than or equal"; //$NON-NLS-1$

			case LARGER_OR_EQUAL:
				return "less than or equal"; //$NON-NLS-1$

			case EQUALS:
				return "equal"; //$NON-NLS-1$

			// DIFFERS
			default:
				return "differs"; //$NON-NLS-1$
		}
	}
}
