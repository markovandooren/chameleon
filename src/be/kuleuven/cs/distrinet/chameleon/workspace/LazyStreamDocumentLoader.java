package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.DocumentLoaderNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;

/**
 * A class of document loaders that read file that contain only a single non-private top-level declaration
 * whose name can be derived from the filename. Java, for example, is a language that can exploit this
 * to do lazy loading of source files.
 * 
 * @author Marko van Dooren
 */
public abstract class LazyStreamDocumentLoader extends StreamDocumentLoader {

	/**
	 * Create a new lazy document loader for the given file and namespace. The
	 * newly created document loader is added to the namespace.
	 * 
	 * @param file The file to be read by this document loader.
	 * @param ns The namespace to which this document loader adds its elements.
	 * @param scanner The document scanner to which this document loader will be
	 *               attached
	 * 
	 * @throws InputException This exception cannot actually be thrown because the
	 *                        {@link #targetDeclarationNames(Namespace)} method cannot throw it.
	 *                        It must be mentioned, however, because we can't catch exceptions
	 *                        from the super constructor call.
	 */
	public LazyStreamDocumentLoader(String declarationName, DocumentLoaderNamespace ns, DocumentScanner scanner) throws InputException {
		init(declarationName, ns,scanner);
	}
	
	/**
	 * Does nothing. You must invoke {@link #init(String, DocumentLoaderNamespace, DocumentScanner)} afterwards.
	 */
	protected LazyStreamDocumentLoader() {
		
	}
	
	protected void init(String declarationName, DocumentLoaderNamespace ns, DocumentScanner scanner) throws InputException {
		// The super class cannot yet add the document loader to the namespace because we cannot
		// set the declaration name in advance (it is needed when the document loader is added to the namespace).
		if(declarationName == null) {
			throw new IllegalArgumentException();
		}
		_declarationName = declarationName;
		init(scanner);
		ns.addDocumentLoader(this);
	}
	
	private String _declarationName;
	
	public String declarationName() {
		return _declarationName;
	}
	
	/**
	 * Return the file name without the file extension. For lazy loading, that name should be
	 * the same as the single non-private top-level declaration in the file.
	 */
	@Override
	public List<String> targetDeclarationNames(Namespace ns) {
		return Collections.singletonList(_declarationName);
	}


}
