package com.redpois0n.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class FileCrypter {

	public static void decrypt(File input, File output, String key) throws Exception {
		byte[] buffer = new byte[1024];
		
		InputStream in = new FileInputStream(input);
		OutputStream out = new FileOutputStream(output);

		Cipher dcipher = Cipher.getInstance("DESede");

		dcipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "DESede"));

		in = new CipherInputStream(in, dcipher);

		int numRead = 0;
		while ((numRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, numRead);
		}
		
		in.close();
		out.close();
	}

	public static void encrypt(File input, File output, String key) throws Exception {
		byte[] buffer= new byte[1024];
		
		InputStream in = new FileInputStream(input);
		OutputStream out = new FileOutputStream(output);

		Cipher ecipher = Cipher.getInstance("DESede");

		ecipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "DESede"));

		out = new CipherOutputStream(out, ecipher);

		int numRead = 0;
		while ((numRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, numRead);
		}
		
		in.close();
		out.close();
	}
}
