package jcrypt;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.Key;
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

		byte[] iv = new byte[16];
		System.arraycopy(extra, 16, iv, 0, 16);
		
		boolean cryptAll = extra[32] == 1;
		
		byte[] bMainClass = new byte[extra.length - 33];
		System.arraycopy(extra, 33, bMainClass, 0, extra.length - 33);
		String mainClass = new String(bMainClass);

		zip.close();

		InputStream resource = Main.class.getResourceAsStream(ENCRYPTED_ARCHIVE);

		Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");

		Key sks = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

		JarInputStream jarInputStream = new JarInputStream(new CipherInputStream(resource, cipher));
		EncryptedClassLoader classLoader = new EncryptedClassLoader(Main.class.getClassLoader(), jarInputStream, cryptAll);

		Class<?> classToLoad = classLoader.loadClass(mainClass);
		Method method = classToLoad.getMethod("main", new Class[] { String[].class });

		method.invoke(classToLoad.newInstance(), new Object[] { args });

		jarInputStream.close();
	}

}
