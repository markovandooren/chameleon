package chameleon.oo.expression;

import chameleon.core.reference.CrossReferenceTarget;


public abstract class TargetedExpression<E extends TargetedExpression<E>> extends Expression<E> {

	public abstract void setTarget(CrossReferenceTarget target);
	
	public abstract CrossReferenceTarget<?> getTarget();
}
