package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.predicate.SafePredicate;

public interface PointcutExpression<J extends Element> extends Element {
	
//	public PointcutExpression<J> clone();
	
	/**
	 * 	Get this pointcut-expression tree but filter according to the given predicate
	 * 
	 * 	@param 	filter
	 * 			The predicate to filter
	 * 	@return	The pruned tree
	 */
  public default PointcutExpression<?> retainOnly(final Predicate<PointcutExpression<?>, Nothing> filter) {
    return without(filter.negation());
  }
	
	/**
	 * 	Get this pointcut-expression tree but filter out the types to any type but the given type (all instances of the supplied type are removed)
	 * 
	 * 	@param 	type
	 * 			The type to exclude
	 * 	@return	The pruned tree
	 */
  public default PointcutExpression<?> without(final Class<? extends PointcutExpression> type) {  
    return without(object -> type.isInstance(object));
  }
	
	/**
	 * 	Get this pointcut-expression tree but filter out according to the given filter
	 * 
	 * 	@param 	filter
	 * 			The predicate to filter
	 * 	@return	The pruned tree
	 */
  public default PointcutExpression<?> without(Predicate<PointcutExpression<?>,Nothing> filter) {
    PointcutExpression<?> result = null;
    if (!filter.eval(this)) {
      result = clone(this);
      result.setOrigin(origin());
    }
    return result;
  }
  
	
	/**
	 * 	Get all the joinpoints in the given compilation unit that this pointcut expression selects
	 * 	
	 * 	@param 	compilationUnit
	 * 			The compilationunit to check
	 * 	@return	The list of matchresults (joinpoint and pointcut expression)
	 * 	
	 * 	@throws LookupException TODO: check if this is necessary
	 */
  public default List<MatchResult> joinpoints(Document compilationUnit) throws LookupException {
    List<MatchResult> result = Lists.create();
    List<? extends Element> joinPoints = compilationUnit.lexical().descendants(joinPointType());
    for (Element joinPoint : joinPoints) {
      MatchResult match = matches(joinPoint);
      if (match.isMatch()) {
        result.add(match);
      }
    }
    return result; 
  }
	
	/**
	 * 	Get the pointcut expression tree as a list, in post order
	 * 
	 * 	@return	The pointcut expression as a tree in post order
	 */
  public default List<PointcutExpression<?>> toPostorderList() {
    return Collections.<PointcutExpression<?>>singletonList(this);
  }
  

	/**
	 * 	Check if this pointcut expression matches the given joinpoint. Note: null (as a pointcutexpression) always matches.
	 * 
	 * 	@param joinpoint
	 * 			The joinpoint to check
	 * @throws LookupException 
	 */
  public default MatchResult matches(Element joinpoint) throws LookupException {
    if(joinPointType().isInstance(joinpoint)) {
      return match((J)joinpoint);
    } else {
      return MatchResult.noMatch();
    }
  }
  
  public MatchResult match(J joinpoint) throws LookupException;
	
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
