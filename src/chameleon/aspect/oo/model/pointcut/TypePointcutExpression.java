package chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.type.BasicTypeReference;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public abstract class TypePointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	public TypePointcutExpression(NamedTargetExpression parameter) {
		setParameter(parameter);
	}
	
	public abstract TypePointcutExpression clone();

	private SingleAssociation<TypePointcutExpression, NamedTargetExpression> _parameter = new SingleAssociation<TypePointcutExpression, NamedTargetExpression>(this);
	
	public NamedTargetExpression parameter() {
		return _parameter.getOtherEnd();
	}
	
	public void setParameter(NamedTargetExpression parameter) {
		setAsParent(_parameter, parameter);
	}
	
	public TypeReference getType() {
		try {
			return new BasicTypeReference(parameter().getType().getFullyQualifiedName());
		} catch (LookupException e) {
			
		}
		
		return null;
	}

	
	@Override
	public List<? extends Element> children() {
		return Util.createNonNullList(parameter());
	}
	
	@Override
	public List<NamedTargetExpression> parameters() {
		return Collections.singletonList(parameter());
	}
}