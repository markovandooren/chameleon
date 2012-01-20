package chameleon.aspect.core.weave.infrastructure;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * 	Represents a transformation that doesn't do anything.
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 *
 */
public class NoOperationFactory extends AbstractInfrastructureFactory<Element,Advice> {

	public NoOperationFactory() {
		super(Advice.class);
	}
	
	/**
	 * 	{@inheritDoc}
	 * 
	 * 	No operation, so don't do anything
	 */
	@Override
	public void execute(Advice advice, MatchResult joinpoint) throws LookupException {
		transform(advice, joinpoint);
	}

	/**
	 * 	{@inheritDoc}
	 * 
	 * 	No operation, so don't do anything
	 */
	@Override
	public void transform(Advice advice, MatchResult joinpoint) throws LookupException {

	}

}