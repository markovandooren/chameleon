package org.aikodi.chameleon.aspect.core.model.pointcut.expression.generic;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

public class PointcutExpressionAnd extends PointcutExpressionDual<Element> {

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
	protected PointcutExpressionAnd cloneSelf() {
		return new PointcutExpressionAnd(null, null);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public PointcutExpression<?> without(Predicate<PointcutExpression<?>, Nothing> filter) {		
		PointcutExpression<?> left = expression1().without(filter);
		PointcutExpression<?> right = expression2().without(filter);
		
		if (left == null && right == null)
			return null;
		if (left == null && right != null)
			return right;
		if (left != null && right == null)
			return left;
		
		return new PointcutExpressionAnd(left, right);
	}
}
