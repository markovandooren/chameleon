package org.aikodi.chameleon.core.validation;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.util.Lists;

import java.util.ArrayList;
import java.util.List;

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
	public Verification and(Verification other) {
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
	protected Verification andInvalid(Invalid problem) {
		CompositeProblem result = new CompositeProblem();
		result.addAll(problems());
		result.addAll(problem.problems());
		return result;
	}

	private List<AtomicProblem> _problems = Lists.create();
	
	/**
	 * Return the problems indicated by this composite problem.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	@Override
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
		StringBuilder builder = new StringBuilder();
		for (AtomicProblem elem : problems()) {
			builder.append(elem.message() +"\n");
		}
		return builder.toString();
	}

	@Override
	public void setElement(Element element) {
		for(Verification problem:problems()) {
			problem.setElement(element);
		}
	}

	@Override
	public int nbProblems() {
		return _problems.size();
	}
}
