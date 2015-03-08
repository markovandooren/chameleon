package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.oo.expression.NameExpression;

public class TargetTypePointcutExpression extends TypePointcutExpression {
	
	public TargetTypePointcutExpression(NameExpression parameter) {
		super(parameter);
	}

	@Override
	protected TargetTypePointcutExpression cloneSelf() {
		return new TargetTypePointcutExpression(null);
	}
}
