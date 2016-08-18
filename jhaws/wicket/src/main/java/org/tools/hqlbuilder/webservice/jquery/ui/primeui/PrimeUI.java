package org.tools.hqlbuilder.webservice.jquery.ui.primeui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryui.JQueryUI;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryuithemes.JQueryUIThemes;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;
import org.tools.hqlbuilder.webservice.wicket.VirtualPackageResourceReference;

/**
 * @see http://www.primefaces.org/primeui/demo.html
 */
public class PrimeUI {
    public static JavaScriptResourceReference PRIME_UI_JS = new JavaScriptResourceReference(PrimeUI.class, "primeui-1.1-custom.js");

    static {
        try {
            PRIME_UI_JS.addJavaScriptResourceReferenceDependency(JQueryUI.getJQueryUIReference());
        } catch (Exception ex) {
            //
        }
    }

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_CURSOR_POSITION_JS = new JavaScriptResourceReference(PrimeUI.class,
            "plugins/cursorposition.js").addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static JavaScriptResourceReference PRIME_UI_PLUGIN_RANGY_INPUT_JS = new JavaScriptResourceReference(PrimeUI.class, "plugins/rangyinput.js")
            .addJavaScriptResourceReferenceDependency(PRIME_UI_JS);

    public static JavaScriptResourceReference PRIME_UI_FACTORY_JS = new JavaScriptResourceReference(PrimeUI.class, "primeui-factory.js")
            .addJavaScriptResourceReferenceDependency(PRIME_UI_JS).addJavaScriptResourceReferenceDependency(JQueryUI.JQUERY_UI_FACTORY_JS);

    public static CssResourceReference PRIME_UI_CSS = new CssResourceReference(PrimeUI.class, "primeui-1.1.css");

    public static CssResourceReference forJQueryUITheme(String name) {
        return new CssResourceReference(PrimeUI.class, "themes/" + name.toString() + "/theme.css").addCssResourceReferenceDependency(PRIME_UI_CSS);
    }

    protected static List<String> themes = null;

    public static List<String> getThemes() {
        if (themes == null) {
            themes = new ArrayList<String>();
            try {
                String name = PrimeUI.class.getPackage().getName().replace('\\', '/').replace('.', '/') + "/themes/themes.list";
                BufferedReader in = new BufferedReader(new InputStreamReader(JQueryUIThemes.class.getClassLoader().getResourceAsStream(name)));
                String theme;
                while ((theme = in.readLine()) != null) {
                    themes.add(theme);
                }
                in.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return themes;
    }

    public static void mountImages(WebApplication webApplication) {
        String[] images = { "password-meter.png", "messages.png", "loading.gif" };
        for (String image : images) {
            String relPath = "images/" + image;
            PackageResourceReference reference = new VirtualPackageResourceReference(PrimeUI.class, relPath, PrimeUI.class, relPath);
            webApplication.getSharedResources().add("wicket/" + relPath, reference.getResource());
            webApplication.mountResource("wicket/" + relPath, reference);
        }
    }

    public static final String puiinputtext = "puiinputtext";

    public static final String puiinputtextarea = "puiinputtextarea";

    public static final String puidropdown = "puidropdown";

    public static final String puiaccordion = "puiaccordion";

    public static final String puipassword = "puipassword";

    public static final String puispinner = "puispinner";

    public static final String puilistbox = "puilistbox";

    public static final String puiautocomplete = "puiautocomplete";

    public static final String puibreadcrumb = "puibreadcrumb";

    public static final String puibutton = "puibutton";

    public static final String puicheckbox = "puicheckbox";

    public static final String puifieldset = "puifieldset";

    public static final String puigalleria = "puigalleria";

    public static final String puilightbox = "puilightbox";

    public static final String puimenu = "puimenu";

    public static final String puimenubar = "puimenubar";

    public static final String puicontextmenu = "puicontextmenu";

    public static final String puislidemenu = "puislidemenu";

    public static final String puitieredmenu = "puitieredmenu";

    public static final String puipaginator = "puipaginator";

    public static final String puipanel = "puipanel";

    public static final String puipicklist = "puipicklist";

    public static final String puiprogressbar = "puiprogressbar";

    public static final String puiradiobutton = "puiradiobutton";

    public static final String puisticky = "puisticky";

    public static final String puitabview = "puitabview";

    public static final String puigrowl = "puigrowl";

    public static final String puinotifytop = "puinotifytop";

    public static final String puinotifybottom = "puinotifybottom";

    public static final String puidropdownfiltered = "puidropdownfiltered";
}
