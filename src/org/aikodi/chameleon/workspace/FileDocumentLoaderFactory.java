package org.aikodi.chameleon.workspace;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespace.Namespace;

public class FileDocumentLoaderFactory {

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

	public IFileDocumentLoader create(File file, DirectoryScanner scanner) throws InputException {
		IFileDocumentLoader fileDocumentLoader = doCreateDocumentLoader(file,scanner);
		DocumentLoaderNamespace currentNamespace = (DocumentLoaderNamespace) currentNamespace();
		System.out.println("Adding file: "+file.getAbsolutePath()+ " to namespace "+currentNamespace.getFullyQualifiedName());
		fileDocumentLoader.setNamespace(currentNamespace);
		return fileDocumentLoader;
	}

	protected IFileDocumentLoader doCreateDocumentLoader(File file, DirectoryScanner scanner) throws InputException {
		return new FileDocumentLoader(file,scanner);
	}
}