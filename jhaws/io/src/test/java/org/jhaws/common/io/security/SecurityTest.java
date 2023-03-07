package org.jhaws.common.io.security;

import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SecurityTest {
	@Test
	public void list() {
		for (Provider provider : java.security.Security.getProviders()) {
			System.out.println(provider);
			System.out.println("----------");
			for (Provider.Service service : provider.getServices()) {
				System.out.println(service.getType() + " - " + service.getAlgorithm());
			}
			System.out.println("============================================");
		}
	}

	// FIXME @Test
	public void available() {
		try {
			Assert.assertNotNull(Cipher.getInstance("Blowfish"));
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
		try {
			Assert.assertNotNull(SecretKeyFactory.getInstance("Blowfish"));
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
		try {
			Assert.assertNotNull(SecretKeyFactory.getInstance("Blowfish"));
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
	}

	@Test
	public void test_SecureMeAlt() {
		try {
			Security s = new SecureMeAlt("seed".getBytes(), "masterPass");
			String originalPassword = "originalPassword";
			System.out.println("Original password: " + originalPassword);
			byte[] encryptedPassword = s.encrypt(originalPassword);
			System.out.println("Encrypted password: " + new String(encryptedPassword));
			String decryptedPassword = s.decrypt(encryptedPassword);
			System.out.println("Decrypted password: " + decryptedPassword);
			Assert.assertEquals(originalPassword, decryptedPassword);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex);
		}
	}

	// @Test
	// public void test_SecureMeBC() {
	// try {
	// Security s = new org.jhaws.common.io.security.SecureMeBC(new
	// Seed("seed".getBytes()));
	// String originalPassword = "originalPassword";
	// System.out.println("Original password: " + originalPassword);
	// byte[] encryptedPassword = s.encrypt(originalPassword);
	// System.out.println("Encrypted password: " + new String(encryptedPassword));
	// String decryptedPassword = s.decrypt(encryptedPassword);
	// System.out.println("Decrypted password: " + decryptedPassword);
	// Assert.assertEquals(originalPassword, decryptedPassword);
	// } catch (Exception ex) {
	// ex.printStackTrace(System.out);
	// throw new RuntimeException(ex);
	// }
	// }
}
