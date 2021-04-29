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
import org.jhaws.common.web.wicket.forms.bootstrap.TextFieldPanel;

public class CSP {
	// https://stackoverflow.com/questions/28467789/content-security-policy-object-src-blob
	public static final FixedCSPValue CSPDirectiveSrcValue_BLOB = new FixedCSPValue("blob:") {
		@Override
		public void checkValidityForSrc() {
			/**/
		}
	};

	public static final String CSPDirectiveSrcValue_UNSAFE_HASHES = "'unsafe-hashes'";

	public static void csp(org.apache.wicket.protocol.http.WebApplication app, boolean enableCSP) {
		if (enableCSP) {
			ContentSecurityPolicySettings cspSettings = app.getCspSettings();
			CSPHeaderConfiguration cfg = cspSettings.blocking().clear();
			csp(cfg);
			cspSettings.enforce(app);
		} else {
			app.getCspSettings().blocking().disabled();
		}
	}

	public static void csp(CSPHeaderConfiguration cfg) {
		cfg//
			// ======================================================================
			// .add(CSPDirective.DEFAULT_SRC, CSPDirectiveSrcValue.SELF)//
			//
			// disabled: .add(CSPDirective.SCRIPT_SRC,
			// CSPDirectiveSrcValue.SELF,
			// CSPDirectiveSrcValue.UNSAFE_INLINE,
			// CSPDirectiveSrcValue.UNSAFE_EVAL)//
			// strict: .add(CSPDirective.SCRIPT_SRC,
			// CSPDirectiveSrcValue.STRICT_DYNAMIC,
			// CSPDirectiveSrcValue.NONCE)
			// .add(CSPDirective.SCRIPT_SRC,
			// CSPDirectiveSrcValue.STRICT_DYNAMIC,
			// CSPDirectiveSrcValue.UNSAFE_INLINE,
			// CSPDirectiveSrcValue.NONCE)
				.add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue.SELF, /*
																			 * CSPDirectiveSrcValue
																			 * .
																			 * UNSAFE_INLINE,
																			 *///
						CSPDirectiveSrcValue.UNSAFE_EVAL, //
						CSPDirectiveSrcValue.NONCE)
				//
				.add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF, CSPDirectiveSrcValue.UNSAFE_INLINE)//
				// .add(CSPDirective.IMG_SRC, CSPDirectiveSrcValue.SELF)//
				.add(CSPDirective.CONNECT_SRC, CSPDirectiveSrcValue.SELF)//
				.add(CSPDirective.FONT_SRC, CSPDirectiveSrcValue.SELF)//
				.add(CSPDirective.MANIFEST_SRC, CSPDirectiveSrcValue.SELF)//
				.add(CSPDirective.CHILD_SRC, CSPDirectiveSrcValue.SELF)//
				.add(CSPDirective.CHILD_SRC, CSPDirectiveSrcValue_BLOB)//
				.add(CSPDirective.BASE_URI, CSPDirectiveSrcValue.SELF)//
				// ======================================================================
				// .add(CSPDirective.IMG_SRC, "data:")//
				// ======================================================================
				// // Google tracking
				// (be.ugent.gismo.researchweb.wicket.TrackerConfig)
				.add(CSPDirective.SCRIPT_SRC, "https://www.googletagmanager.com")//
				.add(CSPDirective.SCRIPT_SRC, "https://www.google-analytics.com")//
				.add(CSPDirective.STYLE_SRC, "https://www.googletagmanager.com")//
				.add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com")//
				// .add(CSPDirective.IMG_SRC, "https://ssl.gstatic.com")//
				// .add(CSPDirective.IMG_SRC, "https://www.gstatic.com")//
				// .add(CSPDirective.IMG_SRC,
				// "https://www.google-analytics.com")//
				.add(CSPDirective.FONT_SRC, "https://fonts.gstatic.com")//
				.add(CSPDirective.FONT_SRC, "data:")//
				.add(CSPDirective.CONNECT_SRC, "https://www.google-analytics.com/")//
				// //
				// ======================================================================
				// // Google Recaptcha (overlaps with Google tracking)
				.add(CSPDirective.SCRIPT_SRC, "https://www.google.com")//
				.add(CSPDirective.SCRIPT_SRC, "https://www.gstatic.com")//
				.add(CSPDirective.FRAME_SRC, "https://www.google.com")//
		;

		cfg.add(CSPDirective.SCRIPT_SRC, CSPDirectiveSrcValue_UNSAFE_HASHES);
		try {
			cfg.add(CSPDirective.SCRIPT_SRC, cspSha256(TextFieldPanel.ONCLICK));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			cfg.add(CSPDirective.SCRIPT_SRC, cspSha256(TextFieldPanel.ONDROP));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			cfg.add(CSPDirective.SCRIPT_SRC, cspSha256("topFunction()"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String cspSha256(String script) {
		return "'" + "sha256-" + base64(sha256(script)) + "'";
	}

	public static MessageDigest sha256 = null;

	public static synchronized byte[] sha256(String originalString) {
		try {
			if (sha256 == null)
				sha256 = MessageDigest.getInstance("SHA-256");
			return sha256.digest(originalString.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Encoder base64 = null;

	public static synchronized String base64(byte[] hash) {
		try {
			if (base64 == null)
				base64 = Base64.getEncoder();
			return base64.encodeToString(hash);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
