package org.aikodi.chameleon.aspect.core.model.pointcut.expression.staticexpression.within;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public abstract class WithinPointcutExpressionDeprecated extends AbstractPointcutExpression<Element> {

	public WithinPointcutExpressionDeprecated() {
		
	}
	
	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}
