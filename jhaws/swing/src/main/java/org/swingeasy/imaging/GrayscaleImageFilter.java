package org.swingeasy.imaging;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

public class GrayscaleImageFilter extends RGBImageFilter {
    protected static final Canvas canvas = new Canvas();

    public GrayscaleImageFilter() {
        this.canFilterIndexColorModel = true;
    }

    public final Image execute(Image source) {
        FilteredImageSource fis = new FilteredImageSource(ImageTools.toMemoryImageSource(source), new GrayscaleImageFilter());
        return GrayscaleImageFilter.canvas.createImage(fis);
    }

    /**
     * Convert color pixels to grayscale, The algorithm matches the NTSC specification
     */
    @Override
    public int filterRGB(final int x, final int y, final int pixel) {
        // Get the average RGB intensity
        int red = (pixel & 0x00ff0000) >> 16;
        int green = (pixel & 0x0000ff00) >> 8;
        int blue = pixel & 0x000000ff;

        int luma = (int) ((0.299 * red) + (0.587 * green) + (0.114 * blue));

        // Return the luma value as the value for each RGB component
        // Note: Alpha (transparency) is always set to max (not transparent)
        return (0xff << 24) | (luma << 16) | (luma << 8) | luma;
    }
}
