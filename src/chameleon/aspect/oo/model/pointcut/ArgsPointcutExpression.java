package chameleon.aspect.oo.model.pointcut;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.util.association.Multi;

public class ArgsPointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	
	private Multi<NamedTargetExpression> _parameters = new Multi<NamedTargetExpression>(this);
	
	public List<NamedTargetExpression> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public void add(NamedTargetExpression parameter) {
		add(_parameters, parameter);
	}
	
	public void addAll(List<NamedTargetExpression> parameters) {
		for (NamedTargetExpression t : parameters)
			add(t);
	}

	@Override
	public ArgsPointcutExpression clone() {
		ArgsPointcutExpression clone = new ArgsPointcutExpression();
		
		for (NamedTargetExpression type : parameters())
			clone.add(type.clone());
		
		return clone;
	}	
}