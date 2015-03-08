package org.aikodi.chameleon.aspect.oo.weave.transform;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.weave.transform.AbstractJoinPointTransformer;
import org.aikodi.chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.Coordinator;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.RuntimeTransformationProvider;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

public abstract class AbstractJoinPointTransformerSupportingRuntimeTransformation<T extends Element,U extends Element> extends AbstractJoinPointTransformer<T,U> implements RuntimeTransformationProvider<T> {
	/**
	 * 	{@inheritDoc}
	 * @throws LookupException 
	 */
	@Override
	public final U transform() throws LookupException {
		Advice advice = joinPointWeaver().getAdvice();
		if(! (advice instanceof ProgrammingAdvice)) {
			throw new ChameleonProgrammerException();
		}
		MatchResult<T> joinpoint = joinPointWeaver().getJoinpoint();
		initialiseRuntimeTransformers(joinpoint);
		Coordinator<T> runtimeCoordinator = getCoordinator(joinpoint);
		
		U result = executeWeaving(joinpoint);
		runtimeCoordinator.transform(joinpoint.getJoinpoint(), ((ProgrammingAdvice)advice).formalParameters());
		return result;
	}
	
	public abstract Coordinator<T> getCoordinator(MatchResult<? extends Element> joinpoint);

	public abstract void initialiseRuntimeTransformers(MatchResult<? extends Element> joinpoint) throws LookupException;
	
	/**
	 * 	Execute the actual weaving
	 * 
	 * 	@param 	joinpoint
	 * 			The joinpoint to weave at
	 * 	@param 	adviceResult
	 * 			The code to weave in
	 */
	protected abstract U executeWeaving(MatchResult<T> joinpoint);

}
