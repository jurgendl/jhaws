package org.jhaws.common.web.wicket;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.csp.CSPHeaderConfiguration;
import org.apache.wicket.csp.ContentSecurityPolicySettings;
import org.apache.wicket.csp.FixedCSPValue;

// in org.apache.wicket.protocol.http.WebApplication
// public void csp(boolean _enableCSP) {
// if (_enableCSP) {
// ContentSecurityPolicySettings cspSettings = getCspSettings();
// CSPHeaderConfiguration cfg = cspSettings.blocking().clear();
// csp(cfg); // configure cuostom
// cspSettings.enforce(this);
// } else {
// getCspSettings().blocking().disabled();
// }
// }
public class CSP {
    public static final String CSPDirectiveSrcValue_DATA = "data:";

    public static final String CSPDirectiveSrcValue_UNSAFE_HASHES = "'unsafe-hashes'";

    public static String getNonce(org.apache.wicket.Component component) {
        return org.apache.wicket.protocol.http.WebApplication.get().getCspSettings().getNonce(component.getRequestCycle());
    }

    public static ContentSecurityPolicySettings setupDisable(org.apache.wicket.protocol.http.WebApplication app) {
        ContentSecurityPolicySettings cspSettings = app.getCspSettings();
        cspSettings.blocking().disabled();
        return cspSettings;
    }

    public static ContentSecurityPolicySettings setupStrict(org.apache.wicket.protocol.http.WebApplication app) {
        ContentSecurityPolicySettings cspSettings = app.getCspSettings();
        cspSettings.blocking().strict();
        return cspSettings;
    }

    // opmerking inline in script en css tag in pagina is niet beschermd omdat er te veel instanties van dit zijn
    // en het zijn zeker de javascript files die het gevaarlijkst zijn
    public static ContentSecurityPolicySettings setupCustom(org.apache.wicket.protocol.http.WebApplication app) {
        ContentSecurityPolicySettings cspSettings = app.getCspSettings();

        CSPHeaderConfiguration _CSPHeaderConfiguration = cspSettings.blocking().clear();
        _CSPHeaderConfiguration//
                // ======================================================================
                // .add(CSPDirective.DEFAULT_SRC, CSPDirectiveSrcValue.SELF)//
                //
                // disabled: .add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.SELF, CSPDirectiveSrcValue.UNSAFE_INLINE,
                // CSPDirectiveSrcValue.UNSAFE_EVAL)//
                // strict: .add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.STRICT_DYNAMIC, CSPDirectiveSrcValue.NONCE)
                // .add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.STRICT_DYNAMIC, CSPDirectiveSrcValue.UNSAFE_INLINE, CSPDirectiveSrcValue.NONCE)
                .add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.SELF, /* CSPDirectiveSrcValue.UNSAFE_INLINE, */
                        CSPDirectiveSrcValue.UNSAFE_EVAL, //
                        CSPDirectiveSrcValue.NONCE)
                //
                .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF, CSPDirectiveSrcValue.UNSAFE_INLINE)//
                // .add(CSPDirective.IMG_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.CONNECT_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.FONT_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.MANIFEST_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.CHILD_SRC, CSPDirectiveSrcValue.SELF)//
                .add(CSPDirective.BASE_URI, CSPDirectiveSrcValue.SELF)//
        // ======================================================================
        // .add(CSPDirective.IMG_SRC, CSPDirectiveSrcValue_DATA)//
        // ======================================================================
        // ======================================================================
        // ======================================================================
        // browser genereert hash van script en zolang dit niet verandert kan dit zo opgegeven worden
        // maar omdat bij een groeiend aantal scripts de lijst bij setup ook groeit
        // want kan dit wel bij paginas apart ingesteld worden om de lijst te beperken?
        // ook bij dynamische scripts moet deze hash telkens herberekend worden
        //
        // dus we gebruiken de NonceInlineScript beter
        // add(new NonceInlineScript("resize_device_check"));
        // <script wicket:id="resize_device_check">
        //
        // of maken er een aparte javascript js file en doen het volgende
        // JavaScriptResourceReferenceWithDependencies JS = new JavaScriptResourceReferenceWithDependencies(getClass(),"...js")
        // zetten het in void renderHead(IHeaderResponse response)
        // response.render(JavaScriptHeaderItem.forReference(JS));
        //
        // dit onderstaand lijntje is bv voor id="resize_device_check" in NieuweHuisstijlPage
        // .add(CSPDirective.SCRIPT_SRC, "'sha256-HPHxp63qHihi+msNPkzdlsKgvLnVFQ5pgRwexy7KOxM='")//
        ;

        // of ...
        // script-src 'unsafe-hashes' 'sha256-...='
        // ======================================================================
        unsafeHashes(_CSPHeaderConfiguration);

        // blijkbaar moet dit nog specifiek opgeroepen worden
        cspSettings.enforce(app);

        return cspSettings;
    }

    public static void unsafeHashes(CSPHeaderConfiguration _CSPHeaderConfiguration) {
        // https://report-uri.com/home/hash
        _CSPHeaderConfiguration.add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue_UNSAFE_HASHES);
        _CSPHeaderConfiguration.add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue_EMPTYJS);
        {
            // be.ugent.komodo.web.wicket.components.NonAjaxBusyIndicator.animeerWanneerGeklikt(Component)
            // if(!$(this).hasClass('disabled')){$(this).addClass('is-loading')};
            // _CSPHeaderConfiguration.add(CSPDirective.SCRIPT_SRC, "'sha256-...='");
            // _CSPHeaderConfiguration.add(CSPDirective.SCRIPT_SRC, cspSha256(NonAjaxBusyIndicator.SCRIPT));
        }
    }

    /* CSPHeaderConfiguration.add(CSPDirective.CHILD_SRC, CSPDirectiveSrcValue_BLOB), blob heeft erachter normaal gezien ook het domein "blob:https://host" */
    public static final FixedCSPValue CSPDirectiveSrcValue_BLOB = new FixedCSPValue("blob:") {
        @Override
        public void checkValidityForSrc() {
            /**/
        }
    };

    public static final String EMPTYJS = "javascript:;";

    public static final String CSPDirectiveSrcValue_EMPTYJS = CSP.cspSha256(EMPTYJS);

    public static String cspSha256(String script) {
        return "'" + "sha256-" + base64(sha256(script)) + "'";
    }

    public static MessageDigest sha256 = null;

    public static synchronized byte[] sha256(String originalString) {
        try {
            if (sha256 == null) sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(originalString.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Encoder base64 = null;

    public static synchronized String base64(byte[] hash) {
        try {
            if (base64 == null) base64 = Base64.getEncoder();
            return base64.encodeToString(hash);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
