package chameleon.core.validation;

import java.util.ArrayList;
import java.util.List;

public class CompositeProblem extends Invalid {

	@Override
	public VerificationResult and(VerificationResult other) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(problems());
	}
	
	

	private List<Invalid> _problems;
	
	/**
	 * Return the problems indicated by this composite problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<Invalid> problems() {
		return new ArrayList<Invalid>(_problems);
	}
	
	/**
	 * Add the given problem to this composite problem.
	 * @param problem
	 */
 /*@
   @ public behavior
   @
   @ pre problem != null;
   @
   @ post problems().contains(problem);
   @*/
	public void add(Invalid problem) {
		_problems.add(problem);
	}
	
	/**
	 * Add all problems in the given list to this composite problem.
	 */
 /*@
   @ public behavior
   @
   @ pre problems != null;
   @ pre ! problems.contains(null);
   @
   @ post problems().containsAll(problems);
   @*/
	public void addAll(List<Invalid> problems) {
		for(Invalid problem:problems) {
			add(problem);
		}
	}
}
