package org.jhaws.common.web.wicket;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.wicket.csp.FixedCSPValue;

public class CSP {
	// https://stackoverflow.com/questions/28467789/content-security-policy-object-src-blob
	public static final FixedCSPValue CSPDirectiveSrcValue_BLOB = new FixedCSPValue("blob:") {
		@Override
		public void checkValidityForSrc() {
			/**/
		}
	};

	public static final String CSPDirectiveSrcValue_UNSAFE_HASHES = "'unsafe-hashes'";

	public static final String EMPTYJS = "javascript:;";

	public static final String CSPDirectiveSrcValue_EMPTYJS = CSP.cspSha256(EMPTYJS);

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
