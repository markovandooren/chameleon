package org.aikodi.chameleon.aspect.core.weave.infrastructure;

import org.aikodi.chameleon.aspect.core.model.advice.Advice;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.weave.JoinPointWeaver;
import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * 	Objects implementing this interface indicate they are responsible for transforming advice.
 * 	This are called, per advice, in a chain, after all the regular types have been woven.
 * 
 * 	Example: For method invocations, this transforms the advice as defined in the aspect to a static method.	
 * 
 * 	
 * 	@author Jens
 *
 */
public interface AdviceInfrastructureFactory {

	public JoinPointWeaver<?, ?> joinPointWeaver();
	
	public void setJoinPointWeaver(JoinPointWeaver<?, ?> joinPointWeaver);
	
	/**
	 * 	Start the advice transformation
	 * 
	 * 	@param 	advice
	 * 			The advice to transform
	 *  @param	joinpoint
	 *  		The joinpoint the advice was applied to
	 * 	@param 	previous
	 * 			If there are multiple matches for a joinpoint, this parameter gives the previous one in the chain 
	 * 	@param 	next
	 * 			If there are multiple matches for a joinpoint, this parameter gives the next one in the chain 
	 * 	@throws LookupException
	 */
	public void execute(Advice advice, MatchResult joinpoint) throws LookupException;

	/**
	 * 	Start the advice transformation
	 * 
	 * 	@param 	advice
	 * 			The advice to transform
	 *  @param	joinpoint
	 *  		The joinpoint the advice was applied to
	 * 	@param 	previous
	 * 			If there are multiple matches for a joinpoint, this parameter gives the previous one in the chain 
	 * 	@param 	next
	 * 			If there are multiple matches for a joinpoint, this parameter gives the next one in the chain 
	 * 	@throws LookupException
	 */
	public void transform(Advice advice, MatchResult joinpoint) throws LookupException;	
}
