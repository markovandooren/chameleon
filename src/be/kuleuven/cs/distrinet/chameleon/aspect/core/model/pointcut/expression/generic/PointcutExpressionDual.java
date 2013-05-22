package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;


public abstract class PointcutExpressionDual<J extends Element> extends AbstractPointcutExpression<J> implements RuntimePointcutExpression<J> {
	
	private Single<PointcutExpression<J>> _expression1 = new Single<PointcutExpression<J>>(this);
	private Single<PointcutExpression<J>> _expression2 = new Single<PointcutExpression<J>>(this);

	public PointcutExpressionDual(PointcutExpression expression1, PointcutExpression expression2) {
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
		List<PointcutExpression<?>> result = new ArrayList<PointcutExpression<?>>();
		
		result.addAll(expression1().toPostorderList());
		result.addAll(expression2().toPostorderList());
		result.add(this);
		
		return result;
	}
	
}
