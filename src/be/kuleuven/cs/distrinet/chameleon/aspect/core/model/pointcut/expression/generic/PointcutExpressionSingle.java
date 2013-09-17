package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;


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
