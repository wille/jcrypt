package com.redpois0n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.redpois0n.crypto.FileCrypter;
import com.redpois0n.crypto.Hex;

public class Build {
	
	public static final String ENCRYPTED_ARCHIVE = "jar.dat";
	public static final String CONFIG_ENTRY = "c.dat";
	public static final String DEFAULT_KEY = "1111111111111111";
	public static final String EXCLUDE = "excl.dat";
	
	public static boolean contains(String[] array, String search) {
		for (String str : array) {
			if (str.equalsIgnoreCase(search)) {
				return true;
			}
		}
		
		return false;
	}

	public static void build(File input, File output, String mainclass, String key, boolean encall, String[] excluded) throws Exception {
		File temp = File.createTempFile("jcrypt", ".jar");
		
		FileCrypter.encrypt(input, temp, key != null ? key : DEFAULT_KEY);
		
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
		
		if (!encall) {
			ZipFile inp = new ZipFile(input);
			
			Enumeration<? extends ZipEntry> entries = inp.entries();
			
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (contains(excluded, entry.getName().replace("/", ".").replace(".class", "")) || !entry.getName().toLowerCase().contains("meta-inf") && !entry.getName().toLowerCase().endsWith(".class")) {
					out.putNextEntry(entry);
					Util.copy(inp.getInputStream(entry), out);
					out.closeEntry();
				}
			}
			
			inp.close();
		}
		
		ZipEntry config = new ZipEntry(CONFIG_ENTRY);	
		out.putNextEntry(config);
		String c = "";
		c += encall + "\n";
		c += mainclass + "\n";	
		
		if (key != null) {
			c += key;
		}
		
		out.write(Hex.encode(c).getBytes("UTF-8"));
		out.closeEntry();
		
		if (excluded.length > 0) {
			ZipEntry excludedEntry = new ZipEntry(EXCLUDE);
			out.putNextEntry(excludedEntry);
			c = "";
			for (String str : excluded) {
				c += str + "\n";
			}
			out.write(Hex.encode(c).getBytes("UTF-8"));
			out.closeEntry();
		}
		
		ZipFile zip = new ZipFile("Bin.jar");
		
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();

			InputStream in = zip.getInputStream(entry);
			out.putNextEntry(entry);
			Util.copy(in, out);
			out.closeEntry();
			in.close();
		}
		
		zip.close();
		
		out.putNextEntry(new ZipEntry(ENCRYPTED_ARCHIVE));
		FileInputStream fis = new FileInputStream(temp);
		Util.copy(fis, out);
		fis.close();
		out.closeEntry();
		
		out.close();
		
		temp.delete();
	}

}
