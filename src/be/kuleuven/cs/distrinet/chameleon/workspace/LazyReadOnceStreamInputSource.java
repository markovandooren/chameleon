package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.InputStream;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public class LazyReadOnceStreamInputSource extends LazyStreamInputSource {

	public LazyReadOnceStreamInputSource(InputStream stream,
			String declarationName, InputSourceNamespace ns, DocumentScanner loader)
			throws InputException {
		super(declarationName, ns, loader);
		_stream = stream;
	}
	
	private InputStream _stream;

	@Override
	public InputStream inputStream() throws InputException {
		return _stream;
	}
	
	@Override
	public LazyReadOnceStreamInputSource clone() {
		try {
			return new LazyReadOnceStreamInputSource(inputStream(),declarationName(),null,null);
		} catch (InputException e) {
			throw new ChameleonProgrammerException(e);
		}
	}
	

}
