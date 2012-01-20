package chameleon.aspect.core.model.pointcut.expression.generic;

import org.rejuse.predicate.SafePredicate;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public class PointcutExpressionAnd<E extends PointcutExpressionAnd<E>> extends PointcutExpressionDual<E,Element> {

	public PointcutExpressionAnd(PointcutExpression expression1, PointcutExpression expression2) {
		super(expression1, expression2);
	}

	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		boolean r1match = (expression1() instanceof RuntimePointcutExpression) ||  expression1().matches(joinpoint).isMatch();
		boolean r2match = (expression2() instanceof RuntimePointcutExpression) ||  expression2().matches(joinpoint).isMatch();
		
		if (r1match && r2match)
			return new MatchResult<Element>(this, joinpoint);
		else
			return MatchResult.noMatch();
	}

	@Override
	public E clone() {
		return (E) new PointcutExpressionAnd<E>(expression1().clone(), expression2().clone());
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public PointcutExpression<?,?> without(SafePredicate<PointcutExpression<?,?>> filter) {		
		PointcutExpression<?,?> left = expression1().without(filter);
		PointcutExpression<?,?> right = expression2().without(filter);
		
		if (left == null && right == null)
			return null;
		if (left == null && right != null)
			return right;
		if (left != null && right == null)
			return left;
		
		return new PointcutExpressionAnd<E>(left, right);
	}
}