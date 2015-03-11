package org.aikodi.chameleon.aspect.core.model.pointcut.expression;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;

/**
 * 	An abstract class with a default implementation for most methods of a pointcut expression.
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public abstract class AbstractPointcutExpression<J extends Element> extends ElementImpl implements PointcutExpression<J> {

}
