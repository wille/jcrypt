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

	private final HashMap<String, byte[]> classes = new HashMap<String, byte[]>();
	private final HashMap<String, byte[]> others = new HashMap<String, byte[]>();
	private final boolean encryptResources;
	private final String[] excludedClasses;

	public EncryptedClassLoader(ClassLoader parent, JarInputStream stream, boolean encryptResources, String[] excludedClasses) {
		super(parent);
		this.loadResources(stream);
		this.encryptResources = encryptResources;
		this.excludedClasses = excludedClasses;
	}

	public InputStream getResourceAsStream(String name) {
		if (encryptResources) {
			byte[] buffer = others.get(name);
			if (buffer != null) {
				return new ByteArrayInputStream(buffer);
			}
		}
		return super.getResourceAsStream(name);
	}

	public URL getResource(String name) {
		if (encryptResources) {
			throw null;
		} else {
			return super.getResource(name);
		}
	}

	protected Enumeration<URL> findResources(String name) throws IOException {
		if (encryptResources) {
			throw new IOException("Cant get URL from resource in memory");
		} else {
			return super.findResources(name);
		}
	}

	public int hashCode() {
		return getParent().hashCode();
	}

	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = getClassData(name);
		
		if (isExcluded(name)) {
			try {
				data = Decrypter.fromInputStream(Decrypter.class.getResourceAsStream("/" + name.replace(".", "/") + ".class"), -1);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return defineClass(name, data, 0, data.length, Decrypter.class.getProtectionDomain());
		} else if (data != null) {
			return defineClass(name, data, 0, data.length, Decrypter.class.getProtectionDomain());
		} else {
			throw new ClassNotFoundException(name);
		}
	}

	public boolean isExcluded(String clazz) {
		for (String str : excludedClasses) {
			if (str.equalsIgnoreCase(clazz)) {
				return true;
			}
		}
		
		return false;
	}

	public void loadResources(JarInputStream stream) {
		byte[] buffer = new byte[1024];

		int count;

		try {
			JarEntry entry = null;
			while ((entry = stream.getNextJarEntry()) != null) {
				int size = (int) entry.getSize();

				ByteArrayOutputStream out = new ByteArrayOutputStream(size == -1 ? 1024 : size);

				while ((count = stream.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();

				byte[] array = out.toByteArray();
				
				if (entry.getName().toLowerCase().endsWith(".class")) {
					classes.put(Decrypter.getClassName(entry.getName()), array);
				} else if (encryptResources) {
					others.put(entry.getName(), array);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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