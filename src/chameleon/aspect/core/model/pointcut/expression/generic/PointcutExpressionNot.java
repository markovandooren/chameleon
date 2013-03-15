package chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.Map;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.variable.FormalParameter;

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