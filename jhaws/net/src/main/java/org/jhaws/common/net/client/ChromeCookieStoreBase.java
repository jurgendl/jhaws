package org.jhaws.common.net.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.Utils;
import org.jhaws.common.io.Utils.OSGroup;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.SQLiteDataSource;

import com.sun.jna.platform.win32.Crypt32Util;

// https://stackoverflow.com/questions/33629474/reading-and-inserting-chrome-cookies-java
// https://github.com/benjholla/CookieMonster
public class ChromeCookieStoreBase {
    protected NamedParameterJdbcTemplate jdbc;

    public ChromeCookieStoreBase(FilePath... cookieStores) {
        for (FilePath cookieStore : cookieStores) {
            if (cookieStore.fileExists()) {
                SQLiteDataSource dataSource = new SQLiteDataSource();
                FilePath backup = cookieStore.appendExtension("backup");
                try {
                    cookieStore = cookieStore.copyTo(backup);
                } catch( UncheckedIOException ex){
                    cookieStore = cookieStore.copyFileTo(backup);
                }
                String url = "jdbc:sqlite:" + cookieStore.getAbsolutePath();
                System.out.println(url);
                dataSource.setUrl(url);
                jdbc = new NamedParameterJdbcTemplate(dataSource);
                break;
            }
        }
        if(jdbc==null) throw new NullPointerException("jdbc");
    }

    public ChromeCookieStoreBase() {
        this(FilePath.getUserHomeDirectory()//
                .child("AppData")//
                .child("Local")//
                .child("Google")//
                .child("Chrome")//
                .child("User Data")//
                .child("Default")//
                .child("Network")//
                .child("Cookies")//
                , FilePath.getUserHomeDirectory()//
                        .child("AppData")//
                        .child("Local")//
                        .child("Google")//
                        .child("Chrome")//
                        .child("User Data")//
                        .child("Default")//
                        .child("Cookies")//

        );
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
                    byte[] decrypted_value = decrypt(rs.getBytes(i++));
                    Date expires = chromeUtcToDate(rs.getString(i++));
                    boolean is_secure = rs.getBoolean(i++);
                    CookieBase cookie = new CookieBase();
                    cookie.setName(name);
                    cookie.setValue(decrypted_value==null?null:new String(decrypted_value));
                    cookie.setDomain(host_key);
                    cookie.setPath(path);
                    cookie.setExpiryDate(expires);
                    cookie.setSecure(is_secure);
                    return cookie;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        System.out.println("loading chrome cookies");
        return jdbc.query("select host_key, name, path, encrypted_value, expires_utc, is_secure from cookies", params, cookieRowMapper);
    }

    private String chromeKeyringPassword = null;

    private static String getMacKeyringPassword(String application) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "security", "find-generic-password", "-w", "-s", application };
        Process proc = rt.exec(commands);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String result = "";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            result += s;
        }
        return result;
    }

    private byte[] decrypt(byte[] encrypted_value) {
        if (Utils.osgroup == OSGroup.Windows) {
            if(new String(encrypted_value).startsWith("v20")) return null;
            /*{
                byte[] trimmedData = new byte[encrypted_value.length - 4];
                System.arraycopy(encrypted_value, 4, trimmedData, 0, trimmedData.length);
                encrypted_value=trimmedData;
            }*/
            return Crypt32Util.cryptUnprotectData(encrypted_value);
        }

        if (Utils.osgroup == OSGroup.Nix) {
            try {
                byte[] salt = "saltysalt".getBytes(); // TODO inject
                char[] password = "peanuts".toCharArray(); // TODO inject
                char[] iv = new char[16];
                Arrays.fill(iv, ' ');
                int keyLength = 16;
                int iterations = 1;
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
                SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));
                // if cookies are encrypted "v10" is a the prefix (has to be
                // removed before decryption)
                byte[] encryptedBytes = encrypted_value;
                if (new String(encrypted_value).startsWith("v10")) {
                    encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
                }
                return cipher.doFinal(encryptedBytes);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        if (Utils.osgroup == OSGroup.Mac) {
            try {
                // access the decryption password from the keyring manager
                if (chromeKeyringPassword == null) {
                    chromeKeyringPassword = getMacKeyringPassword("Chrome Safe Storage");
                }
                byte[] salt = "saltysalt".getBytes();
                char[] password = chromeKeyringPassword.toCharArray();
                char[] iv = new char[16];
                Arrays.fill(iv, ' ');
                int keyLength = 16;
                int iterations = 1003;
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
                SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));
                // if cookies are encrypted "v10" is a the prefix (has to be
                // removed before decryption)
                byte[] encryptedBytes = encrypted_value;
                if (new String(encrypted_value).startsWith("v10")) {
                    encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
                }
                return cipher.doFinal(encryptedBytes);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        throw new UnsupportedOperationException("" + Utils.osgroup);
    }

    private static Date chromeUtcToDate(String timeString) {
        if ("0".equals(timeString)) return null;
        // chrome: ldap time (if 17 digits add 1 random digit(use 0 out of
        // convenience))
        timeString = timeString.concat("0");
        long timeNoC = Long.parseLong(timeString);
        // source:
        // http://www.epochconverter.com/epoch/ldap-timestamp.php
        // formula 1: LDAP time = (time()+11644473600)*10000000
        // formala 2: LDAP time / 10000000 - 11644473600 = time()
        timeNoC = timeNoC / 10000000L - 11644473600L;
        timeNoC = timeNoC * 1000;
        return new Date(timeNoC);
    }

    public static void main(String[] args) {
       new ChromeCookieStoreBase().getSerializableCookies().forEach(System.out::println);
    }
}
