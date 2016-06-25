package org.jhaws.common.io.filter;

/**
 * filters only audio files
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class AudioFileFilter extends FileExtensionFilter {
	/**
	 * Creates a new AudioFileFilter object.
	 * 
	 * @param description
	 *            description
	 */
	public AudioFileFilter(String description) {
		super(description,
				new String[] { "mp3", //$NON-NLS-1$
						"mpu", //$NON-NLS-1$
						"wma", //$NON-NLS-1$
						"wav", //$NON-NLS-1$
						"ogg", //$NON-NLS-1$
						"ac3", //$NON-NLS-1$
						"mp1", //$NON-NLS-1$
						"mp2", //$NON-NLS-1$
						"ram" //$NON-NLS-1$
				});
	}

	/**
	 * Creates a new AudioFileFilter object.
	 */
	public AudioFileFilter() {
		this("audio files"); //$NON-NLS-1$
	}
}
