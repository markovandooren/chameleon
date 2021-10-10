package org.aikodi.chameleon.core.validation;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.Util;

import java.util.List;

public abstract class AtomicProblem extends Invalid {
	
	/**
	 * Create a new atomic problem for the given element.
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post element() == element;
   @*/
	public AtomicProblem(Element element) {
		setElement(element);
	}

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
	 * The element to which this basic problem applies is set to the given element.
	 */
 /*@
   @ public behavior
   @
   @ post element() == element; 
   @*/
	@Override
	public void setElement(Element element) {
		if(element == null) {
			throw new ChameleonProgrammerException();
		}
		_element = element;
	}

	@Override
	public Verification and(Verification other) {
		if(other != null) {
			return other.andInvalid(this);
		} else {
			return this;
		}
	}

	@Override
	protected Verification andInvalid(Invalid compositeProblem) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(compositeProblem.problems());
		result.add(this);
		return result;
	}

	@Override
	public List<AtomicProblem> problems() {
		return Util.createNonNullList(this);
	}

	/**
	 * Returns 1.
	 */
	@Override
	public int nbProblems() {
		return 1;
	}


}
