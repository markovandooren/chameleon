package org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.aspect.core.weave.JoinPointWeaver;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.ParameterExposureProvider;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.RuntimeExpressionFactory;
import org.aikodi.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.element.Element;

// FIXME: documentation
public interface RuntimeTransformationProvider<T extends Element> {
	public boolean supports(PointcutExpression<?> pointcutExpression);
	
	public RuntimeExpressionFactory getRuntimeExpressionProvider(RuntimePointcutExpression pointcutExpression);
	public ParameterExposureProvider getRuntimeParameterInjectionProvider(ParameterExposurePointcutExpression<?> expression);
	public JoinPointWeaver joinPointWeaver();
}
