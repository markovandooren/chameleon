package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.weave.infrastructure.AbstractInfrastructureFactory;
import chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.Coordinator;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.RuntimeTransformationProvider;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;

public abstract class AbstractInfrastructureFactorySupportingRuntime<T extends Element> extends AbstractInfrastructureFactory<T,ProgrammingAdvice> implements RuntimeTransformationProvider<T> {

	public AbstractInfrastructureFactorySupportingRuntime() {
		super(ProgrammingAdvice.class);
	}
	
	/**
	 * 	{@inheritDoc}
	 * 
	 * 	Subclasses should override transform instead of execute. Execute also starts the runtime transformations.
	 */
	@Override
	public void execute(Advice advice, MatchResult joinpoint) throws LookupException {
		transform(advice, joinpoint);
	}

	/**
	 * 	{@inheritDoc}
	 * 
	 * 	Subclasses should override transform instead of execute. Execute also starts the runtime transformations.
	 */
	@Override
	public void transform(Advice advice, MatchResult joinpoint) throws LookupException {
		setAdvice(advice);
		setJoinpoint(joinpoint);
		T createdElement = transform();
		
		// Check if no element had to be created because it already existed.
		// If this is the case, don't perform runtime transformations
		if (createdElement == null)
			return;

		initialiseRuntimeTransformers(getJoinpoint());
		Coordinator<T> coordinator = getCoordinator(getJoinpoint());
		if (coordinator != null)
			coordinator.transform(createdElement, getAdvice().formalParameters());
	}
	
	public abstract Coordinator<T> getCoordinator(MatchResult<? extends Element> joinpoint);

	public abstract void initialiseRuntimeTransformers(MatchResult<? extends Element> joinpoint) throws LookupException;
	
	/**
	 * 	Transform the given advice
	 * 
	 * 	@param 	previous
	 * 			If there are multiple matches for a joinpoint, this parameter gives the previous one in the chain 
	 * 	@param 	next
	 * 			If there are multiple matches for a joinpoint, this parameter gives the next one in the chain 
	 * 	@throws LookupException
	 */
	public abstract T transform()  throws LookupException;

	public abstract Expression getNextExpression() throws LookupException;

}