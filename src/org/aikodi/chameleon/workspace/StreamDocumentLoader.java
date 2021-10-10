package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.input.ModelFactory;
import org.aikodi.chameleon.input.ParseException;

import java.io.*;

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
		  //FIXME I think this should call disconnectChildren() instead.
			rawDocument().disconnect();
		}
		try {
			InputStream inputStream = inputStream();
			parse(inputStream);
			// Connect the namespace declarations in the document to the corresponding namespaces.
		} catch (Exception e) {
			throw new InputException(e);
		}
	}

  /**
   * @param inputStream
   * @throws IOException
   * @throws ParseException
   */
  protected void parse(InputStream inputStream) throws IOException, ParseException {
  	//Util.debug(toString().endsWith("Reference.java"));
    namespace().language().plugin(ModelFactory.class).parse(inputStream, rawDocument());
  }

	public abstract InputStream inputStream() throws InputException;

}
