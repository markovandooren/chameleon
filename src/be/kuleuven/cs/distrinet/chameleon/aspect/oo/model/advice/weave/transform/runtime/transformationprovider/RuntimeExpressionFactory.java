package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.registry.NamingRegistry;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;

/**
 * 	Represents an expression provider for runtime checks. Given a runtime check, return the matching expression.
 *
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public interface RuntimeExpressionFactory<T extends RuntimePointcutExpression<?>> {
	public Expression getExpression(T expr, NamingRegistry<RuntimePointcutExpression<?>> namingRegistry) throws LookupException;
}
