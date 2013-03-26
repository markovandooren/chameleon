package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.staticexpression.within;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class WithinPointcutExpressionDeprecated extends AbstractPointcutExpression<Element> {

	public WithinPointcutExpressionDeprecated() {
		
	}
	
	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}
