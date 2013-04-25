package com.redpois0n.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Md5 {
	
	public static String md5(byte[] data) throws Exception {	
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		return Hex.encode(digest.digest(data));
	}
	
	public static String md5(String data) throws Exception {
		return md5(data.getBytes("UTF-8"));
	}
	
	public static String md5(File file) throws Exception {
		byte[] contents = new byte[(int) file.length()];
		FileInputStream in = new FileInputStream(file);
		in.read(contents);
		in.close();
		
		return md5(contents);
	}
}
