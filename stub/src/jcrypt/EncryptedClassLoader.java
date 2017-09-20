package jcrypt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class EncryptedClassLoader extends ClassLoader {


    /**
     * Binary storage for each class loaded
     */
	private final HashMap<String, byte[]> classes = new HashMap<String, byte[]>();

    /**
     * Binary storage for each resource loaded
     */
	private final HashMap<String, byte[]> others = new HashMap<String, byte[]>();
	private final boolean encryptResources;

	public EncryptedClassLoader(ClassLoader parent, JarInputStream stream, boolean encryptResources) {
		super(parent);
		this.loadResources(stream);
		this.encryptResources = encryptResources;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
	    // If all resources are encrypted, retrieve it from memory cache
		if (encryptResources) {
			byte[] buffer = others.get(name);
			if (buffer != null) {
				return new ByteArrayInputStream(buffer);
			}
		}

		// If resources are not encrypted, retrieve it normally from the current JAR
		return super.getResourceAsStream(name);
	}

	@Override
	public URL getResource(String name) {
		if (encryptResources) {
			throw null;	// Cannot get URL for encrypted resource
        } else {
			return super.getResource(name);
		}
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (encryptResources) {
			throw new IOException("Cant get URL from resource in memory"); // Cannot get URL for encrypted resource
		} else {
			return super.findResources(name);
		}
	}

	@Override
	public int hashCode() {
		return getParent().hashCode();
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = getClassData(name);
		
		if (data != null) {
			return defineClass(name, data, 0, data.length, Main.class.getProtectionDomain());
		} else {
			throw new ClassNotFoundException(name);
		}
	}

    /**
     *  Iterate the JarInputStream (that points to current JAR)
     *  and load the resources and classes into memory
     */
	public void loadResources(JarInputStream stream) {
		byte[] buffer = new byte[1024];

		int count;

		try {
			JarEntry entry = null;
			while ((entry = stream.getNextJarEntry()) != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				while ((count = stream.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();

				byte[] array = out.toByteArray();
				
				if (entry.getName().toLowerCase().endsWith(".class")) {
					classes.put(Utils.getClassName(entry.getName()), array);
				} else if (encryptResources) {
					others.put(entry.getName(), array);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EncryptedClassLoader) {
			return ((EncryptedClassLoader) o).getParent() == getParent();
		}
		return false;
	}

	public byte[] getClassData(String name) {
		byte[] b = classes.get(name);
		classes.remove(name);
		return b;
	}

}