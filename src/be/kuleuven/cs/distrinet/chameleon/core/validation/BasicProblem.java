package be.kuleuven.cs.distrinet.chameleon.core.validation;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

/**
 * A class that represents a single problem in a model. To report multiple
 * problems, the individual problems are combined using the Composite pattern.
 * 
 * @author Marko van Dooren
 */
public class BasicProblem extends Invalid {

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

	@Override
	public VerificationResult and(VerificationResult other) {
		return other.andInvalid(this);
	}

	@Override
	protected VerificationResult andInvalid(Invalid compositeProblem) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(compositeProblem.problems());
		result.add(this);
		return result;
	}

	@Override
	public List<BasicProblem> problems() {
		List<BasicProblem> result = new ArrayList<BasicProblem>();
		result.add(this);
		return result;
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
}
