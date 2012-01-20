package chameleon.aspect.oo.model.pointcut;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

public abstract class AbstractDynamicPointcutExpression<E extends AbstractDynamicPointcutExpression<E>> extends AbstractPointcutExpression<E,Element> {
	
	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		return new MatchResult<Element>(this, joinpoint);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return Element.class;
	}
}