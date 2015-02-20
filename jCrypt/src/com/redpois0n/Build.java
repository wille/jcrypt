package com.redpois0n;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Build {
	
	public static final String ENCRYPTED_ARCHIVE = "jar.dat";
	
	public static boolean contains(String[] array, String search) {
		for (String str : array) {
			if (str.equalsIgnoreCase(search)) {
				return true;
			}
		}
		
		return false;
	}

	public static void build(File input, File output, String mainclass, byte[] key, boolean encall) throws Exception {				
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
		
		if (!encall) {
			ZipFile inp = new ZipFile(input);
			
			Enumeration<? extends ZipEntry> entries = inp.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (!entry.getName().toLowerCase().contains("meta-inf") && !entry.getName().toLowerCase().endsWith(".class")) {
					out.putNextEntry(entry);
					Utils.copy(inp.getInputStream(entry), out);
					out.closeEntry();
				}
			}
			
			inp.close();
		}
		
		ZipFile zip = new ZipFile("Bin.jar");	
		Enumeration<? extends ZipEntry> entries = zip.entries();	
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();

			InputStream in = zip.getInputStream(entry);
			out.putNextEntry(entry);
			Utils.copy(in, out);
			out.closeEntry();
			in.close();
		}
		zip.close();
				
		byte[] iv = new byte[16];
		new Random().nextBytes(iv);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
		
		FileInputStream is = new FileInputStream(input);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CipherOutputStream cos = new CipherOutputStream(baos, cipher);
		
		Utils.copy(is, cos);
		
		is.close();
		cos.close();
		
		byte[] bMainClass = mainclass.getBytes("UTF-8");
		
		byte[] config = new byte[key.length + iv.length + 1 + bMainClass.length];
		System.arraycopy(key, 0, config, 0, key.length);
		System.arraycopy(iv, 0, config, 16, iv.length);
		config[32] = (byte) (encall ? 1 : 0);
		System.arraycopy(bMainClass, 0, config, 33, bMainClass.length);
		
		ZipEntry entry = new ZipEntry(ENCRYPTED_ARCHIVE);
		out.putNextEntry(entry);
		entry.setExtra(config);
		out.write(baos.toByteArray());
		out.closeEntry();	
		
		out.close();	
	}

}
