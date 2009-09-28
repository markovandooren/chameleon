package chameleon.core.validation;

import chameleon.core.element.Element;

/**
 * A class that represents an error.
 * @author Marko van Dooren
 *
 */
public class BasicProblem extends VerificationResult {

	public BasicProblem(Element element, String message) {
		_element = element;
		_message = message;
	}
	
	private String _message;
	
	private Element _element;

	/**
	 * The element to which this problem applies.
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
	 * A description of the problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public String message() {
		return _message;
	}
	
	/**
	 * @return The message of this problem.
	 */
	public String toString() {
		return _message;
	}

	@Override
	public VerificationResult and(VerificationResult other) {
		
	}
}
