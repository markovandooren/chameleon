package chameleon.aspect.oo.model.pointcut;

import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.element.Element;


public interface RuntimePointcutExpression<J extends Element> extends PointcutExpression<J> {

	//FIXME Why is this interface here? Fix the context insertion code.
}
