package org.aikodi.chameleon.workspace;

import java.io.InputStream;

import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public class LazyReadOnceStreamDocumentLoader extends LazyStreamDocumentLoader {

	public LazyReadOnceStreamDocumentLoader(InputStream stream,
			String declarationName, DocumentLoaderNamespace ns, DocumentScanner scanner)
			throws InputException {
		super(declarationName, ns, scanner);
		_stream = stream;
	}
	
	private InputStream _stream;

	@Override
	public InputStream inputStream() throws InputException {
		return _stream;
	}
	
	@Override
	public LazyReadOnceStreamDocumentLoader clone() {
		try {
			return new LazyReadOnceStreamDocumentLoader(inputStream(),declarationName(),null,null);
		} catch (InputException e) {
			throw new ChameleonProgrammerException(e);
		}
	}
	

}
