package chameleon.aspect.core.weave;

import chameleon.aspect.core.model.advice.Advice;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * 	FIXME: change comments due to returning of WeavingEncapsulator<T, U> in some methods
 * 
 * 	This interface represents an element weaver. It has the responsibility to tie the different strategies together that allow weaving, as outlined below:
 * 
 * 		- 	First, it declares the <em>WeaveResultProvider</em>. This returns what the join point is transformed into by weaving.
 * 			This can differ by the advice type.
 * 			
 * 				Example 1: MethodInvocations
 * 				Here we have the same result for all different types. The join point (a method invocation) is simply transformed into a
 * 				different method invocation.
 * 
 * 		-	Second, it declares the <em>AdviceTransformationProvider</em>. This provides an opportunity to transform advice code, if this is necessary.
 *			Again, this can differ by the advice type.
 *
 *				Example 1: MethodInvocations
 *				The TranslationStrategy for method invocations is to create a new static method in a new class (denoting the aspect), if it doesn't exist yet.
 *				Note that, since the method is different for different advice types, this is differed by advice type.
 *		
 *		-	Third, it connects these two through a <em>WeavingProvider</em>. This outlines how the join point and the weaveResultProvider are connected.
 *
 *				Example 1: MethodInvocations
 *				Remember, the join point is a method invocation and so is the result of the weaver. We simply swap these to correctly implement
 *				the weaving.
 * 
 * 	All weavers are used together in a chain of responsibility.
 * 
 * @author Jens De Temmerman
 * @author Marko van Dooren
 *
 * @param <T>	The type of the join point (a subtype of Element)
 * @param <U>	The type of the weaving result (can be anything, e.g. a MethodInvocation, or a List<Statement>, ...)
 */
public interface Weaver<T extends Element, U extends Element> {

	/**
	 * 	The start of the weaving process - each weaver is called until one can handle it. No further weavers are called once handled
	 * 
	 * 	@param 	compilationUnit
	 * 			The	compilation unit to weave 
	 * 	@param 	advice
	 * 			The advice to weave
	 * 	@param 	joinpoint
	 * 			The join points belonging to that advice
	 * 	@throws LookupException
	 */
	public JoinPointWeaver<T, U> weave(Advice<?> advice, MatchResult<T> joinpoint) throws LookupException;
	
	/**
	 * 	Get the next weaver in the chain
	 * 
	 * 	@return	The next weaver in the chain (possibly null)
	 */
	public Weaver next();
	
	/**
	 * 	Set the next weaver in the chain to the given parameter
	 * 
	 * 	@param 	next
	 * 			The next weaver in the chain
	 */
	public void setNext(Weaver next);
}
