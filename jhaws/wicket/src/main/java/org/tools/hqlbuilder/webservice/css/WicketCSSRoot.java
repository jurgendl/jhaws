package org.tools.hqlbuilder.webservice.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.tools.hqlbuilder.webservice.jquery.ui.spectrum.Spectrum;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;

import ro.isdc.wro.extensions.processor.support.yui.YuiCssCompressor;

public class WicketCSSRoot {
    // public static CssResourceReference RESET = new CssResourceReference(WicketCSSRoot.class, "meyer.reset.css");

    public static void main(String[] args) {
        WicketCSSRoot.minify(WicketCSSRoot.class, "", new String[] { "meyer.reset", "normalize" });
        WicketCSSRoot.minify(Spectrum.class, "", new String[] { "spectrum" });
        // minify(WeLoveIcons.class, "", new String[] { "weloveiconfonts", "weloveiconfonts-social" });
    }

    protected static void minify(Class<?> root, String path, String[] sources) {
        try {
            String ext = "css";
            for (String source : sources) {
                File file = new File("src/main/resources/" + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source
                        + "." + ext);
                InputStreamReader in = new InputStreamReader(new FileInputStream(file));
                YuiCssCompressor compressor = new YuiCssCompressor(in);
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/"
                        + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source + ".mini." + ext)));
                compressor.compress(out, Integer.MAX_VALUE);
                in.close();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static CssResourceReference NORMALIZE = new CssResourceReference(WicketCSSRoot.class, "normalize.css");

    public static CssResourceReference GENERAL = new CssResourceReference(WicketCSSRoot.class, "general.css");

    public static CssResourceReference CLEARFIX = new CssResourceReference(WicketCSSRoot.class, "clearfix.css");

    /** http://daneden.github.io/animate.css/ */
    public static CssResourceReference ANIMATE = new CssResourceReference(WicketCSSRoot.class, "animate.css");
}
