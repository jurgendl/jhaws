package org.jhaws.common.net.client;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//https://curl.se/rfc/cookie_spec.html
public class NetscapeTxtCookieStoreBase {
    FilePath[] cookieStores;

    public NetscapeTxtCookieStoreBase(FilePath... cookieStores) {
        this.cookieStores = cookieStores;
    }

    public List<CookieBase> getSerializableCookies() {
        return Arrays.stream(cookieStores).flatMap(cookieStore -> {
            String line;
            List<CookieBase> cookies = new ArrayList<>();
            try (BufferedReader reader = cookieStore.newBufferedReader()) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) continue;
                    if (StringUtils.isBlank(line)) continue;
                    String[] parts = line.split("\t");
                    CookieBase c = new CookieBase();
                    c.setDomain(parts[0]);
                    //(parts[1]);
                    c.setPath(parts[2]);
                    //(parts[3]);
                    c.setExpiryDate(new Date(Long.parseLong(parts[4]) * 1000));
                    c.setName(parts[5]);
                    if (parts.length > 6)
                        c.setValue(parts[6]);
                    cookies.add(c);
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return cookies.stream();
        }).toList();
    }
}
