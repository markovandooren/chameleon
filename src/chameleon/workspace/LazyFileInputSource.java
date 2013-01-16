package chameleon.workspace;

import java.io.File;

import chameleon.core.namespace.InputSourceNamespace;

public class LazyFileInputSource extends LazyStreamInputSource implements IFileInputSource {

	public LazyFileInputSource(File file, String declarationName, InputSourceNamespace ns) throws InputException {
		super(file, declarationName, ns);
		_file = file;
	}
	
	public File file() {
		return _file;
	}
	
	private File _file;

}
