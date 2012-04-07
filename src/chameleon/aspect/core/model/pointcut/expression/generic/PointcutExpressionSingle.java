package chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.util.association.Single;


public abstract class PointcutExpressionSingle<J extends Element> extends AbstractPointcutExpression<J> {
	private Single<PointcutExpression> _expression = new Single<PointcutExpression>(this);;

	public PointcutExpressionSingle(PointcutExpression expression) {
		super();
		setExpression(expression);
	}

	public PointcutExpression<?> expression() {
		return _expression.getOtherEnd();
	}

	private void setExpression(PointcutExpression expression) {
		set(_expression, expression);
	}
	
	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
		
		if (expression() == null)
			result.and(new BasicProblem(this, "The expression of this single expression cannot be null."));
		
		return result;
	}
	
	@Override
	public List<PointcutExpression<?>> toPostorderList() {
		List<PointcutExpression<?>> result = new ArrayList<PointcutExpression<?>>();
		
		result.addAll(expression().toPostorderList());
		result.add(this);
		
		return result;
	}
}
