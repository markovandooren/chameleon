package org.aikodi.chameleon.workspace;

import java.io.InputStream;

import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public class LazyReadOnceStreamDocumentLoader extends LazyStreamDocumentLoader {

	private InputStream _stream;
  private String _resourceName;
	public LazyReadOnceStreamDocumentLoader(InputStream stream,
			String declarationName, 
			DocumentLoaderNamespace ns, 
			DocumentScanner scanner, 
			String resourceName) throws InputException {
		super(declarationName, ns, scanner);
		_stream = stream;
		_resourceName = resourceName;
	}
	
	@Override
	public InputStream inputStream() throws InputException {
		return _stream;
	}
	
	@Override
	public LazyReadOnceStreamDocumentLoader clone() {
		try {
			return new LazyReadOnceStreamDocumentLoader(inputStream(),declarationName(),null,null,_resourceName);
		} catch (InputException e) {
			throw new ChameleonProgrammerException(e);
		}
	}
	
  @Override
  protected String resourceName() {
  	return _resourceName;
  }


}
