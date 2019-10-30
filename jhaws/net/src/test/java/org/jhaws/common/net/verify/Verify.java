package org.jhaws.common.net.verify;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.net.client5.HTTPClient;

public class Verify {
    public static void main(String[] args) {
        try (HTTPClient hc = new HTTPClient()) {
            FilePath file = new FilePath("C:\\Users\\jdlandsh\\Desktop\\tomcat-native-1.2.23-openssl-1.1.1c-win32-bin.zip");
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(file.readFully());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            System.out.println(hashtext);
            System.out.println(hc.get(
                    "https://www.apache.org/dist/tomcat/tomcat-connectors/native/1.2.23/binaries/tomcat-native-1.2.23-openssl-1.1.1c-win32-bin.zip.sha512")
                    .getContentString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
