package chameleon.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import chameleon.core.document.Document;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;

public class FileInputSource extends InputSourceImpl {

	public FileInputSource(File file) throws InputException {
		setFile(file);
	}
	
	public FileInputSource(File file, InputSourceNamespace ns) throws InputException {
		this(file);
		setNamespace(ns);
	}

	protected void setFile(File file) {
		if(file == null) {
			throw new IllegalArgumentException();
		}
		_file = file;
	}
	
	private File _file;
	
	public File file() {
		return _file;
	}
	
	

	@Override
	public void doLoad() throws InputException {
		if(! isLoaded()) {
			InputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(file());
			} catch (FileNotFoundException e1) {
				throw new InputException(e1);
			}
			if(rawDocument() == null) {
				setDocument(new Document());
			} else {
				rawDocument().disconnect();
			}
			try {
				namespace().language().plugin(ModelFactory.class).parse(fileInputStream, rawDocument());
				// Connect the namespace declarations in the document to the corresponding namespaces.
			} catch (Exception e) {
				throw new InputException(e);
			}
		}
	}
	
}
