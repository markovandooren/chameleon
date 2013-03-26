package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;

public abstract class MethodInvocationPointcutExpression extends AbstractPointcutExpression<MethodInvocation> {

	@Override
	public Class<? extends MethodInvocation> joinPointType() throws LookupException {
		return MethodInvocation.class;
	}
}
