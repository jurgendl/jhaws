package org.jhaws.common.io.media.images;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class TransparencyFilter extends RGBImageFilter {
    private Color TRANSPARENT = ImageTools.TRANSPARENT;

    private Color color;

    private int[] blue_ranges;

    private int[] green_ranges;

    private int[] red_ranges;

    private int range;

    public TransparencyFilter() {
        this(Color.WHITE, 0);
    }

    public TransparencyFilter(final Color color) {
        this(color, 0);
    }

    public TransparencyFilter(final Color color, int range) {
        if (range > 256) {
            range = 256;
        }

        this.range = range;
        setColor(color);
    }

    public TransparencyFilter(final int range) {
        this(Color.WHITE, range);
    }

    private boolean between(final int r, final int[] brange) {
        if (brange[0] <= r && r <= brange[1]) {
            return true;
        }

        return false;
    }

    /**
     * @see java.awt.image.RGBImageFilter#filterRGB(int, int, int)
     */
    @Override
    public int filterRGB(final int x, final int y, final int pixel) {
        int[] rgb = ColorTools.convertPixel2Rgb(pixel);

        if (between(rgb[0], red_ranges) && between(rgb[1], green_ranges) && between(rgb[2], blue_ranges)) {
            return TRANSPARENT.getRGB();
        }

        return pixel;
    }

    public Color getColor() {
        return color;
    }

    private int[] ranged(final int r) {
        int lower = r - range;
        int upper = r + range;

        if (lower < 0) {
            lower = 0;
        }

        if (255 > upper) {
            upper = 255;
        }

        return new int[] { lower, upper };
    }

    private void setColor(final Color color) {
        this.color = color;

        int[] rgb = ColorTools.convertPixel2Rgb(color.getRGB());
        red_ranges = ranged(rgb[0]);
        green_ranges = ranged(rgb[1]);
        blue_ranges = ranged(rgb[2]);
    }
}
