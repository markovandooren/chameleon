package chameleon.oo.expression;

import chameleon.core.reference.CrossReferenceTarget;


public abstract class TargetedExpression extends Expression {

	public abstract void setTarget(CrossReferenceTarget target);
	
	public abstract CrossReferenceTarget getTarget();
}
