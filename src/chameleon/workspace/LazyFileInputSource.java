package chameleon.workspace;

import java.io.File;
import java.util.Collections;
import java.util.List;

import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.util.Util;

/**
 * A class of input sources that read file that contain only a single non-private top-level declaration
 * whose name can be derived from the filename. Java, for example, is a language that can exploit this
 * to do lazy loading of source files.
 * 
 * @author Marko van Dooren
 */
public class LazyFileInputSource extends FileInputSource {

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
	public LazyFileInputSource(File file, InputSourceNamespace ns) throws InputException {
		super(file, ns);
	}
	
	/**
	 * Return the file name without the file extension. For lazy loading, that name should be
	 * the same as the single non-private top-level declaration in the file.
	 */
	@Override
	public List<String> targetDeclarationNames(Namespace ns) {
		return Collections.singletonList(Util.getAllButLastPart(file().getName()));
	}

	@Override
	public LazyFileInputSource clone() {
		try {
			return new LazyFileInputSource(file(),null);
		} catch (InputException e) {
			throw new ChameleonProgrammerException(e);
		}
	}
	

}