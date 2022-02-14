package org.jhaws.common.io.filter;

/**
 * filters only images
 *
 * @author Jurgen
 * @version 2.0.0 - 27 June 2006
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
@Deprecated
public class ImageFileFilter extends FileExtensionFilter {
    /**
     * Creates a new ImageFileFilter object.
     *
     * @param description description
     */
    public ImageFileFilter(String description) {
        super(description, new String[] { "jpg", //$NON-NLS-1$
                "jpeg", //$NON-NLS-1$
                "bmp", //$NON-NLS-1$
                "tiff", //$NON-NLS-1$
                "tif", //$NON-NLS-1$
                "pix", //$NON-NLS-1$
                "png", //$NON-NLS-1$
                "gif", //$NON-NLS-1$
                "jp2", //$NON-NLS-1$
                "tga", //$NON-NLS-1$
                "pcx", //$NON-NLS-1$
                "pnm", //$NON-NLS-1$
                "ppm", //$NON-NLS-1$
                "pbm", //$NON-NLS-1$
                "pgm", //$NON-NLS-1$
                "ras", //$NON-NLS-1$
                "iff", //$NON-NLS-1$
                "raw", //$NON-NLS-1$
                "jpe", //$NON-NLS-1$
                "wmf", //$NON-NLS-1$
                "svg", //$NON-NLS-1$
                "jpm", //$NON-NLS-1$
                "emf", //$NON-NLS-1$
                "rla", //$NON-NLS-1$
                "jif", //$NON-NLS-1$
                "dpx", //$NON-NLS-1$
                "dcx", //$NON-NLS-1$
                "pic", //$NON-NLS-1$
                "ico" //$NON-NLS-1$
        });
    }

    /**
     * Creates a new ImageFileFilter object.
     */
    public ImageFileFilter() {
        this("image files"); //$NON-NLS-1$
    }
}
