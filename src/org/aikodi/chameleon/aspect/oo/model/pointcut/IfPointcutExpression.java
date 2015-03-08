package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.util.association.Single;

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
	public IfPointcutExpression cloneSelf() {
		return new IfPointcutExpression(null);
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
