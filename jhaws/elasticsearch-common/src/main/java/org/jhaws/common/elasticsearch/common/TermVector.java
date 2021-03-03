package org.jhaws.common.elasticsearch.common;

public enum TermVector {
	/** No term vectors are stored. (default) */
	no,
	/** Just the terms in the field are stored. */
	yes,
	/** Terms and positions are stored. */
	with_positions,
	/** Terms and character offsets are stored. */
	with_offsets,
	/**
	 * Terms, positions, and character offsets are stored.<br>
	 * The fast vector highlighter requires with_positions_offsets. The term vectors API can retrieve whatever is stored.<br>
	 * Setting with_positions_offsets will double the size of a field’s index.
	 */
	with_positions_offsets,
	/** Terms, positions, and payloads are stored. */
	with_positions_payloads,
	/** Terms, positions, offsets and payloads are stored. */
	with_positions_offsets_payloads
	//
	;

	private String id;

	private TermVector() {
		this.id = name();
	}

	public String id() {
		return id;
	}
}
