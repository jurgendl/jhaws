package org.jhaws.common.io.filter;

/**
 * filters only images
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 *
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class ImageFileFilter extends FileExtensionFilter {
	/**
	 * Creates a new ImageFileFilter object.
	 *
	 * @param description
	 *            description
	 */
	public ImageFileFilter(String description) {
		super(description,
				new String[] { "jpg", "jpeg", "bmp", "tiff", "tif", "pix", "png", "gif", "jp2", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
						"tga", "pcx", "pnm", "ppm", "pbm", "pgm", "ras", "iff", "raw", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
						"jpe", "wmf", "svg", "jpm", "emf", "rla", "jif", "dpx", "dcx", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
						"pic", "ico" //$NON-NLS-1$ //$NON-NLS-2$
				});
	}

	/**
	 * Creates a new ImageFileFilter object.
	 */
	public ImageFileFilter() {
		this("image files"); //$NON-NLS-1$
	}
}
