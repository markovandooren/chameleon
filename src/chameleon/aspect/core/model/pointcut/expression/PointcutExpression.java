package chameleon.aspect.core.model.pointcut.expression;

import java.util.List;

import org.rejuse.predicate.SafePredicate;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;

public interface PointcutExpression<J extends Element> extends NamespaceElement {
	
	/**
	 * 	Get this pointcut-expression tree but filter according to the given predicate
	 * 
	 * 	@param 	filter
	 * 			The predicate to filter
	 * 	@return	The pruned tree
	 */
	public PointcutExpression<?> retainOnly(SafePredicate<PointcutExpression<?>> filter);
	
	/**
	 * 	Get this pointcut-expression tree but filter out the types to any type but the given type (all instances of the supplied type are removed)
	 * 
	 * 	@param 	type
	 * 			The type to exclude
	 * 	@return	The pruned tree
	 */
	public PointcutExpression<?> without(Class<? extends PointcutExpression> type);
	
	/**
	 * 	Get this pointcut-expression tree but filter out according to the given filter
	 * 
	 * 	@param 	filter
	 * 			The predicate to filter
	 * 	@return	The pruned tree
	 */
	public PointcutExpression<?> without(SafePredicate<PointcutExpression<?>> filter);
	
	/**
	 * 	Get all the joinpoints in the given compilation unit that this pointcut expression selects
	 * 	
	 * 	@param 	compilationUnit
	 * 			The compilationunit to check
	 * 	@return	The list of matchresults (joinpoint and pointcut expression)
	 * 	
	 * 	@throws LookupException TODO: check if this is necessary
	 */
	public List<MatchResult> joinpoints(CompilationUnit compilationUnit) throws LookupException;
	
	/**
	 * 	Get the pointcut expression tree as a list, in post order
	 * 
	 * 	@return	The pointcut expression as a tree in post order
	 */
	public List<PointcutExpression<?>> toPostorderList();

	/**
	 * 	Check if this pointcut expression matches the given joinpoint. Note: null (as a pointcutexpression) always matches.
	 * 
	 * 	@param joinpoint
	 * 			The joinpoint to check
	 * @throws LookupException 
	 */
	public MatchResult<?> matches(Element joinpoint) throws LookupException;
	
	/**
	 * Return the type of the elements matched by this pointcut expression.
	 * @return
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ post
   @*/
	public Class<? extends J> joinPointType() throws LookupException;
}