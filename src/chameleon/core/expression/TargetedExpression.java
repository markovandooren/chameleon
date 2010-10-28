package chameleon.core.expression;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElement;

public abstract class TargetedExpression<E extends TargetedExpression<E>> extends Expression<E> {

	public abstract void setTarget(InvocationTarget target);
	
	public abstract InvocationTarget getTarget();
}
