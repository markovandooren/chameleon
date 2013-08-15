package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;
import java.io.InputStream;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;

public class LazyFileInputSource extends LazyStreamInputSource implements IFileInputSource {

	public LazyFileInputSource(File file, String declarationName, InputSourceNamespace ns,DocumentLoader loader) throws InputException {
		_file = file;
		init(declarationName, ns, loader);
	}
	
	public File file() {
		return _file;
	}
	
	private File _file;

  @Override
  public String toString() {
  	return "file: "+ _file.toString();
  }

	@Override
	public InputStream inputStream() throws InputException {
		return convert(file());
	}
}
