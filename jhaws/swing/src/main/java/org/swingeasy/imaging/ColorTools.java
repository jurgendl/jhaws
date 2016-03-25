package org.swingeasy.imaging;

import java.awt.Color;
import java.awt.image.ColorModel;

public class ColorTools {
    /**
     * converts a Color object to its hexadecimal representation ( #000000 - #FFFFFF )
     *
     * @param c : Color : color
     *
     * @return : String : hexadecimal representation ( #000000 - #FFFFFF )
     */
    public static String colorToHex(final Color c) {
        return "#" + HexString.bufferToHex(new byte[] { (byte) c.getRed() }) + HexString.bufferToHex(new byte[] { 
                (byte) c.getGreen() }) + HexString.bufferToHex(new byte[] { (byte) c.getBlue() });
    }

    /**
     * converts a Color to it's int value
     *
     * @param color : Color : color
     *
     * @return : int : color int value
     */
    public static int convertColor2Pixel(final Color color) {
        return color.getRGB();
    }

    /**
     * converts a Color to it's RGB int values (0-255)
     *
     * @param color : Color : color
     *
     * @return : int[] : RGB values (0-255)
     */
    public static int[] convertColor2Rgb(final Color color) {
        return new int[] { color.getRed(), color.getGreen(), color.getBlue() };
    }

    /**
     * na
     *
     * @param color na
     *
     * @return
     */
    public static int[] convertColor2Rgba(final Color color) {
        return new int[] { color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() };
    }

    /**
     * converts a int color value to a Color objects
     *
     * @param rgbpixel : int : color int value
     *
     * @return : Color : color
     */
    public static Color convertPixel2Color(final int rgbpixel) {
        return new Color(rgbpixel);
    }

    /**
     * converts a int color value to RGB int values (0-255)
     *
     * @param rgbpixel : int : color int value
     *
     * @return : int[] : RGB values (0-255)
     */
    public static int[] convertPixel2Rgb(final int rgbpixel) {
        Color c = new Color(rgbpixel);

        return new int[] { c.getRed(), c.getGreen(), c.getBlue() };
    }

    /**
     * na
     *
     * @param pixel na
     *
     * @return
     */
    public static int[] convertPixel2Rgba(final int pixel) {
        int a = (pixel >> 24) & 0xff;
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;

        return new int[] { r, g, b, a };
    }

