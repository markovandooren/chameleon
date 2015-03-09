package org.aikodi.chameleon.analysis;


/**
 * The result of a computation.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The "self" type.
 */
public abstract class Result<T extends Result<T>> {
	
	/**
	 * Return a message that describes the result of the analysis.
	 */
  public abstract String message();
  
	/**
	 * @return The message of this problem.
	 */
	@Override
   public String toString() {
		return message();
	}

	
	/**
	 * Combine this analysis result with the given other analysis result.
	 * The given verification analysis is not modified.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post \result != null;
   @*/
	public abstract T and(T other);

}