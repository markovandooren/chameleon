package chameleon.aspect.core.weave.infrastructure;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ChameleonProgrammerException;


/**
 * 	Default implementation of some of the AdviceTransformationProvider methods
 *
 */
public abstract class AbstractInfrastructureFactory<T extends Element, A extends Advice> implements AdviceInfrastructureFactory {
	
	public AbstractInfrastructureFactory(Class<? extends A> adviceType) {
		this._adviceType = adviceType;
	}
	
	public void setJoinPointWeaver(JoinPointWeaver joinPointWeaver) {
		_joinPointWeaver = joinPointWeaver;
	}
	
	@Override
	public JoinPointWeaver joinPointWeaver() {
		return _joinPointWeaver;
	}
	
	private JoinPointWeaver _joinPointWeaver; 
	
	/**
	 * 	The joinpoint that is woven
	 */
	private MatchResult _joinpoint;
	
	/**
	 * 	The advice that is transformed
	 */
	private A _advice;
	
	private Class<? extends A> _adviceType;
	
	/**
	 * 	Get the advice to transform
	 * 
	 * 	@return The advice to transform
	 */
	public A getAdvice() {
		return _advice;
	}

	/**
	 * 	Get the joinpoint this advice was applied to
	 * 
	 * 	@return	The joinpoint this advice was applied to
	 */
	public MatchResult<?> getJoinpoint() {
		return _joinpoint;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public abstract void execute(Advice advice, MatchResult joinpoint) throws LookupException;

	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public abstract void transform(Advice advice, MatchResult joinpoint) throws LookupException;
	
	/**
	 * 	Transform the given advice
	 * 
	 * 	NOTE: subclasses should directly override execute instead of this method. DO NOT OVERRIDE OR REIMPLEMENT THIS METHOD, IT IS OUTDATED.
	 * 
	 * @param	previous
	 * 			If there are multiple matches for a joinpoint, this parameter gives the previous one in the chain 
	 * @param 	next
	 * 			If there are multiple matches for a joinpoint, this parameter gives the next one in the chain 
	 * 
	 * 	@throws LookupException
	 */
//	@Deprecated
//	public final T transform(WeavingEncapsulator previous, WeavingEncapsulator next)  throws LookupException {
//		throw new ChameleonProgrammerException("Method transform not implemented. This method isn't abstract because it is deprecated, so a default implementation has been added. Override execute instead!!");
//	}

	/**
	 * 	Set the advice to transform
	 * 
	 * 	@param 	advice
	 * 			The advice to transform
	 */
	protected void setAdvice(Advice advice) {
		if(! _adviceType.isInstance(advice)) {
			throw new ChameleonProgrammerException();
		}
		this._advice = (A)advice;
	}

	/**
	 * 	Set the joinpoint the advice is applied to
	 * 
	 * 	@param 	joinpoint
	 * 			The joinpoint the advice is applied to
	 */
	protected void setJoinpoint(MatchResult joinpoint) {
		this._joinpoint = joinpoint;
	}
}
