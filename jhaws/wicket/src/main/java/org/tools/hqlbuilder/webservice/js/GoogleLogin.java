package org.tools.hqlbuilder.webservice.js;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.tools.hqlbuilder.webservice.wicket.CachingUrlResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

// https://console.developers.google.com/project/
// https://developers.google.com/identity/sign-in/web/sign-in
// https://developers.google.com/identity/sign-in/web/backend-auth
// https://developers.google.com/identity/sign-in/web/devconsole-project
public class GoogleLogin {
    public static final String VALIDATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";

    public static CachingUrlResourceReference JS_PLATFORM = new CachingUrlResourceReference(URI.create("http://apis.google.com/js/platform.js"),
            "google-platform");

    public static void init(WicketApplication app) {
		try {
			// load application key
			Properties p = new Properties();
			try (FileInputStream inStream = new FileInputStream(
					System.getProperty("user.home") + "/google.app.client_id.properties")) {
				p.load(inStream);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			app.setGoogleSigninClientId(p.getProperty("google.app.client_id"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
}
