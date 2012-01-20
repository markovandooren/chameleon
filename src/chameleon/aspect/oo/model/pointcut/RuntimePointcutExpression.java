package chameleon.aspect.oo.model.pointcut;

import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.element.Element;


public interface RuntimePointcutExpression<E extends RuntimePointcutExpression<E,J>,J extends Element> extends PointcutExpression<E,J> {

}
