package chameleon.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;

public class FileInputSource extends InputSourceImpl {

	public FileInputSource(File file) throws InputException {
		this(file,null);
	}
	
	public FileInputSource(File file, InputSourceNamespace ns) throws InputException {
		super();
		if(file == null) {
			throw new IllegalArgumentException();
		}
		_file = file;
		setNamespace(ns);
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
			} catch (IOException | ParseException e) {
				throw new InputException(e);
			}
		}
	}
	
}
