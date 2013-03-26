package be.kuleuven.cs.distrinet.chameleon.oo.expression;

import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;


public abstract class TargetedExpression extends Expression {

	public abstract void setTarget(CrossReferenceTarget target);
	
	public abstract CrossReferenceTarget getTarget();
}
