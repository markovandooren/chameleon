package org.aikodi.chameleon.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.plugin.LanguagePlugin;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.association.Association;

/**
 * An object that parses source text. A parser must be able to create
 * a model from string. A parse is attached to a {@link Language} object 
 * via {@link #setLanguage(org.aikodi.chameleon.core.language.Language, Class)}.
 * 
 * @author Marko van Dooren
 */
public interface ModelFactory extends LanguagePlugin {

	/**
	 * Parse the given source text for the given document. The content
	 * of the document is replaced by the result of parsin the given source
	 * String.
	 * 
	 * @param source The String representation to be parsed.
	 * @param document The document in which the parse result must be placed.
	 * @throws ParseException The given text could not be parsed.
	 */
   public default void parse(String source, Document document) throws ParseException {
    InputStream inputStream;
    try {
      inputStream = new ByteArrayInputStream(source.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e1) {
      throw new ChameleonProgrammerException(e1);
    }
    try {
      parse(inputStream, document);
    } catch (IOException e) {
      // cannot happen if we work with a String
      throw new ChameleonProgrammerException("IOException while parsing a String.", e);
    }
  }

   /**
    * Parse the conmtent of the given input stream for the given document. 
    * The content of the document is replaced by the result of parsing the 
    * content of the given input stream.
    * 
    * @param source The input stream of the text to be parsed.
    * @param document The document in which the parse result must be placed.
    * @throws ParseException The given text could not be parsed.
    */
	public void parse(InputStream inputStream, Document cu) throws IOException, ParseException;

	/**
	 * <p>Refresh the given element.</p>
	 * 
	 * <p>The method requires a {@link SourceManager} to be registered
	 * in the view of the given element. The source manager is asked to
	 * provide the String that corresponds to the given element, but which
	 * may have changed since the construction of the given element. This
	 * String is then parsed, and the element is replaced by the parse result.</p>
	 * 
	 * @param element The element to be refreshed.
	 * @throws ParseException
	 */
	public default void refresh(Element element) throws ParseException {
    View view = element.view();
    boolean done = false;
    if(view != null) {
      SourceManager manager = view.plugin(SourceManager.class);
      while((element != null) && (! done)){
        try {
          String text = manager.text(element);
          Element newElement = parse(element, text);
          if(newElement != null) {
            // Use raw type here, we can't really type check this.
            Association childLink = element.parentLink().getOtherRelation();
            childLink.replace(element.parentLink(), newElement.parentLink());
//            clearPositions(element,view);
            done = true;
            break;
          }
        } catch(ParseException exc) {
        } catch (NoLocationException e) {
        }
        Element old = element;
        if(! done) {
          element = element.parent();
          if(element == null) {
            throw new ParseException(old.lexical().nearestAncestor(Document.class));
          }
        }
        old.disconnect();
      }
    }
	}
	
	/**
	 * Try to parse the given text as if it were a replacement for the given element.
	 * 
	 * @param element The element that will be replaced by the parse result. 
	 * @param text The text to be parsed.
	 * @return An element that is constructed by parsing the given text.
	 * @throws ParseException The given text could not be parsed, or is not a
	 * text that can serve as a replacement for the given element.
	 */
  public default <P extends Element> Element parse(Element element, String text) throws ParseException {
    throw new ParseException("Partial parsing not supported by this model factory.", element.lexical().nearestAncestor(Document.class));
  }
	
	/**
	 * Create a 'clone' of this model factory. The model factory will be attached to a fresh language instance.
	 * No elements will have been added to the model.
	 */
	@Override
   public ModelFactory clone();
	
}
