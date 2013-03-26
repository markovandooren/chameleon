package be.kuleuven.cs.distrinet.chameleon.util;

import java.io.File;
import java.net.URI;

public class FileUtil {

	public static String relativePath(String base, String path) {
		return relativeURI(base, path).getPath();
	}

	protected static URI relativeURI(String base, String path) {
		return new File(base).toURI().relativize(new File(path).toURI());
	}
}
