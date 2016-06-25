package org.jhaws.common.io.filter;

/**
 * filters only video files
 * 
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class VideoFileFilter extends FileExtensionFilter {
	/**
	 * Creates a new VideoFileFilter object.
	 * 
	 * @param description
	 *            description
	 */
	public VideoFileFilter(String description) {
		super(description,
				new String[] { "mpg", //$NON-NLS-1$
						"mpeg", //$NON-NLS-1$
						"avi", //$NON-NLS-1$
						"wmv", //$NON-NLS-1$
						"wmf", //$NON-NLS-1$
						"mov", //$NON-NLS-1$
						"asf", //$NON-NLS-1$
						"qt", //$NON-NLS-1$
						"rm", //$NON-NLS-1$
						"divx", //$NON-NLS-1$
						"m2v", //$NON-NLS-1$
						"mpa", //$NON-NLS-1$
						"mp4", //$NON-NLS-1$
						"ogm", //$NON-NLS-1$
						"asx", //$NON-NLS-1$
						"xvid" //$NON-NLS-1$
				});
	}

	/**
	 * Creates a new VideoFileFilter object.
	 */
	public VideoFileFilter() {
		this("video files"); //$NON-NLS-1$
	}
}
