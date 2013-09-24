package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.NameExpression;

public class ThisTypePointcutExpression extends TypePointcutExpression {
	
	public ThisTypePointcutExpression(NameExpression parameter) {
		super(parameter);
	}

	@Override
	protected ThisTypePointcutExpression cloneSelf() {
		return new ThisTypePointcutExpression(null);
	}
}
