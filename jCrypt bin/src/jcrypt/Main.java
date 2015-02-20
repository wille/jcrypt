package jcrypt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.Enumeration;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main {

	public static final String ENCRYPTED_ARCHIVE = "/jar.dat";

	public static void main(String[] args) throws Exception {
		ZipFile zip = new ZipFile(Utils.getJarFile());

		ZipEntry e = zip.getEntry("jar.dat");
		byte[] extra = e.getExtra();

		byte[] key = new byte[16];
		System.arraycopy(extra, 0, key, 0, 16);
		boolean cryptAll = extra[16] == 1;
		byte[] bMainClass = new byte[extra.length - 17];
		System.arraycopy(extra, 17, bMainClass, 0, extra.length - 17);
		String mainClass = new String(bMainClass);

		zip.close();

		InputStream resource = Main.class.getResourceAsStream(ENCRYPTED_ARCHIVE);

		Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");

		Key sks = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, }));

		JarInputStream jarInputStream = new JarInputStream(new CipherInputStream(resource, cipher));
		EncryptedClassLoader classLoader = new EncryptedClassLoader(Main.class.getClassLoader(), jarInputStream, cryptAll);

		Class<?> classToLoad = classLoader.loadClass(mainClass);
		Method method = classToLoad.getMethod("main", new Class[] { String[].class });

		method.invoke(classToLoad.newInstance(), new Object[] { args });

		jarInputStream.close();
	}

	public static String getClassName(String fileName) {
		return fileName.substring(0, fileName.length() - 6).replace('/', '.');
	}

	/**
	 * Reads string from input stream
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String readString(InputStream is) throws Exception {
		char[] buf = new char[1024];
		Reader r = new InputStreamReader(is, "UTF-8");
		StringBuilder s = new StringBuilder();

		int n;
		while ((n = r.read(buf)) != -1) {
			s.append(buf, 0, n);
		}
		return s.toString();
	}

	/**
	 * Apache common codec
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static String decode(String s) throws Exception {
		char[] data = s.toCharArray();
		int len = data.length;

		if ((len & 0x1) != 0) {
			throw new NumberFormatException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		int i = 0;
		for (int j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = ((byte) (f & 0xFF));
		}

		return new String(out);
	}

	/**
	 * Apache commons codec
	 * 
	 * @param ch
	 * @param index
	 * @return
	 * @throws Exception
	 */
	private static int toDigit(char ch, int index) throws Exception {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	public static byte[] fromInputStream(InputStream stream, int size) throws Exception {
		byte[] buffer = new byte[1024];
		int count = 0;

		ByteArrayOutputStream out = new ByteArrayOutputStream(size == -1 ? 1024 : size);

		while ((count = stream.read(buffer)) != -1) {
			out.write(buffer, 0, count);
		}

		out.close();

		return out.toByteArray();
	}

}
