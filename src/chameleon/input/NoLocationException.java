package chameleon.input;

import chameleon.core.element.Element;

/**
 * A class of exceptions to signal the absence of location related meta information.
 * 
 * @author Marko van Dooren
 */
public class NoLocationException extends Exception {

	private Element _element;
	
	/**
	 * Return the element that has no location meta information.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Element element() {
		return _element;
	}
	
	/**
	 * Create a new NoLocationException for the given element.
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post element() == element;
   @*/
	public NoLocationException(Element element) {
		_element = element;
	}
}
