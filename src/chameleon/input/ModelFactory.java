package chameleon.input;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import chameleon.core.element.Element;
import chameleon.tool.Connector;

public interface ModelFactory extends Connector {

	public void addToModel(Collection<File> files) throws ParseException, IOException;
	
	public void addToModel(File file) throws ParseException, IOException;
	
	public void addToModel(String compilationUnit) throws ParseException;

	public void reParse(Element element);
	
//	public Set loadFiles(String path, String extension, boolean recursive);
}
