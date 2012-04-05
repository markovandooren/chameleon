package chameleon.aspect.oo.model.pointcut;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.oo.expression.NamedTargetExpression;

public class ArgsPointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	
	private OrderedMultiAssociation<ArgsPointcutExpression, NamedTargetExpression> _parameters = new OrderedMultiAssociation<ArgsPointcutExpression, NamedTargetExpression>(this);
	
	public List<NamedTargetExpression> parameters() {
		return _parameters.getOtherEnds();
	}
	
	public void add(NamedTargetExpression parameter) {
		setAsParent(_parameters, parameter);
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