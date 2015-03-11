package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;

public abstract class AbstractDynamicPointcutExpression extends ElementImpl implements PointcutExpression<Element> {
	
	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		return new MatchResult<Element>(this, joinpoint);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}
