/**
 * 
 */
package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;

/**
 * A problem class for reporting a missing signature.
 */
public class MissingSignature extends BasicProblem {

	/**
	 * Create a new missing signature problem for the given element.
	 */
 /*@
   @ public behavior
   @
   @ post element() == element;
   @*/
	public MissingSignature(Element element) {
		super(element, "The element has no signature.");
	}
	
}