package org.aikodi.chameleon.input;

import java.io.IOException;
import java.io.InputStream;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.plugin.LanguagePlugin;

/**
 * An interface for model factories. A model factory must be able to create
 * a model from a collection of files. In addition, it must add all predefined types
 * and operations.
 *
 * FIXME: rename this to Parser or SourceParser
 * 
 * A model factory is attached to a Language object as a Connector.
 * @author Marko van Dooren
 */
public interface ModelFactory extends LanguagePlugin {

	
	public void parse(String source, Document compilationUnit) throws ParseException;
	
	public void parse(InputStream inputStream, Document cu) throws IOException, ParseException;

	public void refresh(Element element) throws ParseException;
	
//	public void initializePredefinedElements();
	
	/**
	 * Create a 'clone' of this model factory. The model factory will be attached to a fresh language instance.
	 * No elements will have been added to the model.
	 */
	@Override
   public ModelFactory clone();
	
}
