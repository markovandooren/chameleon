package chameleon.aspect.oo.model.advice.weave.transform.runtime;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.aspect.core.weave.JoinPointWeaver;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.ParameterExposureProvider;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.transformationprovider.RuntimeExpressionFactory;
import chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

// FIXME: documentation
public interface RuntimeTransformationProvider<T extends Element> {
	public boolean supports(PointcutExpression<?,?> pointcutExpression);
	
	public RuntimeExpressionFactory getRuntimeExpressionProvider(RuntimePointcutExpression pointcutExpression);
	public ParameterExposureProvider getRuntimeParameterInjectionProvider(ParameterExposurePointcutExpression<?,?> expression);
	public JoinPointWeaver joinPointWeaver();
}