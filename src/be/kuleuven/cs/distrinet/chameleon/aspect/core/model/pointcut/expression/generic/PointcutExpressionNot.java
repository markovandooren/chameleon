package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.generic;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
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
	public PointcutExpressionNot clone() {
		return new PointcutExpressionNot(expression().clone());
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
