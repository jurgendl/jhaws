package org.jhaws.common.io.security;

public class BC {
	static {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	public static boolean provide() {
		return true;
	}
}
