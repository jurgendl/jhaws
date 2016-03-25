package org.swingeasy.imaging;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * @see Hugo Teixeira High-Quality Image Resize with Java http://www.componenthouse.com/article-20
 */
public class ImageTools {
    // /**
    // * bloom effect
    // *
    // * @param image image
    // * @param tresshold tresshold (0.7) (lower is brigter)
    // * @param iterations iterations (4) (more is brighter)
    // * @param gauss gauss strength (3) (higher for softer edges)
    // *
    // * @return bloomed image
    // */
    // public static BufferedImage bloom(Image image, float tresshold, int iterations, int gauss) {
    // BufferedImage result = ImageTools.toBufferedImage(image);
    // int w = result.getWidth();
    // int h = result.getHeight();
    // float smoothness = 1.0f;
    //
    // if (smoothness > 1.0f) {
    // result = ImageTools.getScaledInstance(result, (int) (w / smoothness));
    // }
    //
    // BrightPassFilter brightPassFilter = new BrightPassFilter(tresshold);
    // GaussianBlurFilter gaussianBlurFilter = new GaussianBlurFilter(gauss);
    //
    // BufferedImage brightPass = brightPassFilter.filter(result, null);
    //
    // BufferedImage bloom = ImageTools.newTransparentBufferedImage(w, h);
    // Graphics2D g2 = bloom.createGraphics();
    // g2.drawImage(image, 0, 0, null);
    // g2.setComposite(BlendComposite.Add);
    // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //
    // for (int i = 0; i < iterations; i++) {
    // g2.drawImage(gaussianBlurFilter.filter(brightPass, null), 0, 0, result.getWidth(), h, null);
    // brightPass = ImageTools.createThumbnailFast(brightPass, brightPass.getWidth() / 2);
    // }
    //
    // g2.dispose();
    //
    // return bloom;
    // }

    /**
     * blurs an image
     *
     * @param image : Image : image
     * @param strength : int : strength (0=original, 10=strong blurring, ...)
     *
     * @return : BufferedImage : blurred image
     */
    public static BufferedImage blur(final Image image, final int strength) {
        float[] kernel = new float[strength * strength];
        float v = 1.0f / kernel.length;

        for (int i = 0; i < strength; i++) {
            for (int j = 0; j < strength; j++) {
                kernel[(i * strength) + j] = v;
            }
        }

        Kernel kernelC = new Kernel(strength, strength, kernel);
        ConvolveOp cop = new ConvolveOp(kernelC, ConvolveOp.EDGE_NO_OP, null);

        return cop.filter(ImageTools.toBufferedImage(image), null);
    }

    // /**
    // * box blur an image
    // *
    // * @param image image
    // * @param strength strength 0-10
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurBox(final Image image, final int strength) {
    // return new BoxBlur(strength).filter(ImageTools.toBufferedImage(image));
    // }

    // /**
    // * fast blur an image
    // *
    // * @param image image
    // * @param radius radius from 1 to 100
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurFast(final Image image, final int radius) {
    // return ImageTools.blurFast(image, radius, 1);
    // }

    // /**
    // * fast blur an image
    // *
    // * @param image image
    // * @param radius radius from 1 to 100
    // * @param iterations keep it low
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurFast(final Image image, final int radius, final int iterations) {
    // return new StackBlurFilter(radius, iterations).filter(ImageTools.toBufferedImage(image), null);
    // }

    // /**
    // * nice fast gaussian blur
    // *
    // * @param image image
    // * @param radius 1-50
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurGaussian(Image image, int radius) {
    // return ImageTools.blurGaussian(image, radius, true);
    // }

    // /**
    // * nice gaussian blur
    // *
    // * @param image image
    // * @param radius 1-50
    // * @param faster faster blurring (by downsizing)
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurGaussian(Image image, int radius, boolean faster) {
    // BufferedImage bi = ImageTools.toBufferedImage(image);
    //
    // if (faster) {
    // image = ImageTools.getScaledInstance(bi, 0.5);
    // }
    //
    // bi = GaussianBlurConvolveOp.getGaussianBlurConvolveOp(radius, true).filter(bi, null);
    // bi = GaussianBlurConvolveOp.getGaussianBlurConvolveOp(radius, false).filter(bi, null);
    //
    // if (faster) {
    // image = ImageTools.getScaledInstance(bi, 2.0);
    // }
    //
    // return bi;
    // }

    // /**
    // * radial blur an image
    // *
    // * @param image image
    // * @param radius radius 0-10
    // *
    // * @return blurred image
    // */
    // public static BufferedImage blurRadial(final Image image, final int radius) {
    // return new RadialBlur(radius).filter(ImageTools.toBufferedImage(image));
    // }

    /**
     * blur
     */
    public static BufferedImage blurImage(BufferedImage image) {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };

        Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
        map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RenderingHints hints = new RenderingHints(map);
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);

        return op.filter(image, null);
    }

    /**
     * brightens an image
     *
     * @param image : Image : source
     * @param percentage : float : percentage to brighten (0-100-...)
     *
     * @return : BufferedImage : brightened image
     */
    public static BufferedImage brighten(final Image image, final int percentage) {
        float s = 1.0f + (percentage / 100.0f);

        return ImageTools.rescale(image, s);
    }

    /**
     * brightens an image (alternative)
     *
     * @param image image
     * @param factor factor 0.0 - 1.0 (0.65 is pretty good)
     *
     * @return brightened image
     */
    public static BufferedImage brightenAlt(final Image image, final float factor) {
        BufferedImage original = ImageTools.toBufferedImage(image);
        int w = original.getWidth();
        int h = original.getHeight();
        BufferedImage brightened = ImageTools.newTransparentBufferedImage(w, h);
        final int FI = (int) (1.0 / (1.0 - factor));

        for (int j = 0; j < h; j++) {
            for (int k = 0; k < w; k++) {
                int pixel = original.getRGB(k, j);
                int alpha = (pixel & 0xff000000) >> 24;
            int red = (pixel & 0x00ff0000) >> 16;
            int green = (pixel & 0x0000ff00) >> 8;
            int blue = pixel & 0x000000ff;
            red = Math.min((int) (Math.max(red, FI) / factor), 255);
            green = Math.min((int) (Math.max(green, FI) / factor), 255);
            blue = Math.min((int) (Math.max(blue, FI) / factor), 255);
            brightened.setRGB(k, j, (alpha << 24) | (red << 16) | (green << 8) | blue);
            }
        }

        return brightened;
    }

    /**
     * copies an image
     *
     * @param source : BufferedImage : image to copy
     *
     * @return : BufferedImage : copied image
     */
    public static BufferedImage copy(final BufferedImage source) {
        return ImageTools.copy(source, ImageTools.newBufferedImage(source.getWidth(), source.getHeight()));
    }

    /**
     * copies an image
     *
     * @param source : BufferedImage : image to copy
     * @param target : BufferedImage : copied image
     *
     * @return : BufferedImage : copied image
     */
    public static BufferedImage copy(final BufferedImage source, final BufferedImage target) {
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        double scalex = (double) target.getWidth() / source.getWidth();
        double scaley = (double) target.getHeight() / source.getHeight();
        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(source, xform);
        g2.dispose();

        return target;
    }

    /**
     * copies an image using an interpolation hint
     *
     * @param source : BufferedImage : image to copy
     * @param target : BufferedImage : copied image
     * @param interpolationHint : Object : interpolation hint
     *
     * @return : BufferedImage : copied image
     *
     * @see java.awt.RenderingHints
     */
    public static BufferedImage copy(final BufferedImage source, final BufferedImage target, final Object interpolationHint) {
        Graphics2D g2 = target.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationHint);

        double scalex = (double) target.getWidth() / source.getWidth();
        double scaley = (double) target.getHeight() / source.getHeight();
        AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);
        g2.drawRenderedImage(source, xform);
        g2.dispose();

        return target;
    }

    /**
     * copies an image using an interpolation hint
     *
     * @param source : BufferedImage : image to copy
     * @param interpolationHint : Object : interpolation hint
     *
     * @return : BufferedImage : copied image
     *
     * @see java.awt.RenderingHints
     */
    public static BufferedImage copy(final BufferedImage source, final Object interpolationHint) {
        return ImageTools.copy(source, ImageTools.newBufferedImage(source.getWidth(), source.getHeight()), interpolationHint);
    }

    /**
     * <p>
     * Returns a new <code>BufferedImage</code> using the same color model as the image passed as a parameter. The returned image is only compatible
     * with the image passed as a parameter. This does not mean the returned image is compatible with the hardware.
     * </p>
     *
     * @param image the reference image from which the color model of the new image is obtained
     * @return a new <code>BufferedImage</code>, compatible with the color model of <code>image</code>
     */
    public static BufferedImage createColorModelCompatibleImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        return new BufferedImage(cm, cm.createCompatibleWritableRaster(image.getWidth(), image.getHeight()), cm.isAlphaPremultiplied(), null);
    }

