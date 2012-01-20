package chameleon.aspect.oo.model.pointcut;

import java.util.List;

import chameleon.oo.expression.NamedTargetExpression;

public class ThisTypePointcutExpression<E extends ThisTypePointcutExpression<E>> extends TypePointcutExpression<E> {
	
	public ThisTypePointcutExpression(NamedTargetExpression parameter) {
		super(parameter);
	}

	@Override
	public E clone() {
		ThisTypePointcutExpression<E> clone = new ThisTypePointcutExpression<E>(parameter().clone());		
		return (E) clone;
	}
}
