package chameleon.core.validation;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;

/**
 * A class that represents an error.
 * @author Marko van Dooren
 *
 */
public class BasicProblem extends Invalid {

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
	public List<Invalid> problems() {
		List<Invalid> result = new ArrayList<Invalid>();
		result.add(this);
		return result;
	}
}