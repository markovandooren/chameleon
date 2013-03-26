package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NamedTargetExpression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.BasicTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class TypePointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	public TypePointcutExpression(NamedTargetExpression parameter) {
		setParameter(parameter);
	}
	
	public abstract TypePointcutExpression clone();

	private Single<NamedTargetExpression> _parameter = new Single<NamedTargetExpression>(this);
	
	public NamedTargetExpression parameter() {
		return _parameter.getOtherEnd();
	}
	
	public void setParameter(NamedTargetExpression parameter) {
		set(_parameter, parameter);
	}
	
	public TypeReference getType() {
		try {
			return new BasicTypeReference(parameter().getType().getFullyQualifiedName());
		} catch (LookupException e) {
			
		}
		
		return null;
	}

	
	@Override
	public List<NamedTargetExpression> parameters() {
		return Collections.singletonList(parameter());
	}
}
