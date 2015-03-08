package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.element.Element;


public interface RuntimePointcutExpression<J extends Element> extends PointcutExpression<J> {

	//FIXME Why is this interface here? Fix the context insertion code.
}
