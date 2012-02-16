package chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider;

import java.util.List;

import chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Statement;
import chameleon.oo.variable.FormalParameter;

public interface ParameterExposureProvider<T extends ParameterExposurePointcutExpression<?>> {
	public List<Statement> getParameterExposureDeclaration(T expression, FormalParameter fp) throws LookupException;
}
