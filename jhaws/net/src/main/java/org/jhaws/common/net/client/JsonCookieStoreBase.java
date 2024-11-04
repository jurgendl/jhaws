package org.jhaws.common.net.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.resteasy.CustomObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//https://chromewebstore.google.com/detail/get-cookiestxt-locally/cclelndahbckbenkjhflpdbgdldlbecc?pli=1
public class JsonCookieStoreBase {
    static CustomObjectMapper om = new CustomObjectMapper();

    FilePath[] cookieStores;
    
    public JsonCookieStoreBase(FilePath... cookieStores) {
        this.cookieStores = cookieStores;
    }

    static public class JsonCookie {
        String domain;
        Double expirationDate;
        Boolean hostOnly;
        Boolean httpOnly;
        String name;
        String path;
        String sameSite;
        Boolean secure;
        Boolean session;
        String value;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public Double getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Double expirationDate) {
            this.expirationDate = expirationDate;
        }

        public Boolean getHostOnly() {
            return hostOnly;
        }

        public void setHostOnly(Boolean hostOnly) {
            this.hostOnly = hostOnly;
        }

        public Boolean getHttpOnly() {
            return httpOnly;
        }

        public void setHttpOnly(Boolean httpOnly) {
            this.httpOnly = httpOnly;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSameSite() {
            return sameSite;
        }

        public void setSameSite(String sameSite) {
            this.sameSite = sameSite;
        }

        public Boolean getSecure() {
            return secure;
        }

        public void setSecure(Boolean secure) {
            this.secure = secure;
        }

        public Boolean getSession() {
            return session;
        }

        public void setSession(Boolean session) {
            this.session = session;
        }


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "JsonCookie{" +
                    "domain=" + domain +
                    ", expirationDate=" + expirationDate +
                    ", hostOnly=" + hostOnly +
                    ", httpOnly=" + httpOnly +
                    ", name=" + name +
                    ", path=" + path +
                    ", sameSite=" + sameSite +
                    ", secure=" + secure +
                    ", session=" + session +
                    ", value=" + value +
                    '}';
        }
    }

    public List<CookieBase> getSerializableCookies() {
        return Arrays.stream(cookieStores).flatMap(cookieStore -> {
            try {
                return om.readValue(cookieStore.newInputStream(), new TypeReference<List<JsonCookie>>() {
                }).stream().map(jsonCookie -> {
                    CookieBase cookie = new CookieBase();
                    cookie.setDomain(jsonCookie.getDomain());
                    cookie.setName(jsonCookie.getName());
                    cookie.setPath(jsonCookie.getPath());
                    cookie.setSecure(jsonCookie.getSecure());
                    if (jsonCookie.getExpirationDate() != null) {
                        cookie.setExpiryDate(new Date((long) (jsonCookie.getExpirationDate() * 1000)));
                    }
                    return cookie;
                });
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).toList();
    }
}
