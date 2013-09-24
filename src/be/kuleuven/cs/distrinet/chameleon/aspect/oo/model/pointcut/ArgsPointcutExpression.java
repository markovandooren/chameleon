package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NameExpression;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

public class ArgsPointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	
	private Multi<NameExpression> _parameters = new Multi<NameExpression>(this);
	
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
