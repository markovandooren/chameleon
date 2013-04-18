package be.kuleuven.cs.distrinet.chameleon.core.validation;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

/**
 * A class that combines basic problems.
 * 
 * @author Marko van Dooren
 */
public class CompositeProblem extends Invalid {

	/**
	 * The conjunction of a composite problem with another verification result
	 * is the 'invalid' conjunction of the other verification result with this object.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post \result == other.andInvalid(this);
   @*/
	@Override
	public VerificationResult and(VerificationResult other) {
		if(other != null) {
			return other.andInvalid(this);
		} else {
			return this;
		}
	}
	
	/**
	 * The conjunction of a composite problem with another invalid verification result
	 * is a composite problem that contains the basic problems of both invalid verification results.
	 */
 /*@
   @ public behavior
   @
   @ pre problem != null;
   @
   @ post (\forall BasicProblem p; ; \result.contains(p) <==> problems().contains(p) || problem.problems().contains(p));
   @*/
	@Override
	protected VerificationResult andInvalid(Invalid problem) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(problems());
		result.addAll(problem.problems());
		return result;
	}

	private List<AtomicProblem> _problems = new ArrayList<AtomicProblem>();
	
	/**
	 * Return the problems indicated by this composite problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public List<AtomicProblem> problems() {
		return new ArrayList<AtomicProblem>(_problems);
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
   @ post problems().size() == \old(problems().size()) + 1;
   @*/
	public void add(AtomicProblem problem) {
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
   @ post problems().size() == \old(problems().size()) + problems.size();
   @*/
	public void addAll(List<? extends AtomicProblem> problems) {
		for(AtomicProblem problem:problems) {
			add(problem);
		}
	}

	/**
	 * The message of a composite problem is the concatenation of the messages of its basic problems. The
	 * messages of the basic problems are separated with a newline character.
	 */
	@Override
	public String message() {
		String result = "";
		for (AtomicProblem elem : problems()) {
			result = result.concat("\n " + elem.message());
		}
		return result;
	}

	@Override
	public void setElement(Element element) {
		for(VerificationResult problem:problems()) {
			problem.setElement(element);
		}
	}
}
