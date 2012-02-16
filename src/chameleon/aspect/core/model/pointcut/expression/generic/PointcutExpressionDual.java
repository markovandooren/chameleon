package chameleon.aspect.core.model.pointcut.expression.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.variable.FormalParameter;


public abstract class PointcutExpressionDual<J extends Element> extends AbstractPointcutExpression<J> implements RuntimePointcutExpression<J> {
	
	private SingleAssociation<PointcutExpressionDual, PointcutExpression<J>> _expression1 = new SingleAssociation<PointcutExpressionDual, PointcutExpression<J>>(this);
	private SingleAssociation<PointcutExpressionDual, PointcutExpression<J>> _expression2 = new SingleAssociation<PointcutExpressionDual, PointcutExpression<J>>(this);

	public PointcutExpressionDual(PointcutExpression expression1, PointcutExpression expression2) {
		super();
		setExpression1(expression1);
		setExpression2(expression2);
	}

	public abstract PointcutExpressionDual<J> clone();
	
	public PointcutExpression<J> expression1() {
		return _expression1.getOtherEnd();
	}

	public PointcutExpression<J> expression2() {
		return _expression2.getOtherEnd();
	}
	
	protected void setExpression1(PointcutExpression<J> expression1) {
		setAsParent(_expression1, expression1);
	}

	protected void setExpression2(PointcutExpression<J> expression2) {
		setAsParent(_expression2, expression2);
	}

	@Override
	public List<? extends Element> children() {
		List<PointcutExpression> children = new ArrayList<PointcutExpression>();
		
		if (expression1() != null)
			children.add(expression1());
		
		if (expression2() != null)
			children.add(expression2());
		
		return children;
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
		
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