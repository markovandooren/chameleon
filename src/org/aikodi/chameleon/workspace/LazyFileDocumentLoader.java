package org.aikodi.chameleon.workspace;

import java.io.File;
import java.io.InputStream;

import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;

public class LazyFileDocumentLoader extends LazyStreamDocumentLoader implements IFileDocumentLoader {

	public LazyFileDocumentLoader(File file, String declarationName, DocumentLoaderNamespace ns,DocumentScanner scanner) throws InputException {
		_file = file;
		init(declarationName, ns, scanner);
	}
	
	@Override
   public File file() {
		return _file;
	}
	
	private File _file;

  @Override
  public String toString() {
  	return "file: "+ _file.toString();
  }

	@Override
	public InputStream inputStream() throws InputException {
		return convert(file());
	}
	
  @Override
  protected String resourceName() {
  	return file().getAbsolutePath();
  }

}
