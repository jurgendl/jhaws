package org.tools.hqlbuilder.webservice.js;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.tools.hqlbuilder.webservice.jquery.ui.jquery.JQuery;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

import ro.isdc.wro.extensions.processor.js.UglifyJsProcessor;

public class WicketJSRoot {
    public static void main(String[] args) {
        WicketJSRoot.minify(WicketJSRoot.class, "", new String[] { "general.js" });
    }

    protected static void minify(Class<?> root, String path, String[] sources) {
        try {
            String ext = "js";
            for (String source : sources) {
                File css = new File(
                        "src/main/resources/" + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source + "." + ext);
                try (InputStreamReader in = new InputStreamReader(new FileInputStream(css));
                        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/"
                                + root.getPackage().getName().replace('\\', '/').replace('.', '/') + path + "/" + source + ".mini." + ext)))) {
                    UglifyJsProcessor compressor = new UglifyJsProcessor();
                    compressor.process(in, out);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JavaScriptResourceReference GENERAL = new JavaScriptResourceReference(WicketJSRoot.class, "general.js");

    static {
        try {
            GENERAL.addJavaScriptResourceReferenceDependency(JQuery.getJQueryReference());
        } catch (Exception ex) {
            //
        }
    }
}
