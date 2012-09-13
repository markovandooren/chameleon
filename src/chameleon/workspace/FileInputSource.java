package chameleon.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import chameleon.core.document.Document;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;

public abstract class FileInputSource implements InputSource {

	public FileInputSource(File file, ModelFactory factory) {
		if(file == null) {
			throw new IllegalArgumentException();
		}
		if(factory == null) {
			throw new IllegalArgumentException();
		}
		_file = file;
		_factory = factory;

	}
	
	private File _file;
	
	public File file() {
		return _file;
	}
	
	public ModelFactory modelFactory() {
		return _factory;
	}

	private ModelFactory _factory;
	
	@Override
	public void load() throws InputException {
		if(! _loaded) {
			InputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(file());
			} catch (FileNotFoundException e1) {
				throw new InputException(e1);
			}
			if(_document == null) {
				_document = new Document();
			} else {
				_document.disconnect();
			}
			try {
				modelFactory().parse(fileInputStream, _document);
			} catch (IOException | ParseException e) {
				throw new InputException(e);
			}
			_loaded = true;
		}
	}
	
	private boolean _loaded = false;

//	public void refresh() throws IOException, ParseException {
//		_loaded = false;
//		load();
//	}
	
	private Document _document;
	
	public Document document() {
		return _document;
	}
	
}
