package chameleon.workspace;

import java.io.File;

import chameleon.core.namespace.InputSourceNamespace;

public class FileInputSource extends StreamInputSource implements IFileInputSource {

	public FileInputSource(File file) throws InputException {
		super(convert(file));
		_file = file;
	}
	
	public File file() {
		return _file;
	}
	
	private File _file;
	
	public FileInputSource(File file, InputSourceNamespace ns) throws InputException {
		this(file);
		setNamespace(ns);
	}	
	

}
