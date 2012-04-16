package chameleon.input;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import chameleon.core.document.Document;
import chameleon.core.element.Element;
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

	/**
	 * Initialize the base infrastructure from the given collection of files.
	 * A model will be created from the given files, and any predefined elements
	 * will be added. 
	 */
	public void initializeBase(Collection<File> files) throws ParseException, IOException;
	
	/**
	 * Add the content of the given collection of files to the model.
	 */
	public void addToModel(Collection<File> files) throws ParseException, IOException;
	
	/**
	 * Add the content of the given file to the current model.
	 */
	public void addToModel(File file) throws ParseException, IOException;
	
	public void addToModel(String source, Document compilationUnit) throws ParseException;

	public void reParse(Element element) throws ParseException;
	
	/**
	 * Create a 'clone' of this model factory. The model factory will be attached to a fresh language instance.
	 * No elements will have been added to the model.
	 */
	public ModelFactory clone();
	
}
