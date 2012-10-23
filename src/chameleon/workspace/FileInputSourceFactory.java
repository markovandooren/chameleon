package chameleon.workspace;

import java.io.File;

import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;

public class FileInputSourceFactory {

	public void initialize(Namespace root) {
		_currentNamespace = root;
	}
	private Namespace _currentNamespace;

	public void pushDirectory(String name) {
		// FIXME make this lazy to avoid creation of namespaces for directories without source files
		//       take intermediate levels into account though.
			_currentNamespace = _currentNamespace.getOrCreateNamespace(name);
	}

	public void popDirectory() {
		_currentNamespace = (Namespace) _currentNamespace.parent();
	}

	protected Namespace currentNamespace() {
		return _currentNamespace;
	}

	public InputSource create(File file, DirectoryLoader loader) throws InputException {
		FileInputSource fileInputSource = doCreateInputSource(file);
		loader.addInputSource(fileInputSource);
//		fileInputSource.loaderLink().lock();
		fileInputSource.setNamespace((InputSourceNamespace) currentNamespace());
		return fileInputSource;
	}

	protected FileInputSource doCreateInputSource(File file) throws InputException {
		return new FileInputSource(file);
	}
}
