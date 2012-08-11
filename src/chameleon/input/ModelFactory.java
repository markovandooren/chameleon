package chameleon.input;

import java.io.IOException;
import java.io.InputStream;

import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.core.namespace.RootNamespace;
import chameleon.plugin.Plugin;

/**
 * An interface for model factories. A model factory must be able to create
 * a model from a collection of files. In addition, it must add all predefined types
 * and operations.
 * 
 * A model factory is attached to a Language object as a Connector.
 * @author Marko van Dooren
 */
public interface ModelFactory extends Plugin {

	
	public void parse(String source, Document compilationUnit) throws ParseException;
	
	public void parse(InputStream inputStream, Document cu) throws IOException, ParseException;

	public void refresh(Element element) throws ParseException;
	
	public void initializePredefinedElements();
	
	/**
	 * Create a 'clone' of this model factory. The model factory will be attached to a fresh language instance.
	 * No elements will have been added to the model.
	 */
	public ModelFactory clone();
	
}
