package jcrypt;

import java.io.File;

public class Utils {

	public static File getJarFile() {
		return new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("file:", ""));
	}

}
