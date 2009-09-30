package chameleon.core.validation;

/**
 * A class the represents the result of a verification of a model element. A verification result
 * can for example be valid, an error, a warning, etc.
 * 
 * @author Marko van Dooren
 */
public abstract class VerificationResult {

	/**
	 * Combine this verification result with the given other verification result.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post \result != null;
   @*/
	public abstract VerificationResult and(VerificationResult other);

	protected abstract VerificationResult andInvalid(Invalid compositeProblem);
	
}
