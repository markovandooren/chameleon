package chameleon.input;

import java.io.File;
import java.util.Set;

import chameleon.core.namespace.Namespace;
import chameleon.tool.Connector;

public interface ModelFactory extends Connector {

	public Namespace getMetaModel(Set<File> files) throws Exception;
	
	public Set loadFiles(String path, String extension, boolean recursive);
}
