package be.kuleuven.cs.distrinet.chameleon.core.validation;

import java.util.List;

/**
 * An abstract class representing verification results that mark an element as invalid.
 * 
 * @author Marko van Dooren, Nelis Boucke
 */
public abstract class Invalid extends VerificationResult {

	/**
	 * Return the basic problems that make up this invalid verification result.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract List<AtomicProblem> problems();

	/**
	 * @return The message of this problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract String message();
}
