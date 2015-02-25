package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.MethodInvocation;

public abstract class MethodInvocationPointcutExpression extends AbstractPointcutExpression<MethodInvocation> {

	@Override
	public Class<? extends MethodInvocation> joinPointType() throws LookupException {
		return MethodInvocation.class;
	}
}
