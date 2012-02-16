package chameleon.aspect.core.model.pointcut.expression.staticexpression.within;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public abstract class WithinPointcutExpressionDeprecated extends AbstractPointcutExpression<Element> {

	public WithinPointcutExpressionDeprecated() {
		
	}
	
	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}