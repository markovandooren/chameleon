package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.File;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;

public class FileInputSource extends StreamInputSource implements IFileInputSource {

	public FileInputSource(File file, DocumentLoader loader) throws InputException {
		super(convert(file));
		_file = file;
		init(loader);
	}
	
	public File file() {
		return _file;
	}
	
	private File _file;
	
	public FileInputSource(File file, InputSourceNamespace ns, DocumentLoader loader) throws InputException {
		this(file,loader);
		setNamespace(ns);
	}	
	
  @Override
  public String toString() {
  	return "file: "+ _file.toString();
  }
}
