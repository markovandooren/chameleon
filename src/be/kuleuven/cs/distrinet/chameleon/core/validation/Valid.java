package be.kuleuven.cs.distrinet.chameleon.core.validation;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

/**
 * A class representing the status of a valid model.
 * 
 * @pattern{name="flyweight"}
 * @author Marko van Dooren
 */
public class Valid extends Verification {
	
	private Valid() {
	}

	/**
	 * Create a new Valid object.
	 * 
	 * Performance: it uses the flyweight pattern since Valid is
	 * just a value.
	 */
	public static Valid create() {
		return _instance;
	}
	
	private static Valid _instance = new Valid();
	

	/**
	 * The string representation of a valid verification is "valid".
	 */
 /*@
   @ public behavior
   @
   @ post \result.equals("valid");
   @*/
	public String message() {
		return "valid";
	}

	/**
	 * The conjunction of a valid verification result with another verification result
	 * is the other verification result.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post \result == other;
   @*/
	@Override
	public Verification and(Verification other) {
		if(other != null) {
			return other;
		} else {
			return this;
		}
	}

	/**
	 * The conjunction of a valid verification result with an invalid verification result
	 * is the invalid verification result.
	 */
 /*@
   @ public behavior
   @
   @ pre problem != null;
   @
   @ post \result == problem;
   @*/
	@Override
	protected Verification andInvalid(Invalid problem) {
		return problem;
	}

	
	/**
	 * Does nothing.
	 */
	@Override
	public void setElement(Element element) {
		// Do nothing
	}

}
