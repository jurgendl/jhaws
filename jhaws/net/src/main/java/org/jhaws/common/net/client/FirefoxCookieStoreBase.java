package org.jhaws.common.net.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jhaws.common.io.FilePath;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.SQLiteDataSource;

public class FirefoxCookieStoreBase {
    protected NamedParameterJdbcTemplate jdbc;

    public FirefoxCookieStoreBase(FilePath cookieStore) {
        if (cookieStore.exists()) {
            SQLiteDataSource dataSource = new SQLiteDataSource();
            cookieStore = cookieStore.copyTo(cookieStore.appendExtension("backup"));
            String url = "jdbc:sqlite:" + cookieStore.getAbsolutePath();
            System.out.println(url);
            dataSource.setUrl(url);
            jdbc = new NamedParameterJdbcTemplate(dataSource);
        } else {
            throw new NullPointerException("jdbc");
        }
    }

    public FirefoxCookieStoreBase() {
        this(FilePath.getUserHomeDirectory().child("AppData\\Roaming\\Mozilla\\Firefox\\Profiles").getChildren().get(0).child("cookies.sqlite"));
    }

    public List<CookieBase> getSerializableCookies() {
        Map<String, Object> params = new HashMap<>();
        RowMapper<CookieBase> cookieRowMapper = new RowMapper<CookieBase>() {
            @Override
            public CookieBase mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    int i = 1;
                    String host_key = rs.getString(i++);
                    String name = rs.getString(i++);
                    String path = rs.getString(i++);
                    byte[] decrypted_value = rs.getBytes(i++);
                    Date expires = new Date(1000l * rs.getInt(i++));
                    boolean secure = rs.getBoolean(i++);
                    CookieBase cookie = new CookieBase();
                    cookie.setName(name);
                    cookie.setValue(new String(decrypted_value));
                    cookie.setDomain(host_key);
                    cookie.setPath(path);
                    cookie.setExpiryDate(expires);
                    cookie.setSecure(secure);
                    return cookie;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        System.out.println("loading firefox cookies");
        return jdbc.query("select host, name, path, value, expiry, isSecure from moz_cookies", params, cookieRowMapper);
    }

    public static void main(String[] args) {
        new FirefoxCookieStoreBase().getSerializableCookies().forEach(System.out::println);
    }
}
