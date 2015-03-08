package org.aikodi.chameleon.workspace;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.input.ModelFactory;

public abstract class StreamDocumentLoader extends DocumentLoaderImpl {

	protected InputStream convert(File file) throws InputException {
		try {
			return new BufferedInputStream(new FileInputStream(file),16384);
		} catch (FileNotFoundException e) {
			throw new InputException(e);
		}
	}
	
	@Override
	public void doRefresh() throws InputException {
		if(rawDocument() == null) {
			setDocument(new Document());
		} else {
			rawDocument().disconnect();
		}
		try {
			InputStream inputStream = inputStream();
			namespace().language().plugin(ModelFactory.class).parse(inputStream, rawDocument());
			// Connect the namespace declarations in the document to the corresponding namespaces.
		} catch (Exception e) {
			throw new InputException(e);
		}
	}

	public abstract InputStream inputStream() throws InputException;

}
