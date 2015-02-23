package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;

/**
 * A class of input sources that read file that contain only a single non-private top-level declaration
 * whose name can be derived from the filename. Java, for example, is a language that can exploit this
 * to do lazy loading of source files.
 * 
 * @author Marko van Dooren
 */
public abstract class LazyStreamInputSource extends StreamInputSource {

	/**
	 * Create a new lazy input source for the given file and namespace. The
	 * newly created input source is added to the namespace.
	 * 
	 * @param file The file to be read by this input source.
	 * @param ns The namespace to which this input source adds its elements.
	 * @param scanner The document scanner to which this input source will be
	 *               attached
	 * 
	 * @throws InputException This exception cannot actually be thrown because the
	 *                        {@link #targetDeclarationNames(Namespace)} method cannot throw it.
	 *                        It must be mentioned, however, because we can't catch exceptions
	 *                        from the super constructor call.
	 */
	public LazyStreamInputSource(String declarationName, InputSourceNamespace ns, DocumentScanner scanner) throws InputException {
		init(declarationName, ns,scanner);
	}
	
	/**
	 * Does nothing. You must invoke {@link #init(String, InputSourceNamespace, DocumentScanner)} afterwards.
	 */
	protected LazyStreamInputSource() {
		
	}
	
	protected void init(String declarationName, InputSourceNamespace ns, DocumentScanner scanner) throws InputException {
		// The super class cannot yet add the input source to the namespace because we cannot
		// set the declaration name in advance (it is needed when the input source is added to the namespace).
		if(declarationName == null) {
			throw new IllegalArgumentException();
		}
		_declarationName = declarationName;
		init(scanner);
		ns.addInputSource(this);
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
