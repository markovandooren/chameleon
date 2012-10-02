package chameleon.workspace;

import java.io.File;

import chameleon.core.namespace.Namespace;

public interface FileInputSourceFactory {

	void initialize(Namespace root);
	
	InputSource create(File file) throws InputException;
	
	void popDirectory();
	
	void pushDirectory(String name);
}
