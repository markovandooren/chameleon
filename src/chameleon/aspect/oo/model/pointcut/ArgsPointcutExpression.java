package chameleon.aspect.oo.model.pointcut;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.oo.expression.NamedTargetExpression;

public class ArgsPointcutExpression<E extends ArgsPointcutExpression<E>> extends AbstractParameterExposurePointcutExpression<E> implements RuntimePointcutExpression<E,Element> {
	
	private OrderedMultiAssociation<ArgsPointcutExpression<E>, NamedTargetExpression> _parameters = new OrderedMultiAssociation<ArgsPointcutExpression<E>, NamedTargetExpression>(this);
	
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
	public List<? extends Element> children() {
		return parameters();
	}

	@Override
	public E clone() {
		ArgsPointcutExpression<E> clone = new ArgsPointcutExpression<E>();
		
		for (NamedTargetExpression type : parameters())
			clone.add(type.clone());
		
		return (E) clone;
	}	
}