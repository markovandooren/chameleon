package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.NameExpression;

public class TargetTypePointcutExpression extends TypePointcutExpression {
	
	public TargetTypePointcutExpression(NameExpression parameter) {
		super(parameter);
	}

	@Override
	protected TargetTypePointcutExpression cloneSelf() {
		return new TargetTypePointcutExpression(null);
	}
}
