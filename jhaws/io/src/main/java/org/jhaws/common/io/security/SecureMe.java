package org.jhaws.common.io.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

/**
 * security using BouncyCastle
 */
public class SecureMe implements Security {
	static {
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private Seed key;

	public SecureMe() {
		this.key = new Seed();
	}

	/**
	 * @see org.jhaws.common.io.security.Security#decrypt(byte[])
	 */
	@Override
	public String decrypt(byte[] encrypted) throws DataLengthException, IllegalStateException, InvalidCipherTextException, IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(encrypted);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.decrypt(in, out);

		return new String(out.toByteArray());
	}

	/**
	 * This method performs all the decryption and writes the plain text to the buffered output stream created previously.
	 */
	public void decrypt(InputStream in, OutputStream out) throws IOException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));

		// initialise the cipher for decryption
		cipher.init(false, new KeyParameter(this.key.key));

		/*
		 * As the decryption is from our preformatted file, and we know that it's a hex encoded format, then we wrap the InputStream with a BufferedReader so that we can read it
		 * easily.
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		/*
		 * now, read the file, and output the chunks
		 */
		int outL;
		byte[] inblock = null;
		byte[] outblock = null;
		String rv = null;

		while ((rv = br.readLine()) != null) {
			inblock = Hex.decode(rv);
			outblock = new byte[cipher.getOutputSize(inblock.length)];

			outL = cipher.processBytes(inblock, 0, inblock.length, outblock, 0);

			/*
			 * Before we write anything out, we need to make sure that we've got something to write out.
			 */
			if (outL > 0) {
				out.write(outblock, 0, outL);
			}
		}

		/*
		 * Now, process the bytes that are still buffered within the cipher.
		 */
		outL = cipher.doFinal(outblock, 0);

		if (outL > 0) {
			out.write(outblock, 0, outL);
		}
	}

	/**
	 * This method performs all the encryption and writes the cipher text to the buffered output stream created previously.
	 */
	public void encrypt(InputStream in, OutputStream out) throws IOException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));

		// initialise the cipher with the key bytes, for encryption
		cipher.init(true, new KeyParameter(this.key.key));

		/*
		 * Create some temporary byte arrays for use in encryption, make them a reasonable size so that we don't spend forever reading small chunks from a file.
		 * 
		 * There is no particular reason for using getBlockSize() to determine the size of the input chunk. It just was a convenient number for the example.
		 */

		// int inBlockSize = cipher.getBlockSize() * 5;
		int inBlockSize = 47;
		int outBlockSize = cipher.getOutputSize(inBlockSize);

		byte[] inblock = new byte[inBlockSize];
		byte[] outblock = new byte[outBlockSize];

		/*
		 * now, read the file, and output the chunks
		 */
		int inL;
		int outL;
		byte[] rv = null;

		while ((inL = in.read(inblock, 0, inBlockSize)) > 0) {
			outL = cipher.processBytes(inblock, 0, inL, outblock, 0);

			/*
			 * Before we write anything out, we need to make sure that we've got something to write out.
			 */
			if (outL > 0) {
				rv = Hex.encode(outblock, 0, outL);
				out.write(rv, 0, rv.length);
				out.write('\n');
			}
		}

		/*
		 * Now, process the bytes that are still buffered within the cipher.
		 */
		outL = cipher.doFinal(outblock, 0);

		if (outL > 0) {
			rv = Hex.encode(outblock, 0, outL);
			out.write(rv, 0, rv.length);
			out.write('\n');
		}
	}

	/**
	 * @see org.jhaws.common.io.security.Security#encrypt(java.lang.String)
	 */
	@Override
	public byte[] encrypt(String string) throws IllegalStateException, InvalidCipherTextException, IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(string.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		this.encrypt(in, out);
		return out.toByteArray();
	}
}
