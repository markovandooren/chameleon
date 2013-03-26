package be.kuleuven.cs.distrinet.chameleon.core.analysis;

public abstract class AnalysisResult {
	
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


}
