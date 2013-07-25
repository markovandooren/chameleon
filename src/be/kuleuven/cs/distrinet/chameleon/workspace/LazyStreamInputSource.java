package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * A class of input sources that read file that contain only a single non-private top-level declaration
 * whose name can be derived from the filename. Java, for example, is a language that can exploit this
 * to do lazy loading of source files.
 * 
 * @author Marko van Dooren
 */
public class LazyStreamInputSource extends StreamInputSource {

	/**
	 * Create a new lazy input source for the given file and namespace. The
	 * newly created input source is added to the namespace.
	 * 
	 * @param file The file to be read by this input source.
	 * @param ns The namespace to which this input source adds its elements.
	 * 
	 * @throws InputException This exception cannot actually be thrown because the
	 *                        {@link #targetDeclarationNames(Namespace)} method cannot throw it.
	 *                        It must be mentioned, however, because we can't catch exceptions
	 *                        from the super constructor call.
	 */
	public LazyStreamInputSource(InputStream stream, String declarationName, InputSourceNamespace ns, DocumentLoader loader) throws InputException {
		super(stream);
		init(declarationName, ns,loader);
	}
	
	public void init(String declarationName, InputSourceNamespace ns, DocumentLoader loader) throws InputException {
		// The super class cannot yet add the input source to the namespace because we cannot
		// set the declaration name in advance (it is needed when the input source is added to the namespace).
		if(declarationName == null) {
			throw new IllegalArgumentException();
		}
		_declarationName = declarationName;
		init(loader);
		ns.addInputSource(this);
	}
	
	public LazyStreamInputSource(File file, String declarationName, InputSourceNamespace ns, DocumentLoader loader) throws InputException {
		super(convert(file));
		init(declarationName,ns,loader);
	}
	
	protected LazyStreamInputSource(File file) throws InputException {
		super(convert(file));
	}
	
	private String _declarationName;
	
	/**
	 * Return the file name without the file extension. For lazy loading, that name should be
	 * the same as the single non-private top-level declaration in the file.
	 */
	@Override
	public List<String> targetDeclarationNames(Namespace ns) {
		return Collections.singletonList(_declarationName);
	}

	@Override
	public LazyStreamInputSource clone() {
		try {
			return new LazyStreamInputSource(inputStream(),_declarationName,null,null);
		} catch (InputException e) {
			throw new ChameleonProgrammerException(e);
		}
	}
	

}
