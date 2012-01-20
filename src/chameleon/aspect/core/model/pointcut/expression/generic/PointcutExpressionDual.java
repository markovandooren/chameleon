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


public abstract class PointcutExpressionDual<E extends PointcutExpressionDual<E,J>,J extends Element> extends AbstractPointcutExpression<E,J> implements RuntimePointcutExpression<E,J> {
	
	private SingleAssociation<PointcutExpressionDual, PointcutExpression> _expression1 = new SingleAssociation<PointcutExpressionDual, PointcutExpression>(this);
	private SingleAssociation<PointcutExpressionDual, PointcutExpression> _expression2 = new SingleAssociation<PointcutExpressionDual, PointcutExpression>(this);

	public PointcutExpressionDual(PointcutExpression expression1, PointcutExpression expression2) {
		super();
		setExpression1(expression1);
		setExpression2(expression2);
	}

	public PointcutExpression<?,?> expression1() {
		return _expression1.getOtherEnd();
	}

	public PointcutExpression<?,?> expression2() {
		return _expression2.getOtherEnd();
	}
	
	protected void setExpression1(PointcutExpression expression1) {
		setAsParent(_expression1, expression1);
	}

	protected void setExpression2(PointcutExpression expression2) {
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
	public List<PointcutExpression<?,?>> toPostorderList() {
		List<PointcutExpression<?,?>> result = new ArrayList<PointcutExpression<?,?>>();
		
		result.addAll(expression1().toPostorderList());
		result.addAll(expression2().toPostorderList());
		result.add(this);
		
		return result;
	}
	
//	/**
//	 * 	{@inheritDoc}
//	 */
//	@Override
//	public void renameParameters(Map<String, String> parameterNamesMap) {
//		if (expression1() instanceof ParameterExposurePointcutExpression)
//			((ParameterExposurePointcutExpression<?,?>) expression1()).renameParameters(parameterNamesMap);
//		
//		if (expression2() instanceof ParameterExposurePointcutExpression)
//			((ParameterExposurePointcutExpression<?,?>) expression2()).renameParameters(parameterNamesMap);
//	}
	
//	/**
//	 * 	{@inheritDoc}
//	 */
//	public boolean hasParameter(FormalParameter fp) {
//		return expression1().hasParameter(fp) || expression2().hasParameter(fp);
//	}
	
//	/**
//	 * 	{@inheritDoc}
//	 */
//	@Override
//	public int indexOfParameter(FormalParameter fp) {
//		int expr1 = testAndIndexOfParameter(expression1(), fp);
//		
//		if (expr1 != -1)
//			return expr1;
//		
//		return testAndIndexOfParameter(expression2(), fp);
//	}
//	
//	private int testAndIndexOfParameter(PointcutExpression<?,?> expression, FormalParameter fp) {
//		if (expression instanceof ParameterExposurePointcutExpression)
//			return ((ParameterExposurePointcutExpression<?,?>) expression).indexOfParameter(fp);
//		
//		return -1;
//	}

//	/**
//	 * 	{@inheritDoc}
//	 */
//	@Override
//	public ParameterExposurePointcutExpression<?,?> findExpressionFor(FormalParameter fp) {
//		ParameterExposurePointcutExpression<?,?> expr1 = testAndFindExpressionFor(expression1(), fp);
//		
//		if (expr1 != null)
//			return expr1;
//			
//		return testAndFindExpressionFor(expression2(), fp);
//	}
	
//	private ParameterExposurePointcutExpression<?,?> testAndFindExpressionFor(PointcutExpression<?,?> expression, FormalParameter fp) {
//		ParameterExposurePointcutExpression<?,?> expr1 = null;
//		
//		if (expression instanceof ParameterExposurePointcutExpression)
//			expr1 = ((ParameterExposurePointcutExpression<?,?>) expression.origin()).findExpressionFor(fp);
//		
//		return expr1;
//	}
}