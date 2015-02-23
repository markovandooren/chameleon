package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;

public class FileInputSourceFactory {

	public void initialize(Namespace root) {
//		_currentNamespace = root;
		_root = root;
		_current = _root;
		_namespaceNameStack = new LinkedList<>();
	}
	
	private Namespace _root;
//	private Namespace _currentNamespace;
	private LinkedList<String> _namespaceNameStack = new LinkedList<>();

	public void pushDirectory(String name) {
		// FIXME make this lazy to avoid creation of namespaces for directories without source files
		//       take intermediate levels into account though.
//			_currentNamespace = _currentNamespace.getOrCreateNamespace(name);
		_namespaceNameStack.addLast(name);
		_current = null;
	}

	public void popDirectory() {
//		_currentNamespace = (Namespace) _currentNamespace.parent();
		_namespaceNameStack.removeLast();
		if(_current != null) {
			_current = (Namespace) _current.parent();
		}
	}

	private Namespace _current;
	
	protected Namespace currentNamespace() {
		if(_current == null) {
			StringBuilder builder = new StringBuilder();
			Iterator<String> iterator = _namespaceNameStack.iterator();
			while(iterator.hasNext()) {
				builder.append(iterator.next());
				if(iterator.hasNext()) {
					builder.append('.');
				}
			}
			_current = _root.getOrCreateNamespace(builder.toString());
		}
		return _current;
	}

	public IFileInputSource create(File file, DirectoryScanner loader) throws InputException {
		IFileInputSource fileInputSource = doCreateInputSource(file,loader);
		InputSourceNamespace currentNamespace = (InputSourceNamespace) currentNamespace();
		System.out.println("Adding file: "+file.getAbsolutePath()+ " to namespace "+currentNamespace.getFullyQualifiedName());
		fileInputSource.setNamespace(currentNamespace);
		return fileInputSource;
	}

	protected IFileInputSource doCreateInputSource(File file, DirectoryScanner loader) throws InputException {
		return new FileInputSource(file,loader);
	}
}


//package be.kuleuven.cs.distrinet.chameleon.workspace;
//
//import java.io.File;
//
//import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
//import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
//
//public class FileInputSourceFactory {
//
//	public void initialize(Namespace root) {
//		_currentNamespace = root;
//	}
//	private Namespace _currentNamespace;
//
//	public void pushDirectory(String name) {
//		// FIXME make this lazy to avoid creation of namespaces for directories without source files
//		//       take intermediate levels into account though.
//			_currentNamespace = _currentNamespace.getOrCreateNamespace(name);
//	}
//
//	public void popDirectory() {
//		_currentNamespace = (Namespace) _currentNamespace.parent();
//	}
//
//	protected Namespace currentNamespace() {
//		return _currentNamespace;
//	}
//
//	public IFileInputSource create(File file, DirectoryLoader loader) throws InputException {
//		IFileInputSource fileInputSource = doCreateInputSource(file,loader);
//		InputSourceNamespace currentNamespace = (InputSourceNamespace) currentNamespace();
//		fileInputSource.setNamespace(currentNamespace);
//		return fileInputSource;
//	}
//
//	protected IFileInputSource doCreateInputSource(File file, DirectoryLoader loader) throws InputException {
//		return new FileInputSource(file,loader);
//	}
//}
