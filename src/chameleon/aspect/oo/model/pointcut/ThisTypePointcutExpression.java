package chameleon.aspect.oo.model.pointcut;

import chameleon.oo.expression.NamedTargetExpression;

public class ThisTypePointcutExpression extends TypePointcutExpression {
	
	public ThisTypePointcutExpression(NamedTargetExpression parameter) {
		super(parameter);
	}

	@Override
	public ThisTypePointcutExpression clone() {
		ThisTypePointcutExpression clone = new ThisTypePointcutExpression(parameter().clone());		
		return clone;
	}
}
