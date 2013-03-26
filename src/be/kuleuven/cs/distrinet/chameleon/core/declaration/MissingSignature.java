/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;

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
