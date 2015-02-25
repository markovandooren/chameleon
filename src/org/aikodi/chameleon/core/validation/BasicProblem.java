package org.aikodi.chameleon.core.validation;

import org.aikodi.chameleon.core.element.Element;

/**
 * A class that represents a single problem in a model. To report multiple
 * problems, the individual problems are combined using the Composite pattern.
 * 
 * @author Marko van Dooren
 */
public class BasicProblem extends AtomicProblem {

	/**
	 * Create a new basic problem for the given element with the given error message.
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @ pre message != null;
   @
   @ post element() == element;
   @ post message() == message;
   @*/
	public BasicProblem(Element element, String message) {
		super(element);
		_message = message;
	}
	
	private String _message;
	
	/**
	 * A description of the problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	@Override
   public String message() {
		return _message;
	}

}
