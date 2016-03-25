package org.swingeasy.imaging;

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
        this.setColor(color);
    }

    public TransparencyFilter(final int range) {
        this(Color.WHITE, range);
    }

    private boolean between(final int r, final int[] brange) {
        if ((brange[0] <= r) && (r <= brange[1])) {
            return true;
        }

        return false;
    }

    /**
     *
     * @see java.awt.image.RGBImageFilter#filterRGB(int, int, int)
     */
    @Override
    public int filterRGB(final int x, final int y, final int pixel) {
        int[] rgb = ColorTools.convertPixel2Rgb(pixel);

        if (this.between(rgb[0], this.red_ranges) && this.between(rgb[1], this.green_ranges) && this.between(rgb[2], this.blue_ranges)) {
            return this.TRANSPARENT.getRGB();
        }

        return pixel;
    }

    public Color getColor() {
        return this.color;
    }

    private int[] ranged(final int r) {
        int lower = r - this.range;
        int upper = r + this.range;

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
        this.red_ranges = this.ranged(rgb[0]);
        this.green_ranges = this.ranged(rgb[1]);
        this.blue_ranges = this.ranged(rgb[2]);
    }
}
