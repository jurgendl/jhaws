package org.jhaws.common.io.filter;

/**
 * filters only audio and video files
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
@SuppressWarnings("deprecation")
public class MediaFileFilter extends Operator.OrFileFilter {
	/**
	 * Creates a new MediaFileFilter object.
	 * 
	 * @param description
	 *            description
	 */
	public MediaFileFilter(String description) {
		super(description, new AbstractFileFilter[] { new VideoFileFilter(), new AudioFileFilter() });
	}

	/**
	 * Creates a new MediaFileFilter object.
	 */
	public MediaFileFilter() {
		this("media files"); //$NON-NLS-1$
	}
}
