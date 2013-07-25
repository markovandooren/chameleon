package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.input.ModelFactory;

public class StreamInputSource extends InputSourceImpl {

	protected StreamInputSource(InputStream stream) {
		setInputStream(stream);
	}
	
	protected StreamInputSource(File file) throws InputException {
		this(convert(file));
	}
	
	protected static InputStream convert(File file) throws InputException {
		try {
			return new BufferedInputStream(new FileInputStream(file),16384);
		} catch (FileNotFoundException e) {
			throw new InputException(e);
		}
	}
	

	
	public StreamInputSource(InputStream stream, InputSourceNamespace namespace,DocumentLoader loader) throws InputException {
		this(stream);
		init(loader);
		namespace.addInputSource(this);
	}
	
	@Override
	public void doLoad() throws InputException {
		if(! isLoaded()) {
			if(rawDocument() == null) {
				setDocument(new Document());
			} else {
				rawDocument().disconnect();
			}
			try {
				namespace().language().plugin(ModelFactory.class).parse(inputStream(), rawDocument());
				// Connect the namespace declarations in the document to the corresponding namespaces.
			} catch (Exception e) {
				throw new InputException(e);
			}
		}
	}

	protected void setInputStream(InputStream stream) {
		if(stream == null) {
			throw new IllegalArgumentException();
		}
		_stream = stream;
	}
	
	private InputStream _stream;
	
	public InputStream inputStream() {
		return _stream;
	}

}
