package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.expression.NameExpression;
import org.aikodi.chameleon.util.association.Multi;

import java.util.List;

public class ArgsPointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	
	private Multi<NameExpression> _parameters = new Multi<NameExpression>(this);
	
	@Override
   public List<NameExpression> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public void add(NameExpression parameter) {
		add(_parameters, parameter);
	}
	
	public void addAll(List<NameExpression> parameters) {
		for (NameExpression t : parameters)
			add(t);
	}

	@Override
	public ArgsPointcutExpression cloneSelf() {
		return new ArgsPointcutExpression();
	}	
}
