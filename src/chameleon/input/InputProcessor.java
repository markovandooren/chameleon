package chameleon.input;

import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.plugin.ViewProcessor;

/**
 * An interface for processors used while reading a model from source files. These
 * processors link the parser to the tool-specific infrastructure that tracks which element
 * is at which location in the source document.
 * 
 * @author Marko van Dooren
 */
public interface InputProcessor extends ViewProcessor {

  public void setLocation(Element element, int offset, int length, Document compilationUnit, String tagType);
  
  /**
   * Remove all locations from the given element.
   * 
   * @param element The element from which all locations must be removed.
   */
  public void removeLocations(Element element);
  
  /**
   * Mark a parse error in the source document.
   * 
   * @param offset The position of the first character of the area where a parse
   *               error is detected. 
   * @param length The length of the faulty area
   * @param message The error message
   * @param element The potentially invalid element that corresponds to the text at the faulty area 
   */
  public void markParseError(int offset, int length, String message, Element element);

  /**
	 * Report a parse error.
	 * 
	 * @param exc
	 */
//	public void reportParseError(ParseException exc);

}
