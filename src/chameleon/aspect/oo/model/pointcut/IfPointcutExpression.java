package chameleon.aspect.oo.model.pointcut;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.Util;

public class IfPointcutExpression<E extends IfPointcutExpression<E>> extends AbstractDynamicPointcutExpression<E> implements RuntimePointcutExpression<E,Element> {
	
	private SingleAssociation<IfPointcutExpression<E>, Expression> _expression = new SingleAssociation<IfPointcutExpression<E>, Expression>(this);

	public IfPointcutExpression(Expression expression) {
		setExpression(expression);
	}

	private void setExpression(Expression expression) {
		setAsParent(_expression, expression);
	}

	public Expression expression() {
		return _expression.getOtherEnd();
	}
	
	@Override
	public List<? extends Element> children() {
		return Util.createNonNullList(expression());
	}

	@Override
	public E clone() {
		Expression clonedExpression = null;
		if (expression() != null)
			clonedExpression = expression().clone();
		
		return (E) new IfPointcutExpression<E>(clonedExpression);
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