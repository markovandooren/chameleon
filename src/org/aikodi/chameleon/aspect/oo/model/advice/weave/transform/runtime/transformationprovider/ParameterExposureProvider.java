package org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider;

import org.aikodi.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.variable.FormalParameter;

import java.util.List;

public interface ParameterExposureProvider<T extends ParameterExposurePointcutExpression<?>> {
	public List<Statement> getParameterExposureDeclaration(T expression, FormalParameter fp) throws LookupException;
}
