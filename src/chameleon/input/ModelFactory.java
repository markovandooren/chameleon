package chameleon.input;

import java.io.File;
import java.util.Set;

import chameleon.core.namespace.Namespace;

public interface ModelFactory {

	public Namespace getMetaModel(Set<File> files) throws Exception;
	
	public Set loadFiles(String path, String extension, boolean recursive);
}
