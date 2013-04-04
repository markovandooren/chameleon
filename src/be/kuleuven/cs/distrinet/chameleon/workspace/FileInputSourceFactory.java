package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;

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

	public IFileInputSource create(File file, DirectoryLoader loader) throws InputException {
		IFileInputSource fileInputSource = doCreateInputSource(file,loader);
//		loader.addInputSource(fileInputSource);
//		fileInputSource.loaderLink().lock();
		fileInputSource.setNamespace((InputSourceNamespace) currentNamespace());
		return fileInputSource;
	}

	protected IFileInputSource doCreateInputSource(File file, DirectoryLoader loader) throws InputException {
		return new FileInputSource(file,loader);
	}
}
