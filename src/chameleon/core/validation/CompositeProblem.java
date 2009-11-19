package chameleon.core.validation;

import java.util.ArrayList;
import java.util.List;

public class CompositeProblem extends Invalid {

	@Override
	public VerificationResult and(VerificationResult other) {
		return other.andInvalid(this);
	}
	
	@Override
	protected VerificationResult andInvalid(Invalid problem) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(problems());
		result.addAll(problem.problems());
		return result;
	}

	private List<BasicProblem> _problems = new ArrayList<BasicProblem>();
	
	/**
	 * Return the problems indicated by this composite problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<BasicProblem> problems() {
		return new ArrayList<BasicProblem>(_problems);
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
	public void add(BasicProblem problem) {
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
	public void addAll(List<? extends BasicProblem> problems) {
		for(BasicProblem problem:problems) {
			add(problem);
		}
	}

	@Override
	public String message() {
		String result = "";
		for (BasicProblem elem : problems()) {
			result = result.concat("\n " + elem.message());
		}
		return result;
	}
}