    /**
     * <p>
     * Returns a new compatible image with the same width, height and transparency as the image specified as a parameter.
     * </p>
     *
     * @see java.awt.Transparency
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param image the reference image from which the dimension and the transparency of the new image are obtained
     * @return a new compatible <code>BufferedImage</code> with the same dimension and transparency as <code>image</code>
     */
    public static BufferedImage createCompatibleImage(BufferedImage image) {
        return ImageTools.createCompatibleImage(image, image.getWidth(), image.getHeight());
    }

    /**
     * <p>
     * Returns a new compatible image of the specified width and height, and the same transparency setting as the image specified as a parameter.
     * </p>
     *
     * @see java.awt.Transparency
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @param image the reference image from which the transparency of the new image is obtained
     * @return a new compatible <code>BufferedImage</code> with the same transparency as <code>image</code> and the specified dimension
     */
    public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
        return ImageTools.getGraphicsConfiguration().createCompatibleImage(width, height, image.getTransparency());
    }

    /**
     * <p>
     * Returns a new opaque compatible image of the specified width and height.
     * </p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @return a new opaque compatible <code>BufferedImage</code> of the specified width and height
     */
    public static BufferedImage createCompatibleImage(int width, int height) {
        return ImageTools.getGraphicsConfiguration().createCompatibleImage(width, height);
    }

    // /**
    // * NA
    // *
    // * @param image NA
    // * @param size NA
    // * @param shadowColor NA
    // *
    // * @return NA
    // */
    // private static BufferedImage createDropShadow(BufferedImage image, int size, Color shadowColor) {
    // BufferedImage shadow = new BufferedImage(image.getWidth() + (4 * size), image.getHeight() + (4 * size), BufferedImage.TYPE_INT_ARGB);
    //
    // Graphics2D g2 = shadow.createGraphics();
    // g2.drawImage(image, size * 2, size * 2, null);
    //
    // g2.setComposite(AlphaComposite.SrcIn);
    // g2.setColor(shadowColor);
    // g2.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());
    //
    // g2.dispose();
    //
    // shadow = GaussianBlurConvolveOp.getGaussianBlurConvolveOp(size, true).filter(shadow, null);
    // shadow = GaussianBlurConvolveOp.getGaussianBlurConvolveOp(size, false).filter(shadow, null);
    //
    // return shadow;
    // }

    // /**
    // * creates a shadow around in image in the transparent zone
    // *
    // * @param imageB image
    // * @param fastRendering fast?
    // * @param shadowSize size (5)
    // * @param shadowOpacity 0.0 - 1.0
    // *
    // * @return image with shadow
    // */
    // public static BufferedImage createDropShadow(Image imageB, boolean fastRendering, int shadowSize, float shadowOpacity) {
    // return ImageTools.createDropShadow(imageB, fastRendering, shadowSize, shadowOpacity, Color.BLACK);
    // }

    // /**
    // * creates a shadow around in image in the transparent zone
    // *
    // * @param imageB image
    // * @param fastRendering fast?
    // * @param shadowSize size (5)
    // * @param shadowOpacity 0.0 - 1.0
    // * @param shadowColor shadow color
    // *
    // * @return image with shadow
    // */
    // public static BufferedImage createDropShadow(Image imageB, boolean fastRendering, int shadowSize, float shadowOpacity, Color shadowColor) {
    // BufferedImage imageA = ImageTools.toBufferedImage(imageB);
    // int w = imageA.getWidth();
    // int h = imageA.getHeight();
    // BufferedImage bi = ImageTools.newTransparentBufferedImage(w, h);
    // Image image0 = null;
    //
    // if (!fastRendering) {
    // image0 = ImageTools.createDropShadow(imageA, shadowSize, shadowColor);
    // } else {
    // ShadowRenderer renderer = new ShadowRenderer(shadowSize / 2, 1.0f, shadowColor);
    // image0 = renderer.createShadow(imageA);
    // }
    //
    // int x = (w - imageA.getWidth()) / 2;
    // int y = (h - imageA.getHeight()) / 2;
    //
    // Graphics2D g2 = bi.createGraphics();
    // Composite c = g2.getComposite();
    // g2.setComposite(AlphaComposite.SrcOver.derive(shadowOpacity));
    //
    // if (!fastRendering) {
    // g2.drawImage(image0, (x - (shadowSize * 2)) + 5, (y - (shadowSize * 2)) + 5, null);
    // } else {
    // g2.drawImage(image0, (x - (shadowSize / 2)) + 5, (y - (shadowSize / 2)) + 5, null);
    // }
    //
    // g2.setComposite(c);
    //
    // g2.drawImage(imageA, x, y, null);
    //
    // return bi;
    // }

    // /**
    // * creates a shadow around in image in the transparent zone
    // *
    // * @param image image
    // * @param shadowSize size (5)
    // * @param shadowOpacity 0.0 - 1.0
    // *
    // * @return image with shadow
    // */
    // public static BufferedImage createDropShadow(Image image, int shadowSize, float shadowOpacity) {
    // return ImageTools.createDropShadow(image, true, shadowSize, shadowOpacity);
    // }

    /**
     * <p>
     * Returns a new translucent compatible image of the specified width and height.
     * </p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @return a new translucent compatible <code>BufferedImage</code> of the specified width and height
     */
    public static BufferedImage createCompatibleTranslucentImage(int width, int height) {
        return ImageTools.getGraphicsConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    /**
     * creates a new image with given width and height and given pixel colors (single array)
     *
     * @param w : int : width
     * @param h : int : height
     * @param pix : int[] : pixel colors (single array)
     *
     * @return : BufferedImage : new image
     */
    public static BufferedImage createImage(final int w, final int h, final int[] pix) {
        return ImageTools.toBufferedImage(ImageTools.jComponent.createImage(new MemoryImageSource(w, h, pix, 0, w)));
    }

    /**
     * <p>
     * Returns a thumbnail of a source image. <code>newSize</code> defines the length of the longest dimension of the thumbnail. The other dimension
     * is then computed according to the dimensions ratio of the original picture.
     * </p>
     * <p>
     * This method offers a good trade-off between speed and quality. The result looks better than
     * {@link #createThumbnailFast(java.awt.image.BufferedImage, int)} when the new size is less than half the longest dimension of the source image,
     * yet the rendering speed is almost similar.
     * </p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newSize the length of the largest dimension of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newSize</code> is larger than the largest dimension of <code>image</code> or &lt;= 0
     */
    public static BufferedImage createThumbnail(BufferedImage image, int newSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        boolean isWidthGreater = width > height;

        if (isWidthGreater) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than" + " the image width");
            }
        } else if (newSize >= height) {
            throw new IllegalArgumentException("newSize must be lower than" + " the image height");
        }

        if (newSize <= 0) {
            throw new IllegalArgumentException("newSize must" + " be greater than 0");
        }

        float ratioWH = (float) width / (float) height;
        float ratioHW = (float) height / (float) width;

        BufferedImage thumb = image;

        do {
            if (isWidthGreater) {
                width /= 2;
                if (width < newSize) {
                    width = newSize;
                }
                height = (int) (width / ratioWH);
            } else {
                height /= 2;
                if (height < newSize) {
                    height = newSize;
                }
                width = (int) (height / ratioHW);
            }

            BufferedImage temp = ImageTools.createCompatibleImage(image, width, height);
            Graphics2D g2 = temp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
            g2.dispose();

            thumb = temp;
        } while (newSize != (isWidthGreater ? width : height));

        return thumb;
    }

    /**
     * <p>
     * Returns a thumbnail of a source image.
     * </p>
     * <p>
     * This method offers a good trade-off between speed and quality. The result looks better than
     * {@link #createThumbnailFast(java.awt.image.BufferedImage, int)} when the new size is less than half the longest dimension of the source image,
     * yet the rendering speed is almost similar.
     * </p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @param image the source image
     * @param newWidth the width of the thumbnail
     * @param newHeight the height of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newWidth</code> is larger than the width of <code>image</code> or if code>newHeight</code> is larger
     *             than the height of <code>image or if one the dimensions is
     *             not &gt; 0</code>
     */
    public static BufferedImage createThumbnail(BufferedImage image, int newWidth, int newHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        if ((newWidth >= width) || (newHeight >= height)) {
            throw new IllegalArgumentException("newWidth and newHeight cannot" + " be greater than the image" + " dimensions");
        } else if ((newWidth <= 0) || (newHeight <= 0)) {
            throw new IllegalArgumentException("newWidth and newHeight must" + " be greater than 0");
        }

        BufferedImage thumb = image;

        do {
            if (width > newWidth) {
                width /= 2;
                if (width < newWidth) {
                    width = newWidth;
                }
            }

            if (height > newHeight) {
                height /= 2;
                if (height < newHeight) {
                    height = newHeight;
                }
            }

            BufferedImage temp = ImageTools.createCompatibleImage(image, width, height);
            Graphics2D g2 = temp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(thumb, 0, 0, temp.getWidth(), temp.getHeight(), null);
            g2.dispose();

            thumb = temp;
        } while ((width != newWidth) || (height != newHeight));

        return thumb;
    }

    /**
     * <p>
     * Returns a thumbnail of a source image. <code>newSize</code> defines the length of the longest dimension of the thumbnail. The other dimension
     * is then computed according to the dimensions ratio of the original picture.
     * </p>
     * <p>
     * This method favors speed over quality. When the new size is less than half the longest dimension of the source image,
     * {@link #createThumbnail(BufferedImage, int)} or {@link #createThumbnail(BufferedImage, int, int)} should be used instead to ensure the quality
     * of the result without sacrificing too much performance.
     * </p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newSize the length of the largest dimension of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newSize</code> is larger than the largest dimension of <code>image</code> or &lt;= 0
     */
    public static BufferedImage createThumbnailFast(BufferedImage image, int newSize) {
        float ratio;
        int width = image.getWidth();
        int height = image.getHeight();

        if (width > height) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than" + " the image width");
            } else if (newSize <= 0) {
                throw new IllegalArgumentException("newSize must" + " be greater than 0");
            }

            ratio = (float) width / (float) height;
            width = newSize;
            height = (int) (newSize / ratio);
        } else {
            if (newSize >= height) {
                throw new IllegalArgumentException("newSize must be lower than" + " the image height");
            } else if (newSize <= 0) {
                throw new IllegalArgumentException("newSize must" + " be greater than 0");
            }

            ratio = (float) height / (float) width;
            height = newSize;
            width = (int) (newSize / ratio);
        }

        BufferedImage temp = ImageTools.createCompatibleImage(image, width, height);
        Graphics2D g2 = temp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
        g2.dispose();

        return temp;
    }

    /**
     * <p>
     * Returns a thumbnail of a source image.
     * </p>
     * <p>
     * This method favors speed over quality. When the new size is less than half the longest dimension of the source image,
     * {@link #createThumbnail(BufferedImage, int)} or {@link #createThumbnail(BufferedImage, int, int)} should be used instead to ensure the quality
     * of the result without sacrificing too much performance.
     * </p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newWidth the width of the thumbnail
     * @param newHeight the height of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newWidth</code> is larger than the width of <code>image</code> or if code>newHeight</code> is larger
     *             than the height of <code>image</code> or if one of the dimensions is &lt;= 0
     */
    public static BufferedImage createThumbnailFast(BufferedImage image, int newWidth, int newHeight) {
        if ((newWidth >= image.getWidth()) || (newHeight >= image.getHeight())) {
            throw new IllegalArgumentException("newWidth and newHeight cannot" + " be greater than the image" + " dimensions");
        } else if ((newWidth <= 0) || (newHeight <= 0)) {
            throw new IllegalArgumentException("newWidth and newHeight must" + " be greater than 0");
        }

        BufferedImage temp = ImageTools.createCompatibleImage(image, newWidth, newHeight);
        Graphics2D g2 = temp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
        g2.dispose();

        return temp;
    }

    private static VolatileImage createVolatileImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = ImageTools.getDefaultGraphicsConfiguration();
        VolatileImage image = gc.createCompatibleVolatileImage(width, height, transparency);
        int valid = image.validate(gc);

        if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
            image = ImageTools.createVolatileImage(width, height, transparency);
        }

        return image;
    }

    /**
     * crop an image
     *
     * @param image : Image : source image
     * @param x : int : x coordinate from upper left point
     * @param y : int : y coordinate from upper left point
     * @param w : int : width
     * @param h : int : height
     *
     * @return : BufferedImage : cropped image
     */
    public static BufferedImage crop(final Image image, final int x, final int y, final int w, final int h) {
        return ImageTools.toBufferedImage(image).getSubimage(x, y, w, h);
    }

    /**
     * darkens an image
     *
     * @param image : Image : source
     * @param percentage : float : percentage to darken (0-100)
     *
     * @return : BufferedImage : darkened image
     */
    public static BufferedImage darken(final Image image, final int percentage) {
        float s = 1.0f - ((float) percentage / 100);

        return ImageTools.rescale(image, s);
    }

    // /**
    // * emboss an image
    // *
    // * @param image : Image : source
    // *
    // * @return : BufferedImage : embossed image
    // *
    // * @throws RuntimeException : on exception
    // */
    // public static BufferedImage emboss(final Image image) throws RuntimeException {
    // FilteredImageSource fis = new FilteredImageSource(ImageTools.toMemoryImageSource(image), new EmbossFilter());
    //
    // return ImageTools.toBufferedImage(new Canvas().createImage(fis));
    // }

    /**
     * detect edges
     *
     * @param image : Image : source image
     *
     * @return : BufferedImage : result
     */
    public static BufferedImage edges(final Image image) {
        float[] elements = { 0.0f, -1.0f, 0.0f, -1.0f, 4.f, -1.0f, 0.0f, -1.0f, 0.0f };

        Kernel kernel = new Kernel(3, 3, elements);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        return cop.filter(ImageTools.toBufferedImage(image), null);
    }

    /**
     * emboss an image without losing color
     *
     * @param image : Image : source
     * @param strength : int : strength (0=do nothing, 1, ...)
     *
     * @return : BufferedImage : embossed image
     *
     * @throws RuntimeException exception
     */
    public static BufferedImage emboss(final Image image, final int strength) throws RuntimeException {
        Kernel kernel = new Kernel(3, 3, new float[] { -strength, 0, 0, 0, 1, 0, 0, 0, strength });
        BufferedImageOp op = new ConvolveOp(kernel);

        return op.filter(ImageTools.toBufferedImage(image), null);
    }

    /**
     * filters an image
     *
     * @param image Image
     * @param filter RGBImageFilter
     *
     * @return BufferedImage
     */
    public static BufferedImage filter(final Image image, final RGBImageFilter filter) {
        return ImageTools.toRgbaImage(new Canvas().createImage(new FilteredImageSource(image.getSource(), filter)));
    }

    /**
     * flip a picture
     *
     * @param image : Image : source image
     * @param flip : int : parameter : FLIP_VERTICALLY, FLIP_HORIZONTALLY or FLIP_VERTICALLY|FLIP_HORIZONTALLY
     *
     * @return : BufferedImage : flipped image
     */
    public static BufferedImage flip(final Image image, final int flip) {
        BufferedImage bufferedImage = ImageTools.toBufferedImage(image);

        if (flip == ImageTools.FLIP_VERTICALLY) {
            // Flip the image vertically
            AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));

            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);
        }

        if (flip == ImageTools.FLIP_HORIZONTALLY) {
            // Flip the image horizontally
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);

            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);
        }

        if (flip == (ImageTools.FLIP_VERTICALLY | ImageTools.FLIP_HORIZONTALLY)) {
            // Flip the image vertically and horizontally;
            // equivalent to rotating the image 180 degrees
            AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
            tx.translate(-image.getWidth(null), -image.getHeight(null));

            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = op.filter(bufferedImage, null);
        }

        return bufferedImage;
    }

    /**
     * filters all colors but red from the image
     *
     * @param image : Image : source image
     *
     * @return : BufferedImage : filtered image
     */
    public static BufferedImage getBlue(final Image image) {
        return ImageTools.toBufferedImage(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, int y, final int rgb) {
                y = -1;

                // if (x == -1) {
                // The pixel value is from the image's color table rather than the image itself
                // }
                return (rgb & 0xff0000ff);
            }
        })));
    }

    /**
     * gets the color model of an image
     *
     * @param image : Image : image object
     *
     * @return : ColorModel : images's color model
     */
    public static ColorModel getColorModel(final Image image) {
        try {
            PixelGrabber grabby = new PixelGrabber(image, 0, 0, 1, 1, false);

            if (!grabby.grabPixels()) {
                throw new RuntimeException("pixel grab fails");
            }

            return grabby.getColorModel();
        } catch (final InterruptedException e) {
            return null;
        }
    }

    public static GraphicsConfiguration getDefaultGraphicsConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        return gc;
    }

    // Returns the graphics configuration for the primary screen
    private static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    /**
     * filters all colors but red from the image
     *
     * @param image : Image : source image
     *
     * @return : BufferedImage : filtered image
     */
    public static BufferedImage getGreen(final Image image) {
        return ImageTools.toBufferedImage(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, int y, final int rgb) {
                y = -1;

                // if (x == -1) {
                // The pixel value is from the image's color table rather than the image itself
                // }
                return rgb & 0xff00ff00;
            }
        })));
    }

    /**
     * creates an image from an array of pixel colors
     *
     * @param pixels : int[] : pixel colors (length = wh)
     * @param w : int : width of image
     * @param h : int : heigth of image
     *
     * @return : BufferedImage
     *
     * @throws RuntimeException exception
     */
    public static BufferedImage getImage(final int[] pixels, final int w, final int h) {
        if (pixels.length != (w * h)) {
            throw new RuntimeException("pixels.length <> w*h");
        }

        return ImageTools.createImage(w, h, pixels);
    }

    /**
     * creates an image from an array of pixel colors and a palette
     *
     * @param palette : int[] : palette
     * @param pixels : int[][] : pixel colors
     *
     * @return : BufferedImage
     */
    public static BufferedImage getImage(final int[] palette, final int[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        int[] pix = new int[w * h];

        // convert to RGB
        for (int x = w; x-- > 0;) {
            for (int y = h; y-- > 0;) {
                pix[(y * w) + x] = palette[pixels[x][y]];
            }
        }

        return ImageTools.createImage(w, h, pix);
    }

    /**
     * creates an image from an array of pixel colors
     *
     * @param pixels : int[][] : pixel colors
     *
     * @return : BufferedImage
     */
    public static BufferedImage getImage(final int[][] pixels) {
        int w = pixels.length;
        int h = pixels[0].length;
        int[] pix = new int[w * h];

        // convert to RGB
        for (int x = w; x-- > 0;) {
            for (int y = h; y-- > 0;) {
                pix[(y * w) + x] = pixels[x][y];
            }
        }

        return ImageTools.createImage(w, h, pix);
    }

    /**
     * <p>
     * Returns an array of pixels, stored as integers, from a <code>BufferedImage</code>. The pixels are grabbed from a rectangular area defined by a
     * location and two dimensions. Calling this method on an image of type different from <code>BufferedImage.TYPE_INT_ARGB</code> and
     * <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.
     * </p>
     *
     * @param img the source image
     * @param x the x location at which to start grabbing pixels
     * @param y the y location at which to start grabbing pixels
     * @param w the width of the rectangle of pixels to grab
     * @param h the height of the rectangle of pixels to grab
     * @param pixels a pre-allocated array of pixels of size w*h; can be null
     * @return <code>pixels</code> if non-null, a new array of integers otherwise
     * @throws IllegalArgumentException is <code>pixels</code> is non-null and of length &lt; w*h
     */
    public static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
        if ((w == 0) || (h == 0)) {
            return new int[0];
        }

        if (pixels == null) {
            pixels = new int[w * h];
        } else if (pixels.length < (w * h)) {
            throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
        }

        int imageType = img.getType();
        if ((imageType == BufferedImage.TYPE_INT_ARGB) || (imageType == BufferedImage.TYPE_INT_RGB)) {
            Raster raster = img.getRaster();
            return (int[]) raster.getDataElements(x, y, w, h, pixels);
        }

        // Unmanages the image
        return img.getRGB(x, y, w, h, pixels, 0, w);
    }

    /**
     * gets the pixel colors from an image as an array
     *
     * @param image : Image : image
     *
     * @return : int[][] : pixel colors
     *
     * @throws RuntimeException : pixel-grabber exception
     */
    public static int[][] getPixels(final Image image) throws RuntimeException {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int[] pix = new int[w * h];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, w, h, pix, 0, w);

        try {
            if (grabber.grabPixels() != true) {
                throw new RuntimeException("Grabber returned false: " + grabber.status());
            }
        } catch (final InterruptedException e) {
            //
        }

        int[][] pixels = new int[w][h];

        for (int x = w; x-- > 0;) {
            for (int y = h; y-- > 0;) {
                pixels[x][y] = pix[(y * w) + x];
            }
        }

        return pixels;
    }

    /**
     * getRatio
     */
    private static double getRatio(int width, int height, int iw, int ih) {
        // bv 200x150 image in 100x100 accessory
        double rw = (double) iw / width; // 200/100=2
        double rh = (double) ih / height; // 150/100=1.5
        double r = Math.max(rw, rh); // 2

        return r;
    }

    /**
     * filters all colors but red from the image
     *
     * @param image : Image : source image
     *
     * @return : BufferedImage : filtered image
     */
    public static BufferedImage getRed(final Image image) {
        return ImageTools.toBufferedImage(Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, int y, final int rgb) {
                y = -1;

                // if (x == -1) {
                // The pixel value is from the image's color table rather than the image itself
                // }
                return rgb & 0xffff0000;
            }
        })));
    }

    /**
     * gets point pixel value.<br>
     * keeps image managed
     *
     * @param image source image
     * @param x x location
     * @param y y location
     *
     * @return pixel value
     */
    public static int getRGB(BufferedImage image, int x, int y) {
        int type = image.getType();

        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            return ((int[]) image.getRaster().getDataElements(x, y, 1, 1, null))[0];
        }

        return image.getRGB(x, y);
    }

    /**
     * gets region pixel values.<br>
     * keeps image managed
     *
     * @param image source image
     * @param x x location
     * @param y y location
     * @param width region width
     * @param height region height
     *
     * @return pixel values
     */
    public static int[] getRGB(BufferedImage image, int x, int y, int width, int height) {
        int type = image.getType();

        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, null);
        }

        return image.getRGB(x, y, width, height, null, 0, width);
    }

    /**
     * scale an image
     *
     * @param source : BufferedImage : source image
     * @param factor : double : scale factor (1.0 does not scale)
     *
     * @return : BufferedImage : scaled image (sometimes a BufferedImage)
     */
    public static BufferedImage getScaledInstance(final BufferedImage source, final double factor) {
        return ImageTools.getScaledInstance(source, factor, ImageTools.getDefaultGraphicsConfiguration());
    }

    /**
     * scale an image
     *
     * @param source : BufferedImage : source image
     * @param factor : double : scale factor (1.0 does not scale)
     * @param gc : GraphicsConfiguration : graphics configuration
     *
     * @return : BufferedImage : scaled image (sometimes a BufferedImage)
     */
    public static BufferedImage getScaledInstance(final BufferedImage source, final double factor, GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        if (factor >= 1.0) {
            return ImageTools.getScaledInstance2D(source, factor, RenderingHints.VALUE_INTERPOLATION_BICUBIC, gc);
        }

        return ImageTools.getScaledInstanceAWT(source, factor, Image.SCALE_AREA_AVERAGING);
    }

    /**
     * scale a buffered image
     *
     * @param image : BufferedImage : original image
     * @param width : int : width of scaled image
     * @param height : int : height of scaled image
     *
     * @return : BufferedImage : scaled image
     */
    public static BufferedImage getScaledInstance(final BufferedImage image, final int width, final int height) {
        return ImageTools.getScaledInstance(image, width, height, ImageTools.getDefaultGraphicsConfiguration());
    }

    /**
     * scale a buffered image
     *
     * @param image : BufferedImage : original image
     * @param width : int : width of scaled image
     * @param height : int : height of scaled image
     * @param gc : GraphicsConfiguration : graphics configuration
     *
     * @return : BufferedImage : scaled image
     */
    public static BufferedImage getScaledInstance(final BufferedImage image, final int width, final int height, GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        // int transparency = image.getColorModel().getTransparency();
        int transparency = ImageTools.getColorModel(image).getTransparency();

        return ImageTools.copy(image, gc.createCompatibleImage(width, height, transparency));
    }

    /**
     * scale an image
     *
     * @param source : BufferedImage : source image
     * @param factor : double : scale factor (1.0 does not scale)
     * @param interpolationHint : Object : interpolation hint
     *
     * @return : BufferedImage : scaled image
     *
     * @see java.awt.RenderingHints
     */
    public static BufferedImage getScaledInstance2D(final BufferedImage source, final double factor, final Object interpolationHint) {
        return ImageTools.getScaledInstance2D(source, factor, interpolationHint, ImageTools.getDefaultGraphicsConfiguration());
    }

    /**
     * scale an image
     *
     * @param source : BufferedImage : source image
     * @param factor : double : scale factor (1.0 does not scale)
     * @param interpolationHint : Object : interpolation hint
     * @param gc : GraphicsConfiguration : graphics configuration
     *
     * @return : BufferedImage : scaled image
     *
     * @see java.awt.RenderingHints
     */
    public static BufferedImage getScaledInstance2D(final BufferedImage source, final double factor, final Object interpolationHint,
            GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        // int transparency = source.getColorModel().getTransparency();
        int transparency = ImageTools.getColorModel(source).getTransparency();

        return ImageTools.copy(source, gc.createCompatibleImage(w, h, transparency), interpolationHint);
    }

    /**
     * scale an image
     *
     * @param source : BufferedImage : source image
     * @param factor : double : scale factor (1.0 does not scale)
     * @param hint : int : scale hint Image.SCALE_DEFAULT, Image.SCALE_FAST, Image.SCALE_SMOOTH or Image.SCALE_AREA_AVERAGING
     *
     * @return : BufferedImage : scaled image
     */
    public static BufferedImage getScaledInstanceAWT(final BufferedImage source, final double factor, final int hint) {
        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);

        return ImageTools.toBufferedImage(source.getScaledInstance(w, h, hint));
    }

    /**
     * gets the pixel colors from an image as an array
     *
     * @param image : Image : image
     *
     * @return : int[] : pixel colors
     *
     * @throws RuntimeException : pixel-grabber exception
     */
    public static int[] getSimplePixels(final Image image) throws RuntimeException {
        int w = image.getWidth(null);
        int h = image.getHeight(null);

        int[] pix = new int[w * h];

        PixelGrabber grabber = new PixelGrabber(image, 0, 0, w, h, pix, 0, w);

        try {
            if (grabber.grabPixels() != true) {
                throw new RuntimeException("Grabber returned false: " + grabber.status());
            }
        } catch (final InterruptedException e) {
            //
        }

        return pix;
    }

    /**
     * grayscales an image
     *
     * @param image : Image : source
     *
     * @return : BufferedImage : grayscale image
     *
     * @throws RuntimeException exception
     */
    public static BufferedImage grayscale(final Image image) throws RuntimeException {
        return ImageTools.toBufferedImage(new GrayscaleImageFilter().execute(image));
    }

    /**
     * grayscales an image but keeps transparency
     *
     * @param image : Image : source
     *
     * @return : BufferedImage : grayscale image
     *
     * @throws RuntimeException exception
     */
    public static BufferedImage grayscaleKeepTransparency(final Image image) throws RuntimeException {
        final BufferedImage original = ImageTools.toBufferedImage(image);
        final BufferedImage gray = ImageTools.grayscale(original);

        return ImageTools.filter(gray, new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int pixel) {
                int red = (pixel & 0x00ff0000) >> 16;
                int green = (pixel & 0x0000ff00) >> 8;
                int blue = pixel & 0x000000ff;

                int alpha = (original.getRGB(x, y) & 0xff000000) >> 24;

                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        });
    }

    /**
     * this method returns true if the specified image has transparent pixels
     *
     * @param image : Image : image object
     *
     * @return : boolean : image has alpha channel
     */
    public static boolean hasAlpha(final Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;

            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);

        try {
            pg.grabPixels();
        } catch (final InterruptedException e) {
            //
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();

        if (cm == null) {
            return false;
        }

        return cm.hasAlpha();
    }

    public static boolean isAcceleratedVolatileImageReady(VolatileImage vi, GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        return vi.validate(gc) != VolatileImage.IMAGE_OK;
    }

    /**
     * <p>
     * Returns a new compatible image from a URL. The image is loaded from the specified location and then turned, if necessary into a compatible
     * image.
     * </p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param resource the URL of the picture to load as a compatible image
     * @return a new translucent compatible <code>BufferedImage</code> of the specified width and height
     * @throws java.io.IOException if the image cannot be read or loaded
     */
    public static BufferedImage loadCompatibleImage(URL resource) throws IOException {
        BufferedImage image = ImageIO.read(resource);
        return ImageTools.toCompatibleImage(image);
    }

    /**
     * this funcion makes sure that an Image is fully loaded
     *
     * @param image : Image : image to load
     *
     * @throws RuntimeException : image loading failed
     */
    public static void loadImage(final Image image) throws RuntimeException {
        try {
            if (image instanceof BufferedImage) {
                return;
            }

            MediaTracker tracker = new MediaTracker(ImageTools.jComponent);
            tracker.addImage(image, 0);
            tracker.waitForID(0);

            if (MediaTracker.COMPLETE != tracker.statusID(0, false)) {
                // throw new RuntimeException("image loading fails");
            }
        } catch (final InterruptedException e) {
            //
        }
    }

    /**
     * makes a photo negative of image
     *
     * @param image : Image : source image
     *
     * @return : BufferedImage : photo negative of image
     */
    public static BufferedImage negative(final Image image) {
        RescaleOp op = new RescaleOp(-1.0f, 255f, null);

        return op.filter(ImageTools.toBufferedImage(image), null);
    }

    /**
     * creates a new buffered image with black background
     *
     * @param w : int : width
     * @param h : int : height
     *
     * @return : BufferedImage : empty image
     */
    public static BufferedImage newBufferedImage(final int w, final int h) {
        return ImageTools.newBufferedImage(w, h, Color.BLACK);
    }

    /**
     * creates a new buffered image with given background-color
     *
     * @param w : int : width
     * @param h : int : height
     * @param c : Color : background-color
     *
     * @return : BufferedImage : empty image
     */
    public static BufferedImage newBufferedImage(final int w, final int h, final Color c) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setColor(c);
        g2d.fillRect(0, 0, w, h);
        g2d.dispose();

        return bi;
    }

    /**
     * creates a new buffered image with transparent background
     *
     * @param w : int : width
     * @param h : int : height
     *
     * @return : BufferedImage : empty image
     */
    public static BufferedImage newTransparentBufferedImage(final int w, final int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * read a file (image-file) to a buffered image
     *
     * @param f : File : image file
     *
     * @return : BufferedImage : buffered image
     *
     * @throws RuntimeException : image read exception
     * @throws IOWrapException na
     */
    public static BufferedImage read(final File f) throws IOException {
        return ImageTools.toBufferedImage(ImageIO.read(f));
    }

    /**
     * read a file (image-file) to a buffered image
     *
     * @param f : String : image file-path
     *
     * @return : BufferedImage : buffered image
     *
     * @throws RuntimeException : image read exception
     */
    public static BufferedImage read(final String f) throws IOException {
        return ImageTools.read(new File(f));
    }

    /**
     * removes transparency from an image (converts RGBA to RGB)
     *
     * @param source : Image : any Image or BufferedImage in RGBA or RGB
     *
     * @return : BufferedImage : RGB format
     */
    public static BufferedImage removeTransparency(final Image source) {
        return ImageTools.toBufferedImage(source, ImageTools.TYPE_INT_RGB);
    }

    /**
     * replaces a given color by another given color
     *
     * @param image : Image : source image
     * @param source : Color : source color to replace
     * @param target : Color : target color, replaced by this
     *
     * @return : BufferedImage : new image
     */
    public static BufferedImage replaceColor(final Image image, final Color source, final Color target) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                if (source.getRGB() == rgb) {
                    return target.getRGB();
                }

                return rgb;
            }
        };

        return ImageTools.toRgbaImage(new Canvas().createImage(new FilteredImageSource(image.getSource(), filter)));
    }

    /**
     * replaces transparency from an image by given color (converts RGBA to RGB)
     *
     * @param image : Image : any Image or BufferedImage in RGBA or RGB
     * @param backgroundColor : Color : background color
     *
     * @return : BufferedImage : RGB format
     */
    public static BufferedImage replaceTransparency(final Image image, final Color backgroundColor) {
        BufferedImage biForeground = ImageTools.toBufferedImage(image);
        BufferedImage biBackground = ImageTools.newBufferedImage(biForeground.getWidth(), biForeground.getHeight(), backgroundColor);
        Graphics2D g2d = biBackground.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.drawImage(biForeground, 0, 0, null);
        g2d.dispose();

        return ImageTools.removeTransparency(biBackground);
    }

    /**
     * internally used by {@link #brighten(Image,int)} and<br>
     * {@link #darken(Image,int)}
     *
     * @param image : Image : source
     * @param percentage : float : percentage
     *
     * @return : BufferedImage : darkened or brightened image
     */
    private static BufferedImage rescale(final Image image, final float percentage) {
        RescaleOp op = new RescaleOp(percentage, 0, null);

        return op.filter(ImageTools.toBufferedImage(image), null);
    }

    /**
     * resize, vergroten gebeurt niet
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        int type = (image.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * resize met behoudt van aspect ration, vergroten gebeurt niet
     */
    public static BufferedImage resizeKeepAspect(BufferedImage img, int width, int height) {
        int iw = img.getWidth();
        int ih = img.getHeight();

        if ((iw > width) || (ih > height)) {
            double r = ImageTools.getRatio(width, height, iw, ih);
            iw = (int) (iw / r); // 200/2=100
            ih = (int) (ih / r); // 150/2=75

            return ImageTools.resize(img, iw, ih);
        }

        return img;
    }

    /**
     * resize en blur, vergroten gebeurt niet
     */
    public static BufferedImage resizeTrick(BufferedImage image, int width, int height) {
        image = ImageTools.createCompatibleImage(image);
        image = ImageTools.resize(image, 100, 100);
        image = ImageTools.blurImage(image);

        return ImageTools.resize(image, width, height);
    }

    /**
     * resize en blur met behoudt van aspect ration, vergroten gebeurt niet
     */
    public static BufferedImage resizeTrickKeepAspect(BufferedImage img, int width, int height) {
        int iw = img.getWidth();
        int ih = img.getHeight();

        if ((iw > width) || (ih > height)) {
            double r = ImageTools.getRatio(width, height, iw, ih);
            iw = (int) (iw / r); // 200/2=100
            ih = (int) (ih / r); // 150/2=75

            return ImageTools.resizeTrick(img, iw, ih);
        }

        return img;
    }

    /**
     * <p>
     * Writes a rectangular area of pixels in the destination <code>BufferedImage</code>. Calling this method on an image of type different from
     * <code>BufferedImage.TYPE_INT_ARGB</code> and <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.
     * </p>
     *
     * @param img the destination image
     * @param x the x location at which to start storing pixels
     * @param y the y location at which to start storing pixels
     * @param w the width of the rectangle of pixels to store
     * @param h the height of the rectangle of pixels to store
     * @param pixels an array of pixels, stored as integers
     * @throws IllegalArgumentException is <code>pixels</code> is non-null and of length &lt; w*h
     */
    public static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
        if ((pixels == null) || (w == 0) || (h == 0)) {
            return;
        } else if (pixels.length < (w * h)) {
            throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
        }

        int imageType = img.getType();
        if ((imageType == BufferedImage.TYPE_INT_ARGB) || (imageType == BufferedImage.TYPE_INT_RGB)) {
            WritableRaster raster = img.getRaster();
            raster.setDataElements(x, y, w, h, pixels);
        } else {
            // Unmanages the image
            img.setRGB(x, y, w, h, pixels, 0, w);
        }
    }

    /**
     * sets single pixel value.<br>
     * keeps image managed
     *
     * @param image source image
     * @param x x location
     * @param y y location
     * @param pixel pixel value
     *
     * @return source image
     */
    public static BufferedImage setRGB(BufferedImage image, int x, int y, int pixel) {
        return ImageTools.setRGB(image, x, y, 1, 1, new int[] { pixel });
    }

    /**
     * sets region pixel values.<br>
     * keeps image managed
     *
     * @param image source image
     * @param x x location
     * @param y y location
     * @param width region width
     * @param height region height
     * @param pixels pixels to set
     *
     * @return source image
     *
     * @throws IllegalArgumentException IllegalArgumentException
     */
    public static BufferedImage setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        if ((width * height) != pixels.length) {
            throw new IllegalArgumentException("width multiplied by height should equal pixels.length");
        }

        int type = image.getType();

        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }

        return image;
    }

    /**
     * sets region pixel values.<br>
     * keeps image managed
     *
     * @param image source image
     * @param x x location
     * @param y y location
     * @param width region width
     * @param pixels pixels to set
     *
     * @return source image
     *
     * @throws IllegalArgumentException IllegalArgumentException
     */
    public static BufferedImage setRGB(BufferedImage image, int x, int y, int width, int[] pixels) {
        if ((pixels.length % width) != 0) {
            throw new IllegalArgumentException("pixels.length divided by width should be a natural number");
        }

        return ImageTools.setRGB(image, x, y, width, pixels.length / width, pixels);
    }

    /**
     * sharpens an image
     *
     * @param image : Image : source
     * @param strength : int : strength (0=original, 5=medium, 10=strong, ...)
     *
     * @return : BufferedImage : sharpened image
     */
    public static BufferedImage sharpen(final Image image, final int strength) {
        float s = (float) (strength - 1) / 4;
        float[] elements = { 0.0f, -s, 0.0f, -s, strength, -s, 0.0f, -s, 0.0f };

        Kernel kernel = new Kernel(3, 3, elements);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        return cop.filter(ImageTools.toBufferedImage(image), null);
    }

    /**
     * creates an accelerated image from a buffered image using Graphics2D
     *
     * @param image : BufferedImage : buffered image
     *
     * @return : BufferedImage : accelerated image
     */
    public static BufferedImage toAcceleratedImage(final BufferedImage image) {
        return ImageTools.toAcceleratedImage(image, ImageTools.getDefaultGraphicsConfiguration());
    }

    /**
     * creates an accelerated image from a buffered image using Graphics2D
     *
     * @param image : BufferedImage : buffered image
     * @param gc : GraphicsConfiguration : graphics configuration
     *
     * @return : BufferedImage : accelerated image
     */
    public static BufferedImage toAcceleratedImage(final BufferedImage image, GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        int w = image.getWidth();
        int h = image.getHeight();
        // int transparency = image.getColorModel().getTransparency();
        int transparency = ImageTools.getColorModel(image).getTransparency();
        BufferedImage result = gc.createCompatibleImage(w, h, transparency);
        Graphics2D g2 = result.createGraphics();
        g2.drawRenderedImage(image, null);
        g2.dispose();

        return result;
    }

    /**
     * calls {@link #toAcceleratedVolatileImage(BufferedImage)} with default configuration
     *
     * @param image
     * @return
     */
    public static VolatileImage toAcceleratedVolatileImage(final BufferedImage image) {
        return ImageTools.toAcceleratedVolatileImage(image, ImageTools.getDefaultGraphicsConfiguration());
    }

    /**
     * create a volatileimage from source
     *
     * @param image
     * @param gc
     * @return
     */
    public static VolatileImage toAcceleratedVolatileImage(final BufferedImage source, GraphicsConfiguration gc) {
        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        VolatileImage vimage = ImageTools.createVolatileImage(source.getWidth(), source.getHeight(), Transparency.OPAQUE);
        Graphics2D g = vimage.createGraphics();
        g.drawImage(source, null, 0, 0);
        g.dispose();

        return vimage;
    }

    /**
     * creates a buffered image from a normal image
     *
     * @param image : Image : image
     *
     * @return : BufferedImage : buffered image
     */
    public static BufferedImage toBufferedImage(final Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        ImageTools.loadImage(image);

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = ImageTools.hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;

            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (final HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;

            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }

            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * creates a buffered image from a normal image
     *
     * @param image : Image : image
     * @param cm : ColorModel : color model
     *
     * @return : BufferedImage : buffered image
     */
    public static BufferedImage toBufferedImage(final Image image, final ColorModel cm) {
        if (image instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) image;

            if (cm.equals(bi.getColorModel())) {
                return bi;
            }
        }

        ImageTools.loadImage(image);

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        boolean alphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = cm.createCompatibleWritableRaster(w, h);
        BufferedImage result = new BufferedImage(cm, raster, alphaPremultiplied, null);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return result;
    }

    /**
     * creates a buffered image from a normal image
     *
     * @param image : Image : image
     * @param gc : GraphicsConfiguration : graphics configuration
     *
     * @return : BufferedImage : buffered image
     *
     * @throws RuntimeException : getting color model failed
     */
    public static BufferedImage toBufferedImage(final Image image, GraphicsConfiguration gc) throws RuntimeException {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        if (gc == null) {
            gc = ImageTools.getDefaultGraphicsConfiguration();
        }

        ImageTools.loadImage(image);

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int transparency = ImageTools.getColorModel(image).getTransparency();

        BufferedImage result = gc.createCompatibleImage(w, h, transparency);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return result;
    }

    /**
     * creates a buffered image from a normal image
     *
     * @param image : Image : image
     * @param type : int : image type, use Image. ..., TYPE_INT_RGB, TYPE_INT_ARGB, TYPE_INT_ARGB_PRE, TYPE_INT_BGR, TYPE_3BYTE_BGR, TYPE_4BYTE_ABGR,
     *            TYPE_4BYTE_ABGR_PRE, TYPE_BYTE_GRAY, TYPE_USHORT_GRAY, TYPE_BYTE_BINARY, TYPE_BYTE_INDEXED, TYPE_USHORT_565_RGB or
     *            TYPE_USHORT_555_RGB
     *
     * @return : BufferedImage : buffered image
     */
    public static BufferedImage toBufferedImage(final Image image, final int type) {
        if (image instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) image;

            if (bi.getType() == type) {
                return bi;
            }
        }

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage result = new BufferedImage(w, h, type);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return result;
    }

    /**
     * <p>
     * Return a new compatible image that contains a copy of the specified image. This method ensures an image is compatible with the hardware, and
     * therefore optimized for fast blitting operations.
     * </p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @param image the image to copy into a new compatible image
     * @return a new compatible copy, with the same width and height and transparency and content, of <code>image</code>
     */
    public static BufferedImage toCompatibleImage(BufferedImage image) {
        if (image.getColorModel().equals(ImageTools.getGraphicsConfiguration().getColorModel())) {
            return image;
        }

        BufferedImage compatibleImage = ImageTools.getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(),
                image.getTransparency());
        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return compatibleImage;
    }

    /**
     * na
     *
     * @param image na
     *
     * @return
     */
    public static Icon toIcon(Image image) {
        return new ImageIcon(image);
    }

    /**
     * na
     *
     * @param icon na
     *
     * @return
     */
    public static Image toImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }

        BufferedImage tmp = ImageTools.newTransparentBufferedImage(icon.getIconWidth(), icon.getIconHeight());
        icon.paintIcon(ImageTools.jComponent, tmp.createGraphics(), icon.getIconWidth(), icon.getIconHeight());

        return tmp;
    }

    /**
     * converts any image to a MemoryImageSource
     *
     * @param image : Image : source image
     *
     * @return : MemoryImageSource
     *
     * @throws RuntimeException : exception
     *
     * @see java.awt.image.MemoryImageSource
     */
    public static MemoryImageSource toMemoryImageSource(final Image image) throws RuntimeException {
        BufferedImage bi = ImageTools.toBufferedImage(image);

        return new MemoryImageSource(bi.getWidth(), bi.getHeight(), ImageTools.getSimplePixels(bi), 0, bi.getWidth());
    }

    /**
     * converts an image to an RGB image with an alpha channel
     *
     * @param image : Image : image
     *
     * @return : BufferedImage : RGBA image
     */
    public static BufferedImage toRgbaImage(final Image image) {
        return ImageTools.toBufferedImage(image, ImageTools.TYPE_INT_ARGB);
    }

    /**
     * converts an image to an RGB image without an alpha channel
     *
     * @param image : Image : image
     *
     * @return : BufferedImage : RGB image
     */
    public static BufferedImage toRgbImage(final Image image) {
        return ImageTools.toBufferedImage(image, ImageTools.TYPE_INT_RGB);
    }

    /**
     * replaces the black color in an RGB image by transparency
     *
     * @param image : Image : source image (normally an RGB image but can by an RGBA image)
     *
     * @return : BufferedImage : image with transparency
     */
    public static BufferedImage transparent(final Image image) {
        return ImageTools.transparent(image, Color.BLACK);
    }

    /**
     * replaces given color in an RGB image by transparency
     *
     * @param image : Image : source image (normally an RGB image but can by an RGBA image)
     * @param source : Color : color to remove
     *
     * @return : BufferedImage : image with transparency
     */
    public static BufferedImage transparent(final Image image, final Color source) {
        return ImageTools.replaceColor(ImageTools.toBufferedImage(image), source, ImageTools.TRANSPARENT);
    }

    /**
     * replaces given color in an RGB image by transparency
     *
     * @param image : Image : source image (normally an RGB image but can by an RGBA image)
     * @param source : Color : color to remove
     * @param range : int : range
     *
     * @return : BufferedImage : image with transparency
     */
    public static BufferedImage transparent(final Image image, final Color source, final int range) {
        return ImageTools.filter(ImageTools.toBufferedImage(image), new TransparencyFilter(source, range));
    }

    /**
     * replaces given color in an RGB image by transparency
     *
     * @param image : Image : source image (normally an RGB image but can by an RGBA image)
     * @param range : int : range
     *
     * @return : BufferedImage : image with transparency
     */
    public static BufferedImage transparent(final Image image, final int range) {
        return ImageTools.transparent(image, Color.BLACK, range);
    }

    /** jComponent */
    public static final JComponent jComponent = new JComponent() {
        private static final long serialVersionUID = 2879082751515469986L;
    };

    /** full transparency or otherwise black */
    public static final Color TRANSPARENT = new Color(0.f, 0.f, 0.f, 0.0f);

    /** parameter: color model */
    public static final int TYPE_3BYTE_BGR = BufferedImage.TYPE_3BYTE_BGR;

    /** parameter: color model */
    public static final int TYPE_4BYTE_ABGR = BufferedImage.TYPE_4BYTE_ABGR;

    /** parameter: color model */
    public static final int TYPE_4BYTE_ABGR_PRE = BufferedImage.TYPE_4BYTE_ABGR_PRE;

    /** parameter: color model */
    public static final int TYPE_BYTE_BINARY = BufferedImage.TYPE_BYTE_BINARY;

    /** parameter: color model */
    public static final int TYPE_BYTE_GRAY = BufferedImage.TYPE_BYTE_GRAY;

    /** parameter: color model */
    public static final int TYPE_BYTE_INDEXED = BufferedImage.TYPE_BYTE_INDEXED;

    /** parameter: color model */
    public static final int TYPE_CUSTOM = BufferedImage.TYPE_CUSTOM;

    /** parameter: color model */
    public static final int TYPE_INT_ARGB = BufferedImage.TYPE_INT_ARGB;

    /** parameter: color model */
    public static final int TYPE_INT_ARGB_PRE = BufferedImage.TYPE_INT_ARGB_PRE;

    /** parameter: color model */
    public static final int TYPE_INT_BGR = BufferedImage.TYPE_INT_BGR;

    /** parameter: color model */
    public static final int TYPE_INT_RGB = BufferedImage.TYPE_INT_RGB;

    /** parameter: color model */
    public static final int TYPE_USHORT_555_RGB = BufferedImage.TYPE_USHORT_555_RGB;

    /** parameter: color model */
    public static final int TYPE_USHORT_565_RGB = BufferedImage.TYPE_USHORT_565_RGB;

    /** parameter: color model */
    public static final int TYPE_USHORT_GRAY = BufferedImage.TYPE_USHORT_GRAY;

    /** parameter for flipping */
    public static final int FLIP_VERTICALLY = 1;

    /** parameter for flipping */
    public static final int FLIP_HORIZONTALLY = 8;

    // public static BufferedImage createCompatibleImage(BufferedImage image) {
    // // GraphicsConfiguration gc =
    // // sun.awt.image.BufferedImageGraphicsConfig.getConfig(image);// Sun
    // // proprietary API
    // GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    // int w = image.getWidth();
    // int h = image.getHeight();
    // BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
    // Graphics2D g2 = result.createGraphics();
    // g2.drawRenderedImage(image, null);
    // g2.dispose();
    // return result;
    // }
}
