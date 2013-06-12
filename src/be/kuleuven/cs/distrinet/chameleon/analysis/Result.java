package be.kuleuven.cs.distrinet.chameleon.analysis;


public abstract class Result<T extends Result> {
	
	/**
	 * Return a message that describes the result of the analysis.
	 */
  public abstract String message();
  
	/**
	 * @return The message of this problem.
	 */
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