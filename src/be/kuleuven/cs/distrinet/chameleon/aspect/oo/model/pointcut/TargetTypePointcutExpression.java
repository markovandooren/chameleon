package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.NamedTargetExpression;

public class TargetTypePointcutExpression extends TypePointcutExpression {
	
	public TargetTypePointcutExpression(NamedTargetExpression parameter) {
		super(parameter);
	}

	@Override
	public TargetTypePointcutExpression clone() {
		TargetTypePointcutExpression clone = new TargetTypePointcutExpression(parameter().clone());		
		return clone;
	}
}
