package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;

public interface ParameterExposureProvider<T extends ParameterExposurePointcutExpression<?>> {
	public List<Statement> getParameterExposureDeclaration(T expression, FormalParameter fp) throws LookupException;
}
