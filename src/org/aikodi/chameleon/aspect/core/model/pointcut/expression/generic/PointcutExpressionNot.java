package org.aikodi.chameleon.aspect.core.model.pointcut.expression.generic;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

public class PointcutExpressionNot extends PointcutExpressionSingle<Element> implements RuntimePointcutExpression<Element> {

	public PointcutExpressionNot(PointcutExpression expression) {
		super(expression);
	}

	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		if ((expression() instanceof RuntimePointcutExpression))
			return new MatchResult(this, joinpoint);
		
		MatchResult matches = expression().matches(joinpoint);
		
		if (matches.isMatch())
			return MatchResult.noMatch();
		else
			return new MatchResult(this, joinpoint);
	}
	

	@Override
	public PointcutExpressionNot cloneSelf() {
		return new PointcutExpressionNot(expression());
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public PointcutExpression<?> without(SafePredicate<PointcutExpression<?>> filter) {
		PointcutExpression<?> expression = expression().without(filter);
		
		if (expression == null)
			return null;
		
		return new PointcutExpressionNot(expression);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}
