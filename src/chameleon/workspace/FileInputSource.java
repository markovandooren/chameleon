package chameleon.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import chameleon.core.document.Document;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;

public abstract class FileInputSource extends InputSourceImpl {

	public FileInputSource(File file) {
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
	public void load() throws InputException {
		if(! isLoaded()) {
			InputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(file());
			} catch (FileNotFoundException e1) {
				throw new InputException(e1);
			}
			if(document() == null) {
				setDocument(new Document());
			} else {
				document().disconnect();
			}
			try {
				namespace().language().plugin(ModelFactory.class).parse(fileInputStream, document());
			} catch (IOException | ParseException e) {
				throw new InputException(e);
			}
			setLoaded(true);
		}
	}
	
	private boolean _loaded = false;

	protected boolean isLoaded() {
		return _loaded;
	}
	
	protected void setLoaded(boolean loaded) {
		_loaded = loaded;
	}
	
	private Document _document;
	
	public Document document() {
		return _document;
	}
	
	protected void setDocument(Document doc) {
		_document = doc;
	}
}
