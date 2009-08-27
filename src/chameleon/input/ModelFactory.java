package chameleon.input;

import java.io.File;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.namespace.Namespace;
import chameleon.tool.Connector;

public interface ModelFactory extends Connector {

	public Namespace createModel(Set<File> files) throws Exception;
	
	public void reParse(Element element);
	
//	public Set loadFiles(String path, String extension, boolean recursive);
}
