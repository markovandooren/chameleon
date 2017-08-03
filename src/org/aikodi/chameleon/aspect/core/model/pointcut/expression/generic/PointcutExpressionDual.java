package org.aikodi.chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Single;


public abstract class PointcutExpressionDual<J extends Element> extends AbstractPointcutExpression<J> implements RuntimePointcutExpression<J> {
	
	private Single<PointcutExpression<J>> _expression1 = new Single<PointcutExpression<J>>(this, "first expression");
	private Single<PointcutExpression<J>> _expression2 = new Single<PointcutExpression<J>>(this, "second expression");

	public PointcutExpressionDual(PointcutExpression<J> expression1, PointcutExpression<J> expression2) {
		super();
		setExpression1(expression1);
		setExpression2(expression2);
	}

//	public abstract PointcutExpressionDual<J> clone();
	
	public PointcutExpression<J> expression1() {
		return _expression1.getOtherEnd();
	}

	public PointcutExpression<J> expression2() {
		return _expression2.getOtherEnd();
	}
	
	protected void setExpression1(PointcutExpression<J> expression1) {
		set(_expression1, expression1);
	}

	protected void setExpression2(PointcutExpression<J> expression2) {
		set(_expression2, expression2);
	}

	@Override
	public Verification verifySelf() {
		Verification result = super.verifySelf();
		
		if (expression1() == null)
			result.and(new BasicProblem(this, "The first expression of this dual expression cannot be null."));
		
		if (expression2() == null)
			result.and(new BasicProblem(this, "The second expression of this dual expression cannot be null."));
		
		return result;
	}
	
	@Override
	public List<PointcutExpression<?>> toPostorderList() {
		List<PointcutExpression<?>> result = Lists.create();
		
		result.addAll(expression1().toPostorderList());
		result.addAll(expression2().toPostorderList());
		result.add(this);
		
		return result;
	}
	
}
