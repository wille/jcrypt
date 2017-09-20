package com.redpois0n;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class Utils {

	public static ImageIcon getIcon(String name) {
		return new ImageIcon(Main.class.getResource("/com/redpois0n/icons/" + name + ".png"));
	}
	
	public static File showOpenDialog() {
		JFileChooser c = new JFileChooser();
		c.showOpenDialog(null);
		return c.getSelectedFile();
	}
	
	public static File showSaveDialog() {
		JFileChooser c = new JFileChooser();
		c.showSaveDialog(null);
		return c.getSelectedFile();
	}
	
	
	public static void copy(InputStream in, OutputStream out) throws Exception {
		byte[] buf = new byte[1024];
		
		int bytesRead;
		while ((bytesRead = in.read(buf)) != -1) {
			out.write(buf, 0, bytesRead);
		}
	}
	
	public static String randomString(int len) {
		char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		
		String s = "";
		
		for (int i = 0; i < len; i++) {
			s += letters[new Random().nextInt(letters.length)];
		}
		
		return s;
	}
}
