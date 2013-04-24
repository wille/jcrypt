import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.jar.JarInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;


public class Decrypter {
	
	public static final String ENCRYPTED_ARCHIVE = "/jar.dat";
	public static final String CONFIG_ENTRY = "/c.dat";
	public static final String EXCLUDE = "/excl.dat";
	public static final String DEFAULT_KEY = "111111111111111111111111";

	public static void main(String[] args) throws Exception {
		String[] config = decode(readString(Decrypter.class.getResourceAsStream(CONFIG_ENTRY))).trim().split("\n");
		String[] excludedClasses = decode(readString(Decrypter.class.getResourceAsStream(EXCLUDE))).trim().split("\n");
		
		boolean cryptAll = Boolean.parseBoolean(config[0]);
		String mainClass = config[1];
		String skey = null;
		if (config.length > 2) {
			skey = config[2];
		}
		
		InputStream resource = Decrypter.class.getResourceAsStream(ENCRYPTED_ARCHIVE);
		
		Cipher cipher = Cipher.getInstance("DESede");

		Key key = new SecretKeySpec(skey != null ? skey.getBytes("UTF-8") : DEFAULT_KEY.getBytes("UTF-8"), "DESede");
		cipher.init(Cipher.DECRYPT_MODE, key);
		
		JarInputStream jarInputStream = new JarInputStream(new CipherInputStream(resource, cipher));
		JcryptClassLoader classLoader = new JcryptClassLoader(Decrypter.class.getClassLoader(), jarInputStream, cryptAll, excludedClasses);
		
		Class<?> classToLoad = classLoader.loadClass(mainClass);
		Method method = classToLoad.getMethod("main", new Class[] { String[].class });
		
		method.invoke(classToLoad.newInstance(), new Object[] { args });
		
		jarInputStream.close();
	}
	
	public static String getClassName(String fileName) {
		return fileName.substring(0, fileName.length() - 6).replace('/', '.');
	}
	
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
