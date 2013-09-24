package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NameExpression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.BasicTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class TypePointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	public TypePointcutExpression(NameExpression parameter) {
		setParameter(parameter);
	}
	
	private Single<NameExpression> _parameter = new Single<NameExpression>(this);
	
	public NameExpression parameter() {
		return _parameter.getOtherEnd();
	}
	
	public void setParameter(NameExpression parameter) {
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
	public List<NameExpression> parameters() {
		return Collections.singletonList(parameter());
	}
}
