package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class AbstractDynamicPointcutExpression extends AbstractPointcutExpression<Element> {
	
	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		return new MatchResult<Element>(this, joinpoint);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}