    /**
     * converts RGB int values (0-255) to a Color object
     *
     * @return : Color : color
     */
    public static Color convertRgb2Color(final int... rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * converts RGB int values (0-255) to a color int value
     *
     * @return : int : color int value
     */
    public static int convertRgb2Pixel(final int... rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]).getRGB();
    }

    /**
     * converts RGBA int values (0-255) to a color int value
     *
     * @return : int : color int value
     */
    public static int convertRgba2Pixel(final int... rgba) {
        return new Color(rgba[0], rgba[1], rgba[2], rgba[3]).getRGB();
    }

    /**
     * drop middle color in array
     *
     * @return : Color[] : array of color (#L-1)
     */
    private static Color[] dropMiddle(final Color... c) {
        if ((c.length % 2) == 0) {
            return c;
        }

        Color[] tmp = new Color[c.length - 1];
        int index = 0;

        for (int i = 0; i < c.length; i++) {
            if ((i + 1) == ((c.length + 1) / 2)) {
                continue;
            }

            tmp[index++] = c[i];
        }

        return tmp;
    }

    /**
     * returns two analog colors for given base color
     *
     * @param baseColor : Color : base color
     * @param halfnumberdifs : int : half the number of changes
     *
     * @return : Color[] : two analog colors for given base color
     */
    public static Color[] getAnalog(final Color baseColor, final int halfnumberdifs) {
        return ColorTools.dropMiddle(ColorTools.getDegreeColors(baseColor, halfnumberdifs, 20));
    }

    /**
     * returns two analog colors for given base color, degreedif is 20� (from counter clockwise to clockwise)
     *
     * @param baseColor : Color : base color
     * @param halfnumberdifs : int : half the number of changes
     * @param degreedif : int : difference per step in �
     *
     * @return : Color[] : two analog colors for given base color
     */
    public static Color[] getAnalog(final Color baseColor, final int halfnumberdifs, final int degreedif) {
        return ColorTools.dropMiddle(ColorTools.getDegreeColors(baseColor, halfnumberdifs, degreedif));
    }

    /**
     * gets #differences monotone cromatic variances of given base color (given base color is part of that array)
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : monotone cromatic variances of given base color
     */
    public static Color[] getBrightnessDifs(final Color baseColor, final int differences) {
        return ColorTools.getHSBDifs(baseColor, differences, 2);
    }

    /**
     * replaces (or puts) given alpha channel in given color
     *
     * @param c : Color : color
     * @param alpha : int : alpha (0=transparent - 255=opaque)
     *
     * @return : int : color int rgba value
     */
    public static int getColor(final Color c, final int alpha) {
        return ColorTools.getColor(ColorTools.getOpaqueColor(c), alpha);
    }

    /**
     * replaces (or puts) given alpha channel in given color
     *
     * @param rgb : int : rgb value (when rgba, alpha will be dropped first)
     * @param alpha : int : alpha (0=transparent - 255=opaque)
     *
     * @return : int : color int rgba value
     */
    public static int getColor(final int rgb, final int alpha) {
        int[] rgbc = ColorTools.convertPixel2Rgb(rgb);

        return ColorTools.getColor(rgbc[0], rgbc[1], rgbc[2], alpha);
    }

    /**
     * replaces (or puts) given alpha channel in given color
     *
     * @param red : int : red (0-255)
     * @param green : int : green (0-255)
     * @param blue : int : blue (0-255)
     * @param alpha : int : alpha (0=transparent - 255=opaque)
     *
     * @return : int : color int rgba value
     */
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int rgba = (alpha << 24) | (red << 16) | (green << 8) | blue;

        return rgba;
    }

    /**
     * returns the complementary color for given color
     *
     * @param baseColor : Color : base color
     *
     * @return : Color : it's complementary color
     */
    public static Color getComplementary(final Color baseColor) {
        return ColorTools.getInverse(baseColor);
    }

    /**
     * will search for colors on a color disk given degrees to turn (counter-)clockwise, see parameters and return value
     *
     * @param baseColor : Colors : base color
     * @param halfnumberdifs : int : will search for #halfnumberdifs colors clockwise and #halfnumberdifs colors counter-clockwise
     * @param degrees : int : number of degrees (counter-)colockwise to turn
     *
     * @return : Color[] : 2 #halfnumberdifs + 1 long array of colors, middle one is original color
     */
    public static Color[] getDegreeColors(final Color baseColor, final int halfnumberdifs, final int degrees) {
        float change = (float) degrees / 360;
        float[] hsb = ColorTools.rgb2HSBFloat(baseColor);
        Color[] cc = new Color[(halfnumberdifs * 2) + 1];

        for (int i = -halfnumberdifs; i <= +halfnumberdifs; i++) {
            int index = i + halfnumberdifs;
            float h = hsb[0] + (i * change);

            if (h < 0.0F) {
                h += 1.0F;
            }

            int rgb = ColorTools.hsb2RGB(h, hsb[1], hsb[2]);
            cc[index] = new Color(rgb);
        }

        return cc;
    }

    /**
     * gets grayscale of color
     *
     * @param pixel : Color : color
     *
     * @return : int : color int value
     */
    public static int getGrayscale(final Color pixel) {
        return ColorTools.getGrayscale(pixel.getRGB());
    }

    /**
     * gets grayscale of color
     *
     * @param pixel : int : color int value
     *
     * @return : int : color int value
     */
    public static int getGrayscale(final int pixel) {
        // Get the average RGB intensity
        int red = (pixel & 0x00ff0000) >> 16;
            int green = (pixel & 0x0000ff00) >> 8;
        int blue = pixel & 0x000000ff;

        // get luma from rgb
        int luma = (int) ((0.299 * red) + (0.587 * green) + (0.114 * blue));

        // Return the luma value as the value for each RGB component
        // Note: Alpha (transparency) is always set to max (not transparent)
        int gray = (0xff << 24) | (luma << 16) | (luma << 8) | luma;

        return gray;
    }

    /**
     * gets grayscale of color
     *
     * @param r : int : red (0-255)
     * @param g : int : green (0-255)
     * @param b : int : blue (0-255)
     *
     * @return : int : color int value
     */
    public static int getGrayscale(final int r, final int g, final int b) {
        return ColorTools.getGrayscale(new Color(r, g, b));
    }

    /**
     * internal use
     *
     * @param baseColor internal use
     * @param differences internal use
     * @param hsbi internal use
     *
     * @return internal use
     */
    public static Color[] getHSBDifs(final Color baseColor, final int differences, final int hsbi) {
        int d = (differences * 2) + 1;
        Color[] cc = new Color[d];
        float[] hsb = ColorTools.rgb2HSBFloat(baseColor);
        float fdif = 1.0f / d;
        float start = hsb[hsbi];

        if (start == 0.0f) {
            fdif = 0.0f;
        } else {
            if (start == 1.0f) {
                start = fdif;
            } else {
                while ((start - fdif) > 0.0f) {
                    start -= fdif;
                }
            }
        }

        for (int i = 0; i <= (differences * 2); i++) {
            float hsbtmp = start + (fdif * i);
            int rgb = -1;

            if (hsbi == 0) {
                rgb = ColorTools.hsb2RGB(hsbtmp, hsb[1], hsb[2]);
            }

            if (hsbi == 1) {
                rgb = ColorTools.hsb2RGB(hsb[0], hsbtmp, hsb[2]);
            }

            if (hsbi == 2) {
                rgb = ColorTools.hsb2RGB(hsb[0], hsb[1], hsbtmp);
            }

            cc[i] = new Color(rgb);
        }

        return cc;
    }

    /**
     * gets the hue difference of given color
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : saturation difference of given color (given color is part of the array)
     */
    public static Color[] getHueDifs(final Color baseColor, final int differences) {
        return ColorTools.getHSBDifs(baseColor, differences, 0);
    }

    /**
     * gets the intesity difference of given color
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : saturation difference of given color (given color is part of the array)
     *
     * @see #getSaturationDifs(Color, int)
     */
    public static Color[] getIntensityDifs(final Color baseColor, final int differences) {
        return ColorTools.getSaturationDifs(baseColor, differences);
    }

    /**
     * returns the inverse color for given color
     *
     * @param baseColor : Color : base color
     *
     * @return : Color : it's inverse color
     */
    public static Color getInverse(final Color baseColor) {
        return new Color(255 - baseColor.getRed(), 255 - baseColor.getGreen(), 255 - baseColor.getBlue());
    }

    /**
     * gets the graycsale of monotone cromatic variances of given base color
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : monotone acromatic variances of given base color
     */
    public static Color[] getMonotoneAcromatic(final Color baseColor, final int differences) {
        Color[] mcs = ColorTools.getMonotoneCromatic(baseColor, differences);

        for (int i = 0; i < mcs.length; i++) {
            mcs[i] = new Color(ColorTools.getGrayscale(mcs[i].getRGB()));
        }

        return mcs;
    }

    /**
     * gets #differences monotone cromatic variances of given base color (given base color is part of that array)
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : monotone cromatic variances of given base color
     */
    public static Color[] getMonotoneCromatic(final Color baseColor, final int differences) {
        return ColorTools.getBrightnessDifs(baseColor, differences);
    }

    /**
     * converts rgba to rgb
     *
     * @param c : Color : rgb color
     *
     * @return : int : color int rgb value
     */
    public static int getOpaqueColor(final Color c) {
        return ColorTools.getOpaqueColor(c.getRGB());
    }

    /**
     * converts rgba to rgb
     *
     * @param argb : int : color int rgba value
     *
     * @return : int : color int rgb value
     */
    public static int getOpaqueColor(final int argb) {
        // int a = (argb >> 24) & 0xff;
        int r = (argb >> 16) & 0xff;
        int g = (argb >> 8) & 0xff;
        int b = argb & 0xff;
        int rgb = (r << 16) | (g << 8) | b;

        return rgb;
    }

    /**
     * gets the saturation difference of given color
     *
     * @param baseColor : Color : base color
     * @param differences : int : total number of variances
     *
     * @return : Color[] : saturation difference of given color (given color is part of the array)
     */
    public static Color[] getSaturationDifs(final Color baseColor, final int differences) {
        return ColorTools.getHSBDifs(baseColor, differences, 1);
    }

    /**
     * gets #differences of shades of the given color
     *
     * @param baseColor : Color : base color
     * @param differences : int : number of shades you want
     *
     * @return : Color : shades of the given color
     */
    public static Color[] getShades(final Color baseColor, final int differences) {
        float[] hsb = ColorTools.rgb2HSBFloat(baseColor);
        float step = hsb[2] / differences;
        Color[] tmp = new Color[differences];

        for (int i = 0; i < 0; i++) {
            hsb[2] -= step;
            tmp[i] = new Color(ColorTools.hsb2RGB(hsb));
        }

        return tmp;
    }

    /**
     * gets the 2 split complementary colors for given base color
     *
     * @param baseColor : Color : base color
     *
     * @return Color[] : 2 split complementary colors
     */
    public static Color[] getSplitComplementary(final Color baseColor) {
        return ColorTools.dropMiddle(ColorTools.getDegreeColors(baseColor, 1, 150));
    }

    /**
     * gets the 2 triad colors of given base color (counterclockwise)
     *
     * @param baseColor : Color : base color
     *
     * @return : Color[] : triad colors
     */
    public static Color[] getTetrad(final Color baseColor) {
        Color[] tmp = ColorTools.getDegreeColors(baseColor, 3, 90);
        Color[] tetrad = new Color[3];

        for (int i = 0; i < 3; i++) {
            tetrad[i] = tmp[i];
        }

        return tetrad;
    }

    /**
     * gets #differences of tints of the given color
     *
     * @param baseColor : Color : base color
     * @param differences : int : number of tints you want
     *
     * @return : Color[] : tints of the given color
     */
    public static Color[] getTints(final Color baseColor, final int differences) {
        return ColorTools.getHueDifs(baseColor, differences);
    }

    /**
     * gets the 2 triad colors of given base color (first the counterclockwise, the the clockwise)
     *
     * @param baseColor : Color : base color
     *
     * @return : Color[] : triad colors
     */
    public static Color[] getTriad(final Color baseColor) {
        int r = baseColor.getRed();
        int g = baseColor.getGreen();
        int b = baseColor.getBlue();

        return new Color[] { new Color(b, r, g), new Color(g, b, r) };
    }

    /**
     * converts a hexstring to a Color object
     *
     * @param hex : String : hex string either with or without the # sign
     *
     * @return : Color : color
     */
    public static Color hexToColor(String hex) {
        if (hex.startsWith("#")) { 
            hex = hex.substring(1, hex.length());
        }

        int r = HexString.hexToByte(hex.substring(0, 2));
        int g = HexString.hexToByte(hex.substring(2, 4));
        int b = HexString.hexToByte(hex.substring(4, 6));

        if (r < 0) {
            r += 256;
        }

        if (g < 0) {
            g += 256;
        }

        if (b < 0) {
            b += 256;
        }

        return new Color(r, g, b);
    }

    /**
     * converts HSB (hue/saturation/brightness) to color int value
     *
     * @return : int : color int value
     */
    public static int hsb2RGB(final float... hsb) {
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * converts HSB (hue/saturation/brightness) to color int value
     *
     * @param hue : float : hue (0.0 - 1.0)
     * @param sat : float : saturation (0.0 - 1.0)
     * @param bri : float : brightness/value (0.0 - 1.0)
     *
     * @return : int : color int value
     */
    public static int hsb2RGB(final float hue, final float sat, final float bri) {
        return Color.HSBtoRGB(hue, sat, bri);
    }

    /**
     * converts HSB (hue/saturation/brightness) to color int value
     *
     * @param hue : int : hue (0 - 255)
     * @param sat : int : saturation (0 - 255)
     * @param bri : int : brightness/value (0 - 255)
     *
     * @return : int : color int value
     */
    public static int hsb2RGB(final int hue, final int sat, final int bri) {
        float h = (float) hue / 255;
        float s = (float) sat / 255;
        float b = (float) bri / 255;

        return Color.HSBtoRGB(h, s, b);
    }

    /**
     * converts a Color to HSB values (0-255)
     *
     * @param color : Color : color
     *
     * @return : int[] : HSB values (0-255)
     */
    public static int[] rgb2HSB(final Color color) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        int hue = (int) (hsb[0] * 255);
        int sat = (int) (hsb[1] * 255);
        int bri = (int) (hsb[2] * 255);

        return new int[] { hue, sat, bri };
    }

    /**
     * converts a color int value to HSB values (0-255)
     *
     * @param rgbpixel : color int value
     *
     * @return : int[] : HSB values (0-255)
     */
    public static int[] rgb2HSB(final int rgbpixel) {
        float[] hsb = new float[3];
        ColorModel cm = ColorModel.getRGBdefault();
        Color.RGBtoHSB(cm.getRed(rgbpixel), cm.getGreen(rgbpixel), cm.getBlue(rgbpixel), hsb);

        int hue = (int) (hsb[0] * 255);
        int sat = (int) (hsb[1] * 255);
        int bri = (int) (hsb[2] * 255);

        return new int[] { hue, sat, bri };
    }

    /**
     * converts RGB values (0-255) to HSB values (0-255)
     *
     * @param red : int : red (0-255)
     * @param grn : int : green (0-255)
     * @param blu : int : blue (0-255)
     *
     * @return : int[] : RGB int values (0-255)
     */
    public static int[] rgb2HSB(final int red, final int grn, final int blu) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, grn, blu, hsb);

        int hue = (int) (hsb[0] * 255);
        int sat = (int) (hsb[1] * 255);
        int bri = (int) (hsb[2] * 255);

        return new int[] { hue, sat, bri };
    }

    /**
     * converts a Color to HSB values (0.0 - 1.0)
     *
     * @param color : Color : color
     *
     * @return : float[] : HSB values (0.0 - 1.0)
     */
    public static float[] rgb2HSBFloat(final Color color) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        return hsb;
    }

    /**
     * converts a color int value to HSB values (0.0 - 1.0)
     *
     * @param rgbpixel : int : color int value
     *
     * @return : float[] : HSB values (0.0 - 1.0)
     */
    public static float[] rgb2HSBFloat(final int rgbpixel) {
        float[] hsb = new float[3];
        ColorModel cm = ColorModel.getRGBdefault();
        Color.RGBtoHSB(cm.getRed(rgbpixel), cm.getGreen(rgbpixel), cm.getBlue(rgbpixel), hsb);

        return hsb;
    }

    /**
     * converts a color RGB int values (0 - 255) to HSB values (0.0 - 1.0)
     *
     * @param red : int : red (0-255)
     * @param grn : int : green (0-255)
     * @param blu : int : blue (0-255)
     *
     * @return : float[] : HSB values (0.0 - 1.0)
     */
    public static float[] rgb2HSBFloat(final int red, final int grn, final int blu) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(red, grn, blu, hsb);

        return hsb;
    }

    /**
     * converts a color to cmy floats
     *
     * @param co : Color : color
     *
     * @return : float[] : cmy (cyan/magenta/yellow => printer) components (0.0 - 1.0)
     */
    public static float[] rgbToCmy(final Color co) {
        int r = co.getRed();
        int g = co.getGreen();
        int b = co.getBlue();
        float c = (100.0f - (((r + 1.0f) * 100.0f) / 255.0f)) / 100.0f;
        float m = (100.0f - (((g + 1.0f) * 100.0f) / 255.0f)) / 100.0f;
        float y = (100.0f - (((b + 1.0f) * 100.0f) / 255.0f)) / 100.0f;

        return new float[] { c, m, y };
    }

    /**
     * converts a color to cmy floats
     *
     * @param pixel : int : pixel color integer
     *
     * @return : float[] : cmy (cyan/magenta/yellow => printer) components (0.0 - 1.0)
     */
    public static float[] rgbToCmy(final int pixel) {
        return ColorTools.rgbToCmy(new Color(pixel));
    }

    /**
     * converts a color to cmy floats
     *
     * @param r : int : red (0 - 255)
     * @param g : int : green (0 - 255)
     * @param b : int : blue (0 - 255)
     *
     * @return : float[] : cmy (cyan/magenta/yellow => printer) components (0.0 - 1.0)
     */
    public static float[] rgbToCmy(final int r, final int g, final int b) {
        return ColorTools.rgbToCmy(new Color(r, g, b));
    }

    /**
     * rounds a float
     *
     * @param f float
     * @param d d
     *
     * @return rounded float
     */
    public static float round(float f, int d) {
        int pow = (int) Math.pow(10, d);

        return (float) Math.round(f * pow) / pow;
    }
}
