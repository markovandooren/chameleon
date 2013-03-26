package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.JoinPointWeaver;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.ParameterExposureProvider;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.RuntimeExpressionFactory;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

// FIXME: documentation
public interface RuntimeTransformationProvider<T extends Element> {
	public boolean supports(PointcutExpression<?> pointcutExpression);
	
	public RuntimeExpressionFactory getRuntimeExpressionProvider(RuntimePointcutExpression pointcutExpression);
	public ParameterExposureProvider getRuntimeParameterInjectionProvider(ParameterExposurePointcutExpression<?> expression);
	public JoinPointWeaver joinPointWeaver();
}
