package org.aikodi.chameleon.core.validation;

import org.aikodi.chameleon.analysis.Result;
import org.aikodi.chameleon.core.element.Element;

/**
 * A class the represents the result of an analysis of a model element. An analysis result
 * can for example be valid, an error, a warning, etc.
 * 
 * @author Marko van Dooren
 */
public abstract class Verification extends Result<Verification> {

	/**
	 * Combine this verification result with the given problem.
	 * @param compositeProblem
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre problem != null;
   @*/
	protected abstract Verification andInvalid(Invalid problem);
	
	/**
	 * Change the element to which this verification result applies to the given element. If
	 * this verification result is not applicable to an element, there is no effect. If this
	 * verification result has other verification results as children, the operation is applied
	 * recursively to the children. 
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @*/
	public abstract void setElement(Element element);
}
