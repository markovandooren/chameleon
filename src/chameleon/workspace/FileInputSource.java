package chameleon.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import chameleon.core.namespace.InputSourceNamespace;

public class FileInputSource extends StreamInputSource {

	public FileInputSource(File file) throws InputException {
		super(convert(file));
		_file = file;
	}
	
	public File file() {
		return _file;
	}
	
	private File _file;
	
	private static InputStream convert(File file) throws InputException {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new InputException(e);
		}
	}
	
	public FileInputSource(File file, InputSourceNamespace ns) throws InputException {
		this(file);
		setNamespace(ns);
	}	
	

}
