package org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider;

import org.aikodi.chameleon.aspect.core.weave.registry.NamingRegistry;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;

/**
 * 	Represents an expression provider for runtime checks. Given a runtime check, return the matching expression.
 *
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public interface RuntimeExpressionFactory<T extends RuntimePointcutExpression<?>> {
	public Expression getExpression(T expr, NamingRegistry<RuntimePointcutExpression<?>> namingRegistry) throws LookupException;
}
