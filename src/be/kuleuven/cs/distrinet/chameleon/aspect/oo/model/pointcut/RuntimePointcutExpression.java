package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;


public interface RuntimePointcutExpression<J extends Element> extends PointcutExpression<J> {

	//FIXME Why is this interface here? Fix the context insertion code.
}
