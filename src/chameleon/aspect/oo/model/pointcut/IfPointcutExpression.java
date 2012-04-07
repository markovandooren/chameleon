package chameleon.aspect.oo.model.pointcut;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.association.Single;

public class IfPointcutExpression extends AbstractDynamicPointcutExpression implements RuntimePointcutExpression<Element> {
	
	private Single<Expression> _expression = new Single<Expression>(this);

	public IfPointcutExpression(Expression expression) {
		setExpression(expression);
	}

	private void setExpression(Expression expression) {
		set(_expression, expression);
	}

	public Expression expression() {
		return _expression.getOtherEnd();
	}
	
	@Override
	public IfPointcutExpression clone() {
		Expression clonedExpression = null;
		if (expression() != null)
			clonedExpression = expression().clone();
		
		return new IfPointcutExpression(clonedExpression);
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = super.verifySelf();
		try {
			if (!expression().getType().sameAs(language(ObjectOrientedLanguage.class).booleanType()))
				result = result.and(new BasicProblem(this, "An if-expression may only contain boolean expressions"));
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "Exception during lookup "+e.getMessage()));
		}
		return result;
	}
}