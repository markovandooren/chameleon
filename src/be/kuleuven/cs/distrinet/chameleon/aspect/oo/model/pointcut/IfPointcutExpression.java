package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
	public Verification verifySelf() {
		Verification result = super.verifySelf();
		try {
			if (!expression().getType().sameAs(language(ObjectOrientedLanguage.class).booleanType(view().namespace())))
				result = result.and(new BasicProblem(this, "An if-expression may only contain boolean expressions"));
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "Exception during lookup "+e.getMessage()));
		}
		return result;
	}
}
