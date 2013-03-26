package be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.JoinPointWeaver;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

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

	public JoinPointWeaver joinPointWeaver();
	
	public void setJoinPointWeaver(JoinPointWeaver joinPointWeaver);
	
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
