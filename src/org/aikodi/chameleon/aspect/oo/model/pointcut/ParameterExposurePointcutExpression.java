package org.aikodi.chameleon.aspect.oo.model.pointcut;

import java.util.Map;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.variable.FormalParameter;

public interface ParameterExposurePointcutExpression<J extends Element> extends PointcutExpression<J> {
	
	/**
	 * 	Rename the parameters this expression exposes according to the given map
	 * 
	 * 	@param 	parameterNamesMap
	 * 			The map to rename the parameters (keys = from, corresponding values = to)
	 */
	public void renameParameters(Map<String, String> parameterNamesMap);
	
	/**
	 * 	Check if this pointcut expression has the specified parameter
	 * 
	 * 	@param 	fp
	 * 			The parameter to check
	 * 	@return	True if the parameter can be resolved, false otherwise
	 */
	public boolean hasParameter(FormalParameter fp);

}
