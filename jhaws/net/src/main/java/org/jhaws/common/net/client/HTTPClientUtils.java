package org.jhaws.common.net.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.utils.URIUtils;

public class HTTPClientUtils extends HTTPClientUtilsBase {
    /**
     * build relative url
     */
    public static String relativeURL(String original, String newRelative) {
        try {
            return URIUtils.resolve(new URI(original), newRelative).toString();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * build relative url
     */
    public static URL relativeURL(URL original, String newRelative) {
        try {
            return URIUtils.resolve(original.toURI(), newRelative).toURL();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * build relative url
     */
    public static URI relativeURL(URI original, String newRelative) {
        return URIUtils.resolve(original, newRelative);
    }
}
