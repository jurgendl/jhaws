package org.tools.hqlbuilder.webservice.libraries.prism;

import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// http://prismjs.com/download.html#themes=prism-solarizedlight&languages=markup+css+clike+javascript+abap+actionscript+ada+apacheconf+apl+applescript+c+arff+asciidoc+asm6502+csharp+autohotkey+autoit+bash+basic+batch+bison+brainfuck+bro+cpp+aspnet+arduino+coffeescript+clojure+ruby+csp+css-extras+d+dart+diff+django+docker+eiffel+elixir+elm+markup-templating+erlang+fsharp+flow+fortran+gedcom+gherkin+git+glsl+go+graphql+groovy+haml+handlebars+haskell+haxe+http+hpkp+hsts+ichigojam+icon+inform7+ini+io+j+java+jolie+json+julia+keyman+kotlin+latex+less+liquid+lisp+livescript+lolcode+lua+makefile+markdown+erb+matlab+mel+mizar+monkey+n4js+nasm+nginx+nim+nix+nsis+objectivec+ocaml+opencl+oz+parigp+parser+pascal+perl+php+php-extras+sql+powershell+processing+prolog+properties+protobuf+pug+puppet+pure+python+q+qore+r+jsx+typescript+renpy+reason+rest+rip+roboconf+crystal+rust+sas+sass+scss+scala+scheme+smalltalk+smarty+plsql+soy+stylus+swift+tcl+textile+tt2+twig+tsx+vbnet+velocity+verilog+vhdl+vim+visual-basic+wasm+wiki+xeora+xojo+xquery+yaml&plugins=line-highlight+line-numbers+show-invisibles+autolinker+wpd+custom-class+file-highlight+toolbar+jsonp-highlight+highlight-keywords+remove-initial-line-feed+previewers+autoloader+unescaped-markup+command-line+normalize-whitespace+keep-markup+data-uri-highlight+show-language+copy-to-clipboard
// 1.14.0
public class Prism {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Prism.class, "prism.js");

    public static enum PrismTheme {
        defaulted, solarized_light, dark, funky, okaidia, twilight, coy, tomorrow_night;
    }

    public static CssResourceReference CSS_DEFAULT = new CssResourceReference(Prism.class, "prism-default.css");

    public static CssResourceReference CSS_SOLARIZED_LIGHT = new CssResourceReference(Prism.class, "prism-solarized-light.css");

    public static CssResourceReference CSS_DARK = new CssResourceReference(Prism.class, "prism-dark.css");

    public static CssResourceReference CSS_FUNKY = new CssResourceReference(Prism.class, "prism-funky.css");

    public static CssResourceReference CSS_OKAIDIA = new CssResourceReference(Prism.class, "prism-okaidia.css");

    public static CssResourceReference CSS_TWILIGHT = new CssResourceReference(Prism.class, "prism-twilight.css");

    public static CssResourceReference CSS_COY = new CssResourceReference(Prism.class, "prism-coy.css");

    public static CssResourceReference CSS_TOMORROW_NIGHT = new CssResourceReference(Prism.class, "prism-tomorrow-night.css");

    public static ResourceReference css(PrismTheme theme) {
        if (theme == null) return CSS_DEFAULT;
        switch (theme) {
            case defaulted:
                return CSS_DEFAULT;
            case solarized_light:
                return CSS_SOLARIZED_LIGHT;
            case dark:
                return CSS_DARK;
            case funky:
                return CSS_FUNKY;
            case okaidia:
                return CSS_OKAIDIA;
            case twilight:
                return CSS_TWILIGHT;
            case coy:
                return CSS_COY;
            case tomorrow_night:
                return CSS_TOMORROW_NIGHT;
            default:
                return CSS_DEFAULT;
        }
    }
}
