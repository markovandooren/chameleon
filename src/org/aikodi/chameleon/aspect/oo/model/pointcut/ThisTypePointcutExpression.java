package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.oo.expression.NameExpression;

public class ThisTypePointcutExpression extends TypePointcutExpression {
	
	public ThisTypePointcutExpression(NameExpression parameter) {
		super(parameter);
	}

	@Override
	protected ThisTypePointcutExpression cloneSelf() {
		return new ThisTypePointcutExpression(null);
	}
}
