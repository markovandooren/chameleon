package org.aikodi.chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Single;


public abstract class PointcutExpressionSingle<J extends Element> extends AbstractPointcutExpression<J> {
	private Single<PointcutExpression<J>> _expression = new Single<PointcutExpression<J>>(this, "expression");

	public PointcutExpressionSingle(PointcutExpression<J> expression) {
		super();
		setExpression(expression);
	}

	public PointcutExpression<?> expression() {
		return _expression.getOtherEnd();
	}

	private void setExpression(PointcutExpression<J> expression) {
		set(_expression, expression);
	}
	
	@Override
	public Verification verifySelf() {
		Verification result = super.verifySelf();
		
		if (expression() == null)
			result.and(new BasicProblem(this, "The expression of this single expression cannot be null."));
		
		return result;
	}
	
	@Override
	public List<PointcutExpression<?>> toPostorderList() {
		List<PointcutExpression<?>> result = Lists.create();
		
		result.addAll(expression().toPostorderList());
		result.add(this);
		
		return result;
	}
}
