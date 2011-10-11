package chameleon.oo.expression;


public abstract class TargetedExpression<E extends TargetedExpression<E>> extends Expression<E> {

	public abstract void setTarget(InvocationTarget target);
	
	public abstract InvocationTarget<?> getTarget();
}
