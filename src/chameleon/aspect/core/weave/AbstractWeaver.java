package chameleon.aspect.core.weave;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.weave.infrastructure.AdviceInfrastructureFactory;
import chameleon.aspect.core.weave.transform.JoinPointTransformer;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 *	For general info about how the elementWeavers work, see Weaver.java 
 * 
 * 	@author Jens
 *
 */
public abstract class AbstractWeaver<T extends Element, U> implements Weaver<T, U> {
	
	public AbstractWeaver() {
	}
	
	public AbstractWeaver(Weaver next) {
		setNext(next);
	}
	
	/**
	 * 	Get the object that calculates the result of weaving
	 * 
	 * 	@return	The object that is responsible for getting the weave-result 
	 */
	public abstract JoinPointTransformer<T,U> getWeaveResultProvider(Advice<?,?> advice);	
	
	/**
	 * 	Get the object that perform transformations to the advice. This is called per join point, since this may differ per join point
	 * 	For example, method invocations that handle exceptions can differ per join point in the exception clause - so each join point (more specifically, each distinct method matched by all join points)
	 * 	must have its own transformation.
	 * 
	 * 	@param 	advice
	 * 			The advice to transform
	 * 	@param 	joinpoint
	 * 			The join point this advice was matched on
	 * 	@return The object responsible for transformation
	 */
	public abstract AdviceInfrastructureFactory getInfrastructureFactory(Advice<?,?> advice, MatchResult<T> joinpoint);
	
	/**
	 * 	The next weaver in the chain
	 */
	private Weaver next;
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public void setNext(Weaver next) {
		this.next = next;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public Weaver next() {
		return next;
	}

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public final JoinPointWeaver<T, U> weave(Advice<?,?> advice, MatchResult<T> joinpoint) throws LookupException {
		JoinPointWeaver<T, U> isHandled = handle(advice, joinpoint);
		
		if (isHandled != null)
			return isHandled;
		
		if (next() == null)
			throw new RuntimeException("No matching weaver found in chain for joinpoint of type " + joinpoint.getJoinpoint().getClass());
		
		return next().weave(advice, joinpoint);
		
	}
	
	/**
	 * 	Handle the given compilation unit, advice and join point. Used for the Chain Of Responsibility - return true if this weaver can weave
	 * 	the given parameters and perform the actual weaving, return false if it can't.
	 * 	
	 * 	@param 	compilationUnit
	 * 			The compilation unit to weave
	 * 	@param 	advice
	 * 			The advice to weave
	 * 	@param 	joinpoint
	 * 			The join points belonging to that advice
	 * @return	True if this weaver can weave the given parameters, false if not
	 * 
	 * @throws 	LookupException
	 */
	public JoinPointWeaver<T, U> handle(Advice advice, MatchResult<T> joinpoint) throws LookupException {
		if (!supports(advice, joinpoint))
			return null;
		return getJoinPointWeaver(advice, joinpoint);
	}
	
	/**
	 * 	Check if this weaver supports the given join point and advice
	 * 
	 * 	@param 	joinpoint
	 * 			The join point to check
	 * 	@param 	advce
	 * 			the advice
	 * 	@return	True if this weaver supports the join point and advice type, false otherwise
	 */	
	public abstract boolean supports(Advice<?,?> advice, MatchResult result) throws LookupException;
	
	/**
	 * 	Perform the actual weaving
	 * 
	 * 	@param 	advice
	 * 			The advice to weave
	 * 	@param 	joinpoint
	 * 			The join point belonging to that advice
	 * 	@throws LookupException
	 */
	public JoinPointWeaver<T, U> getJoinPointWeaver(Advice<?,?> advice, MatchResult<T> matchResult) throws LookupException {
		JoinPointWeaver<T, U> encapsulator = new JoinPointWeaver<T, U>(getWeaveResultProvider(advice), getInfrastructureFactory(advice, matchResult), advice, matchResult);

		return encapsulator;
	}
	
//	protected PropertySet getAround(Advice advice) {
//		return new PropertySet().with(advice.language().property("advicetype.around"));		
//	}
//	
//	protected PropertySet getBefore(Advice advice) {
//		return new PropertySet().with(advice.language().property("advicetype.before"));
//	}
//	
//	protected PropertySet getAfter(Advice advice) {
//		return new PropertySet().with(advice.language().property("advicetype.after"));
//	}
//	
//	protected PropertySet getAfterReturning(Advice advice) {
//		PropertySet afterReturning = new PropertySet();
//		afterReturning.add(advice.language().property("advicetype.after"));
//		afterReturning.add(advice.language().property("advicetype.returning"));
//	
//		return afterReturning;
//	}
//	
//	protected PropertySet getAfterThrowing(Advice advice) {
//		PropertySet afterThrowing = new PropertySet();
//		afterThrowing.add(advice.language().property("advicetype.after"));
//		afterThrowing.add(advice.language().property("advicetype.throwing"));
//		
//		return afterThrowing;
//	}
}
